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
package com.ainrif.apiator.core.model

import com.ainrif.apiator.core.model.api.ApiField
import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.core.reflection.RUtils

import java.beans.Introspector
import java.lang.reflect.Modifier
import java.util.function.Predicate

import static java.util.Objects.nonNull

final class Helper {
    static ModelType getTypeByClass(Class<?> type) {
        if ([CharSequence, Character, char, Enum].any { it.isAssignableFrom(type) }
                || URL == type
                || UUID == type) return ModelType.STRING

        else if ([Void, void].any { it.isAssignableFrom(type) }) return ModelType.VOID
        else if ([Byte, byte].any { it.isAssignableFrom(type) }) return ModelType.BYTE

        else if ([Boolean, boolean].any { it.isAssignableFrom(type) }) return ModelType.BOOLEAN
        else if ([Integer, Short, int, short].any { it.isAssignableFrom(type) }) return ModelType.INTEGER
        else if ([Long, long].any { it.isAssignableFrom(type) }) return ModelType.LONG
        else if ([Float, float].any { it.isAssignableFrom(type) }) return ModelType.FLOAT
        else if ([Double, double].any { it.isAssignableFrom(type) }) return ModelType.DOUBLE

        else if ([Date, Calendar].any { it.isAssignableFrom(type) }) return ModelType.DATE

        else if (Map.isAssignableFrom(type)) return ModelType.DICTIONARY

        else if (Set.isAssignableFrom(type)) return ModelType.SET

        else if (Iterable.isAssignableFrom(type)
                || type.isArray()) return ModelType.ARRAY

        return ModelType.OBJECT
    }

    /**
     * @return [ < field name > : < field type > ]
     */
    static Map<String, ApiField> getFieldsTypes(Class<?> type) {
        def beanInfo = type.isInterface() ?
                Introspector.getBeanInfo(type) :
                Introspector.getBeanInfo(type, Object)

        def props = beanInfo.propertyDescriptors
                .collect {
            new ApiField(
                    name: it.name,
                    type: nonNull(it.readMethod)
                            ? new ApiType(it.readMethod.genericReturnType)
                            : new ApiType(it.writeMethod.genericParameterTypes[0]),
                    readable: nonNull(it.readMethod),
                    writable: nonNull(it.writeMethod))
        }

        props += RUtils.getAllFields(type, { Modifier.isPublic(it.modifiers) } as Predicate)
                .collect {
            new ApiField(
                    name: it.name,
                    type: new ApiType(it.genericType),
                    readable: true,
                    writable: true)
        }

        props.collectEntries { [it.name, it] }
    }
}