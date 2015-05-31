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
package com.ainrif.apiator.writer.core.view

import com.ainrif.apiator.core.model.Helper
import com.ainrif.apiator.core.model.api.ApiScheme

class ApiSchemeView {
    Map<String, String> apiatorInfo
    String version
    String basePath
    List<ApiContextView> apiContexts = []
    List<ApiEnumerationView> usedEnumerations = []
    List<ApiTypeView> usedApiTypes = []

    ApiSchemeView(ApiScheme scheme) {
        this.apiatorInfo = scheme.apiatorInfo.toMap()
        this.version = scheme.version
        this.basePath = scheme.basePath
        this.apiContexts = scheme.apiContexts.collect { new ApiContextView(it) }.sort()
        this.usedEnumerations = scheme.usedEnumerations.collect { new ApiEnumerationView(it) }.sort()

        this.usedApiTypes = scheme.usedApiTypes.collect {
            Map<String, ApiFieldView> fields = Helper
                    .getPropertiesTypes(it.rawType)
                    .collectEntries { k, v -> [k, new ApiFieldView(v)] }

            new ApiTypeView(it, fields)
        }.sort()
    }
}
