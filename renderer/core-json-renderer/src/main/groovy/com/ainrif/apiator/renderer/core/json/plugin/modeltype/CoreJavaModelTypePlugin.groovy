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
package com.ainrif.apiator.renderer.core.json.plugin.modeltype

import com.ainrif.apiator.renderer.plugin.spi.modeltype.ModelType
import com.ainrif.apiator.renderer.plugin.spi.modeltype.ModelTypePlugin

/**
 * Try to resolve types like: {@code void}, {@code enum}, {@code boolean}, {@code double}...
 * And boxed versions of them
 */
class CoreJavaModelTypePlugin implements ModelTypePlugin {
    @Override
    ModelType resolve(Class<?> type) {
        if ([CharSequence, Character, char].any { it.isAssignableFrom(type) }
                || URL == type
                || UUID == type) return ModelType.STRING

        if ([Void, void].any { it.isAssignableFrom(type) }) return ModelType.VOID
        if (type.enum || Enum.isAssignableFrom(type)) return ModelType.ENUMERATION

        if ([Boolean, boolean].any { it.isAssignableFrom(type) }) return ModelType.BOOLEAN
        if ([Byte, byte].any { it.isAssignableFrom(type) }) return ModelType.BYTE
        if ([Integer, Short, int, short].any { it.isAssignableFrom(type) }) return ModelType.INTEGER
        if ([Long, BigInteger, long].any { it.isAssignableFrom(type) }) return ModelType.LONG
        if ([Float, float].any { it.isAssignableFrom(type) }) return ModelType.FLOAT
        if ([Double, BigDecimal, double].any { it.isAssignableFrom(type) }) return ModelType.DOUBLE

        if (Class.isAssignableFrom(type)) return ModelType.SYSTEM

        return null
    }
}
