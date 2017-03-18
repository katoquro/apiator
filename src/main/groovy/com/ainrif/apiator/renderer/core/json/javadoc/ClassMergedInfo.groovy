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

package com.ainrif.apiator.renderer.core.json.javadoc

import com.ainrif.apiator.core.model.api.ApiEndpoint
import com.ainrif.apiator.core.model.api.ApiField
import com.ainrif.apiator.doclet.model.ClassInfo
import com.ainrif.apiator.doclet.model.FieldInfo
import com.ainrif.apiator.doclet.model.MethodInfo

import javax.annotation.Nullable

class ClassMergedInfo {
    private List<ClassInfo> classInfos

    ClassMergedInfo(List<ClassInfo> classInfos) {
        this.classInfos = classInfos
    }

    @Nullable String getName() {
        return classInfos.findResult { it?.name }
    }

    @Nullable String getDescription() {
        return classInfos.findResult { it?.description }
    }

    @Nullable MethodMergedInfo getMethodMergedInfo(ApiEndpoint apiEndpoint) {
        def methodHierarchy = classInfos
                .collect { it.methods[MethodInfo.createKey(apiEndpoint)] }
                .findAll Objects.&nonNull

        return methodHierarchy ?
                new MethodMergedInfo(methodHierarchy) :
                null
    }

    @Nullable FieldMergedInfo getFieldMergedInfo(ApiField apiField) {
        def fieldHierarchy = classInfos
                .collect { it.fields[FieldInfo.createKey(apiField)] }
                .findAll Objects.&nonNull

        return fieldHierarchy ?
                new FieldMergedInfo(fieldHierarchy) :
                null
    }
}
