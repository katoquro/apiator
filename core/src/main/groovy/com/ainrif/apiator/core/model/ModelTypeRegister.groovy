/*
 * Copyright 2014-2017 Ainrif <support@ainrif.com>
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

import com.ainrif.apiator.core.modeltype.*
import com.ainrif.apiator.core.spi.ModelTypeResolver

class ModelTypeRegister {
    private List<ModelTypeResolver> modelTypeResolvers

    ModelTypeRegister() {
        this.modelTypeResolvers = [
                new CoreJavaModelTypeResolver(),
                new OldDateModelTypeResolver(),
                new BinaryModelTypeResolver(),
                new CollectionsModelTypeResolver(),
                new AnyModelTypeResolver()
        ]
    }

    ModelTypeRegister(List<ModelTypeResolver> additionalModelTypeResolvers) {
        this()
        this.modelTypeResolvers = additionalModelTypeResolvers + modelTypeResolvers
    }

    ModelType getTypeByClass(Class<?> type) {
        for (ModelTypeResolver mtr : modelTypeResolvers) {
            def resolved = mtr.resolve(type)
            if (resolved) return resolved
        }

        return ModelType.OBJECT
    }
}
