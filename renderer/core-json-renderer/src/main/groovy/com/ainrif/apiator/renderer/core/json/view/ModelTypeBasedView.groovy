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
package com.ainrif.apiator.renderer.core.json.view

import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.renderer.core.json.CoreJsonRenderer
import com.ainrif.apiator.renderer.plugin.spi.modeltype.ModelType

import javax.annotation.Nullable

abstract class ModelTypeBasedView implements Comparable<ModelTypeBasedView> {
    @Nullable
    String type
    ModelType modelType

    ModelTypeBasedView(ApiType type) {
        this.modelType = CoreJsonRenderer.getTypeByClass(type.rawType)
        this.type = [ModelType.OBJECT, ModelType.ENUMERATION]
                .any { it == CoreJsonRenderer.getTypeByClass(type.rawType) } ? type.rawType.name : null
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
            } else if (type.actuallyParametrised) {
                this.basedOn = type.actualParameters.collect { new ApiTypeGenericView(it) }
            } else {
                this.basedOn = []
            }
        }
    }
}
