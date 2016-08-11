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

import com.ainrif.apiator.core.model.api.ApiContext
import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.core.reflection.RUtils
import com.ainrif.apiator.doclet.model.ClassInfo

import javax.annotation.Nullable

class JavaDocInfoIndexer {
    private Map<String, ClassInfo> classes;

    JavaDocInfoIndexer(Map<String, ClassInfo> classes) {
        this.classes = classes
    }

    @Nullable ClassMergedInfo getClassMergedInfo(ApiContext apiContext) {
        getClassMergedInfo(apiContext.apiType)
    }

    @Nullable ClassMergedInfo getClassMergedInfo(ApiType apiType) {
        List<ClassInfo> typeHierarchy = RUtils.getAllSuperTypes(apiType.rawType)
                .collect { classes[ClassInfo.createKey(it)] }
                .reverse()

        return typeHierarchy ?
                new ClassMergedInfo(typeHierarchy) :
                null
    }
}
