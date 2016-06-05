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
package com.ainrif.apiator.renderer.core.json.view

import com.ainrif.apiator.core.model.api.ApiEndpoint
import com.ainrif.apiator.doclet.model.MethodInfo

import javax.annotation.Nullable

class ApiEndpointView implements Comparable<ApiEndpointView> {
    String name
    String description
    String path
    String method
    ApiEndpointReturnTypeView returnType
    List<ApiEndpointParamView> params = []

    ApiEndpointView(ApiEndpoint endpoint, @Nullable MethodInfo methodInfo) {
        this.name = endpoint.name
        this.description = methodInfo?.description
        this.path = endpoint.path
        this.method = endpoint.method
        this.returnType = new ApiEndpointReturnTypeView(endpoint.returnType)
        this.params = endpoint.params
                .collect { new ApiEndpointParamView(it, methodInfo?.findInfo(it)) }
                .sort()
    }

    @Override
    int compareTo(ApiEndpointView o) {
        return path.compareToIgnoreCase(o.path) ?: method <=> o.method
    }
}
