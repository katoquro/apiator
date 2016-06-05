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

import com.ainrif.apiator.core.model.Helper
import com.ainrif.apiator.core.model.api.ApiScheme
import com.ainrif.apiator.core.reflection.RUtils
import com.ainrif.apiator.doclet.model.JavaDocInfo

import javax.annotation.Nullable

class ApiSchemeView {
    Map<String, String> apiatorInfo
    Map<String, String> clientApiInfo
    List<ApiContextView> apiContexts = []
    List<ApiEnumerationView> usedEnumerations = []
    List<ApiTypeView> usedApiTypes = []

    ApiSchemeView(ApiScheme scheme, @Nullable JavaDocInfo docInfo) {
        this.apiatorInfo = RUtils.asMap(scheme.apiatorInfo)
        this.clientApiInfo = RUtils.asMap(scheme.clientApiInfo)
        this.apiContexts = scheme.apiContexts
                .collect { new ApiContextView(it, docInfo?.findInfo(it)) }
                .sort()
        this.usedEnumerations = scheme.usedEnumerations
                .collect { new ApiEnumerationView(it, docInfo?.findInfo(it)) }
                .sort()

        this.usedApiTypes = scheme.usedApiTypes.collect {
            def classInfo = docInfo?.findInfo(it)
            List<ApiFieldView> fields = Helper.getPropertiesTypes(it.rawType)
                    .collect { k, v -> new ApiFieldView(v, classInfo?.findInfo(v)) }

            new ApiTypeView(it, fields)
        }
        .unique().sort()
    }
}