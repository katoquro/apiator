/*
 * Copyright 2014-2018 Ainrif <support@ainrif.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ainrif.apiator.core.reflection


import com.ainrif.apiator.core.model.api.ApiType
import groovy.transform.CompileDynamic

import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.annotation.Annotation
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Predicate

final class RUtils {
    private static final List<String> GROOVY_META_PROPS_NAMES = ['metaClass']

    /**
     * @param type for scan
     * @param predicates
     * @return methods grouped by signature from parent to child
     */
    static Map<MethodSignature, List<Method>> getAllMethods(final ApiType type,
                                                            Predicate<? super Method>... predicates) {
        Map<MethodSignature, List<Method>> map = [:].withDefault { [] }

        getAllSuperTypes(type).each {
            it.rawType.declaredMethods
                    .findAll { !it.bridge }
                    .findAll { method -> predicates.every { it.test(method) } }
                    .each { method -> map[new MethodSignature(method)] << (method) }
        }

        return map
    }

    /**
     * @param type
     * @return all classes from {@link Object} (exclusive) to {@code type}.
     * Interfaces -> SuperClasses -> type
     */
    static List<ApiType> getAllSuperTypes(ApiType type) {
        return (getAllSuperClasses(type) + getAllSuperInterfaces(type))
                .asType(List)
                .reverse()
    }

    /**
     * @param type for scan
     * @return all classes from {@code type} to {@link Object} (exclusive the last)
     */
    protected static List<ApiType> getAllSuperClasses(ApiType type) {
        List<ApiType> result = []
        if (type.rawType != Object) {
            result << type

            def superclass = type.rawType.superclass
            if (superclass) {
                result += getAllSuperClasses(new ApiType(superclass))
            }
        }

        return result
    }

    /**
     * @param type for scan
     * @return all interfaces from {@code type} to {@link Object} (exclusive the last) by DFS
     */
    protected static List<ApiType> getAllSuperInterfaces(ApiType type) {
        List<ApiType> result = []

        if (type.rawType != Object) {
            if (type.rawType.isInterface()) {
                result << type
            }
            type.rawType.interfaces.each { result += getAllSuperInterfaces(new ApiType(it)) }

            def superclass = type.rawType.superclass
            if (superclass) {
                result += getAllSuperInterfaces(new ApiType(superclass))
            }
        }

        return result
    }

    /**
     * @param type to scan from
     * @param predicates
     * @return fields from class and all parents
     */
    static List<Field> getAllFields(Class<?> type,
                                    Predicate<? super Field>... predicates) {
        def _clazz = type
        List<Field> fields = _clazz.interface ? [] : _clazz.declaredFields as List

        while (null != (_clazz = _clazz.superclass)) {
            fields += _clazz.declaredFields as List
        }

        return fields.findAll { Field field ->
            predicates.every { it.test(field) }
        }
    }

    /**
     * Shortcut for {@link #getAllFields(java.lang.Class, java.util.function.Predicate [ ])}
     * with predicated to filter out transient and static fields
     * @param type
     * @return
     */
    static List<Field> getAllDeclaredDynamicFields(Class<?> type,
                                                   Predicate<? super Field>... predicates) {
        def filters = new Predicate<? super Field>[predicates.length + 1]
        filters[0] = { Field f ->
            !Modifier.isStatic(f.modifiers) && !Modifier.isTransient(f.modifiers)
        } as Predicate<? super Field>

        System.arraycopy(predicates, 0, filters, 1, predicates.length);

        return getAllFields(type, filters)
    }

    /**
     * collects annotations from method hierarchy tree
     *
     * @param annotationClass
     */
    static <A extends Annotation, E extends AnnotatedElement> List<A> getAnnotationList(List<E> elements,
                                                                                        Class<A> annotationClass) {
        return elements.findAll { it.isAnnotationPresent(annotationClass) }
                .collect { it.getAnnotation(annotationClass) }
    }

    /**
     * converts POJO to Map
     *
     * @param pojo
     * @return for map values it calls {@link Object#toString()} if field is {@null} returns {@null}
     */
    @CompileDynamic
    static Map<String, String> asMap(Object pojo) {
        return pojo.class.declaredFields
                .findAll { !it.synthetic }
                .collectEntries { [(it.name), pojo."$it.name"?.toString()] }
    }

    static List<PropertyDescriptor> introspectProperties(Class<?> type) {
        List<PropertyDescriptor> propDescriptors

        switch (type) {
            case { type.interface }:
                propDescriptors = Introspector.getBeanInfo(type).propertyDescriptors as List
                break
            case { Enum.isAssignableFrom(type) }:
                propDescriptors = Introspector.getBeanInfo(type, Enum).propertyDescriptors as List
                break
            case { GroovyObject.isAssignableFrom(type) }:
                propDescriptors = Introspector.getBeanInfo(type, Object)
                        .propertyDescriptors
                        .findAll { !GROOVY_META_PROPS_NAMES.contains(it.name) } as List
                break
            default:
                propDescriptors = Introspector.getBeanInfo(type, Object).propertyDescriptors as List
        }

        return propDescriptors
    }
}
