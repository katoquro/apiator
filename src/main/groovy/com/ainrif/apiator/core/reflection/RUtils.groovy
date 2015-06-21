/*
 * Copyright 2014-2015 Ainrif <ainrif@outlook.com>
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

import java.lang.annotation.Annotation
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.function.Predicate

final class RUtils {
    /**
     * @param type for scan
     * @param predicates
     * @return methods grouped by signature from parent to child
     */
    public static Map<MethodSignature, List<Method>> getAllMethods(final Class<?> type,
                                                                   Predicate<? super Method>... predicates) {
        Map<MethodSignature, List<Method>> map = [:].withDefault { [] }

        getAllSuperTypes(type)
                .each
                {
                    it.declaredMethods
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
    public static List<Class<?>> getAllSuperTypes(final Class<?> type) {
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
    public static List<Field> getAllFields(final Class<?> type,
                                           Predicate<? super Field>... predicates) {
        def _clazz = type;
        List<Field> fields = _clazz.interface ? [] : _clazz.declaredFields as List

        while (null != (_clazz = _clazz.superclass)) {
            fields += _clazz.declaredFields as List
        }

        return fields.findAll { Field field ->
            predicates.every { it.test(field) }
        }
    }

    /**
     * collects annotations from method hierarchy tree
     *
     * @param annotationClass
     */
    public
    static <A extends Annotation, E extends AnnotatedElement> List<A> getAnnotationList(List<E> elements, Class<A> annotationClass) {
        elements.findAll { it.isAnnotationPresent(annotationClass) }
                .collect { it.getAnnotation(annotationClass) }
    }
}
