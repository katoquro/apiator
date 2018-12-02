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
    static Map<MethodSignature, List<Method>> getAllMethods(final Class<?> type,
                                                            Predicate<? super Method>... predicates) {
        Map<MethodSignature, List<Method>> map = [:].withDefault { [] }

        getAllSuperTypes(type)
                .each
                {
                    it.declaredMethods
                            .findAll { !it.bridge }
                            .findAll { method -> predicates.every { it.test(method) } }
                            .each { method -> map[new MethodSignature(method)] << (method) }
                }

        map
    }

    /**
     * @param type
     * @return all classes from {@link Object} (exclusive) to {@code type}.
     * Interfaces -> SuperClasses -> type
     */
    static List<Class<?>> getAllSuperTypes(final Class<?> type) {
        (getAllSuperClasses(type) + getAllSuperInterfaces(type))
                .asType(List)
                .reverse()
    }

    /**
     * @param type for scan
     * @return all classes from {@code type} to {@link Object} (exclusive the last)
     */
    protected static List<Class> getAllSuperClasses(final Class<?> type) {
        List<Class<?>> classes = []
        if (type && type != Object) {
            classes << type
            classes += getAllSuperClasses(type.superclass)
        }

        classes
    }

    /**
     * @param type for scan
     * @return all interfaces from {@code type} to {@link Object} (exclusive the last) by DFS
     */
    protected static List<Class> getAllSuperInterfaces(final Class<?> type) {
        List<Class<?>> interfaces = []

        if (type && type != Object) {
            if (type.isInterface()) {
                interfaces << type
            }
            type.interfaces.each { interfaces += getAllSuperInterfaces(it) }

            interfaces += getAllSuperInterfaces(type.superclass)
        }

        interfaces
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
        filters[0] = {
            !Modifier.isStatic(it.modifiers) && !Modifier.isTransient(it.modifiers)
        } as Predicate<? super Field>

        System.arraycopy(predicates, 0, filters, 1, predicates.length);

        return getAllFields(type, filters)
    }

    /**
     * collects annotations from method hierarchy tree
     *
     * @param annotationClass
     */
    static <A extends Annotation, E extends AnnotatedElement> List<A> getAnnotationList(List<E> elements, Class<A> annotationClass) {
        elements.findAll { it.isAnnotationPresent(annotationClass) }
                .collect { it.getAnnotation(annotationClass) }
    }

    /**
     * converts POJO to Map
     *
     * @param pojo
     * @return for map values it calls {@link Object#toString()} if field is {@null} returns {@null}
     */
    static Map<String, String> asMap(Object pojo) {
        pojo.class.declaredFields
                .findAll { !it.synthetic }
                .collectEntries { [(it.name), pojo."$it.name"?.toString()] }
    }

    static List<PropertyDescriptor> introspectProperties(Class<?> type) {
        List<PropertyDescriptor> propDescriptors

        switch (type) {
            case { type.interface }:
                propDescriptors = Introspector.getBeanInfo(type).propertyDescriptors
                break
            case { Enum.isAssignableFrom(type) }:
                propDescriptors = Introspector.getBeanInfo(type, Enum).propertyDescriptors
                break
            case { GroovyObject.isAssignableFrom(type) }:
                propDescriptors = Introspector.getBeanInfo(type, Object)
                        .propertyDescriptors
                        .findAll { !GROOVY_META_PROPS_NAMES.contains(it.name) }
                break
            default:
                propDescriptors = Introspector.getBeanInfo(type, Object).propertyDescriptors
        }

        return propDescriptors
    }
}
