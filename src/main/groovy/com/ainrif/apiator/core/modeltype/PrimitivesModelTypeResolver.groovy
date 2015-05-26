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
package com.ainrif.apiator.core.modeltype

import com.ainrif.apiator.api.ModelTypeResolver
import com.ainrif.apiator.core.model.ModelType

/**
 * Try to resolve types like: {@code void}, {@code enum}, {@code boolean}, {@code double}...
 * And boxed versions of them
 */
class PrimitivesModelTypeResolver implements ModelTypeResolver {
    @Override
    ModelType resolve(Class<?> type) {
        if ([CharSequence, Character, char].any { it.isAssignableFrom(type) }
                || URL == type
                || UUID == type) return ModelType.STRING

        if ([Void, void].any { it.isAssignableFrom(type) }) return ModelType.VOID
        else if (type.enum) return ModelType.ENUMERATION

        else if ([Boolean, boolean].any { it.isAssignableFrom(type) }) return ModelType.BOOLEAN
        else if ([Byte, byte].any { it.isAssignableFrom(type) }) return ModelType.BYTE
        else if ([Integer, Short, int, short].any { it.isAssignableFrom(type) }) return ModelType.INTEGER
        else if ([Long, BigInteger, long].any { it.isAssignableFrom(type) }) return ModelType.LONG
        else if ([Float, float].any { it.isAssignableFrom(type) }) return ModelType.FLOAT
        else if ([Double, BigDecimal, double].any { it.isAssignableFrom(type) }) return ModelType.DOUBLE

        return null
    }
}
