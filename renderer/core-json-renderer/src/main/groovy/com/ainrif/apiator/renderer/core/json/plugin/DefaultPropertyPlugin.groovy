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

package com.ainrif.apiator.renderer.core.json.plugin

import com.ainrif.apiator.api.annotation.Param
import com.ainrif.apiator.core.model.api.ApiField
import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.core.reflection.RUtils
import com.ainrif.apiator.renderer.plugin.spi.property.PropertyPlugin
import com.ainrif.apiator.renderer.plugin.spi.property.PropertyViewData
import org.apache.commons.lang3.StringUtils

import javax.annotation.Nullable
import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.annotation.Annotation
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.function.Predicate

import static java.util.Arrays.asList
import static java.util.Collections.singletonMap
import static java.util.Objects.nonNull

/**
 * Tries to resolve properties according to next rules
 *
 * <ol>
 *     <li>java bean properties</li>
 *     <li>public fields</li>
 * </ol>
 *
 * @return found properties
 */
class DefaultPropertyPlugin implements PropertyPlugin {

    @Override
    Collection<ApiField> collectProperties(ApiType apiType) {
        def type = apiType.rawType
        def beanInfo = type.interface ?
                Introspector.getBeanInfo(type) :
                Introspector.getBeanInfo(type, Object)

        Map<String, ApiField> props = beanInfo.propertyDescriptors
                .collectEntries { mapFromPropertyDescriptor(it, type) }

        props += RUtils.getAllFields(type, { Modifier.isPublic(it.modifiers) } as Predicate)
                .collectEntries { mapFromField(it, type) }

        return props.values()
    }

    @Override
    PropertyViewData process(ApiField apiField) {
        def result = new PropertyViewData()
        def annotations = apiField.annotations

        def annotation = annotations.find { Param.isAssignableFrom(it.annotationType()) }
        if (annotation) {
            def paramAnnotation = annotation as Param
            result.defaultValue = StringUtils.trimToNull(paramAnnotation.defaultValue())
            result.optional = paramAnnotation.optional()
        }

        annotation = annotations.find { Nullable.isAssignableFrom(it.annotationType()) }
        if (annotation) {
            result.optional = true
        }

        return result
    }

    protected Map<String, ApiField> mapFromPropertyDescriptor(PropertyDescriptor pd, Class<?> enclosingType) {
        def name = pd.name
        def field = createApiFieldPropertyDescriptor(name, pd, enclosingType)

        return singletonMap(name, field)
    }

    protected ApiField createApiFieldPropertyDescriptor(String name, PropertyDescriptor pd, Class<?> enclosingType) {
        return new ApiField(
                name: name,
                type: nonNull(pd.readMethod)
                        ? new ApiType(pd.readMethod.genericReturnType)
                        : new ApiType(pd.writeMethod.genericParameterTypes[0]),
                enclosingType: new ApiType(enclosingType),
                annotations: mergeAnnotationsFromPropertyDescriptor(pd),
                readable: nonNull(pd.readMethod),
                writable: nonNull(pd.writeMethod))
    }

    protected Map<String, ApiField> mapFromField(Field f, Class<?> enclosingType) {
        def name = f.name
        def field = createApiFieldClassField(name, f, enclosingType)

        return singletonMap(name, field)
    }

    protected ApiField createApiFieldClassField(String name, Field f, Class<?> enclosingType) {
        return new ApiField(
                name: name,
                type: new ApiType(f.genericType),
                enclosingType: new ApiType(enclosingType),
                annotations: f.declaredAnnotations as List<? extends Annotation>,
                readable: true,
                writable: true)
    }

    private List<? extends Annotation> mergeAnnotationsFromPropertyDescriptor(PropertyDescriptor pd) {
        List<Annotation> readMethodAnnotations = pd.readMethod ? asList(pd.readMethod.declaredAnnotations) : []
        List<Annotation> writeMethodAnnotations = pd.writeMethod ? asList(pd.writeMethod.declaredAnnotations) : []

        return readMethodAnnotations + writeMethodAnnotations
    }
}