/*
 * Copyright 2014-2016 Ainrif <ainrif@outlook.com>
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
package com.ainrif.apiator.renderer.core.view

import com.ainrif.apiator.core.model.ModelType
import com.ainrif.apiator.core.model.api.ApiType

import javax.annotation.Nullable

abstract class ModelTypeBasedView implements Comparable<ModelTypeBasedView> {
    @Nullable String type
    ModelType modelType

    ModelTypeBasedView(ApiType type) {
        this.modelType = type.modelType
        this.type = [ModelType.OBJECT, ModelType.ENUMERATION].any { it == type.modelType } ? type.rawType.name : null
    }

    @Override
    int compareTo(ModelTypeBasedView o) {
        return type?.compareToIgnoreCase(o.type) ?: modelType <=> o.modelType
    }

    static class ApiTypeGenericView extends ModelTypeBasedView {
        String templateName
        List<ApiTypeGenericView> basedOn = []

        ApiTypeGenericView(ApiType type) {
            super(type)

            if (type.template) {
                this.templateName = type.templateName
            }

            if (type.array) {
                this.basedOn = [new ApiTypeGenericView(type.componentApiType)]
            } else if (type.generic) {
                this.basedOn = type.actualTypeArguments.collect { new ApiTypeGenericView(it) }
            } else {
                this.basedOn = []
            }
        }
    }
}
