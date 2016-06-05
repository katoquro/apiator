/*
 * Copyright 2014-2016 Ainrif <support@ainrif.com>
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

package com.ainrif.apiator.doclet.model

import com.ainrif.apiator.core.model.api.ApiContext
import com.ainrif.apiator.core.model.api.ApiEndpoint
import com.ainrif.apiator.core.model.api.ApiField
import com.ainrif.apiator.core.model.api.ApiType

import javax.annotation.Nullable

class ClassInfo {
    String name
    String description
    Map<String, MethodInfo> methods
    Map<String, FieldInfo> fields

    @Nullable MethodInfo findInfo(ApiEndpoint apiEndpoint) {
        return methods[MethodInfo.createKey(apiEndpoint)]
    }

    @Nullable FieldInfo findInfo(ApiField apiField) {
        return fields[FieldInfo.createKey(apiField)]
    }

    static String createKey(ClassInfo info) {
        return info.name
    }

    static String createKey(ApiContext apiContext) {
        return apiContext.name
    }

    static String createKey(ApiType apiType) {
        return apiType.rawType.canonicalName
    }
}
