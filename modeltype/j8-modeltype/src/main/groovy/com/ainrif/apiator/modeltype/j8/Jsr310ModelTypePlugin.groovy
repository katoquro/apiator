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
package com.ainrif.apiator.modeltype.j8

import com.ainrif.apiator.renderer.plugin.spi.ModelType
import com.ainrif.apiator.renderer.plugin.spi.ModelTypePlugin

import java.time.temporal.Temporal

class Jsr310ModelTypePlugin implements ModelTypePlugin {
    @Override
    ModelType resolve(Class<?> type) {
        if (Temporal.isAssignableFrom(type)) return ModelType.DATE

        return null
    }
}
