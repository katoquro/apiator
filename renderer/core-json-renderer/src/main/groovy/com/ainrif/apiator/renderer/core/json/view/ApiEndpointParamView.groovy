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

import com.ainrif.apiator.core.model.api.ApiEndpointParam
import com.ainrif.apiator.core.model.api.ApiEndpointParamType
import com.ainrif.apiator.renderer.core.json.CoreJsonRenderer
import com.ainrif.apiator.renderer.core.json.javadoc.ParamMergedInfo

import javax.annotation.Nullable

class ApiEndpointParamView extends ModelTypeBasedView.ApiTypeGenericView
        implements Comparable<ApiEndpointParamView> {
    String name
    String description
    ApiEndpointParamType httpParamType
    Integer index
    String defaultValue

    ApiEndpointParamView(ApiEndpointParam endpointParam, @Nullable ParamMergedInfo paramInfo) {
        super(endpointParam.type)

        this.name = endpointParam.name
        this.description = paramInfo?.description
        this.httpParamType = endpointParam.httpParamType
        this.index = endpointParam.index

        this.defaultValue = CoreJsonRenderer.pluginsConfig.paramPlugin
                .configure(endpointParam)
                .defaultValue
    }

    @Override
    int compareTo(ApiEndpointParamView o) {
        Integer.compare(this.index, o.index)
    }
}
