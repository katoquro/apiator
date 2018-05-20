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

package com.ainrif.apiator.renderer.plugin.jackson

import com.ainrif.apiator.core.model.api.ApiField
import com.ainrif.apiator.renderer.core.json.plugin.DefaultPropertyPlugin
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.core.annotation.AnnotationUtils

import javax.annotation.Nullable
import java.beans.PropertyDescriptor
import java.lang.annotation.Annotation
import java.lang.reflect.Field

import static java.util.Collections.singletonMap

/**
 * Use same rules like {@link DefaultPropertyPlugin} and
 * supports property name override via {@link com.fasterxml.jackson.annotation.JsonProperty}
 */
class JacksonPropertyPlugin extends DefaultPropertyPlugin {
    @Override
    protected Map<String, ApiField> mapFromPropertyDescriptor(PropertyDescriptor pd, Class<?> enclosingType) {

        def readName = pd.readMethod?.with {
            getNameFromAnnotation(AnnotationUtils.findAnnotation(it, JsonProperty), pd.name)
        }

        def writeName = pd.writeMethod?.with {
            getNameFromAnnotation(AnnotationUtils.findAnnotation(it, JsonProperty), pd.name)
        }

        if (readName && writeName && readName == writeName) {
            return singletonMap(readName, createApiFieldPropertyDescriptor(readName, pd, enclosingType))
        } else {
            Map<String, ApiField> result = [:]
            if (readName) {
                result.put(readName, createApiFieldPropertyDescriptor(readName, pd, enclosingType))
            }
            if (writeName) {
                result.put(writeName, createApiFieldPropertyDescriptor(writeName, pd, enclosingType))
            }
            return result
        }
    }

    @Override
    protected Map<String, ApiField> mapFromField(Field f, Class<?> enclosingType) {
        def name = getNameFromAnnotation(f.getDeclaredAnnotation(JsonProperty), f.name)
        def field = createApiFieldClassField(name, f, enclosingType)

        return singletonMap(name, field)
    }

    protected String getNameFromAnnotation(@Nullable Annotation annotation, String defaultName) {
        def result = null
        if (annotation) {
            def value = annotation.asType(JsonProperty).value()
            if (value != JsonProperty.USE_DEFAULT_NAME) {
                result = value
            }
        }

        return result ?: defaultName
    }
}
