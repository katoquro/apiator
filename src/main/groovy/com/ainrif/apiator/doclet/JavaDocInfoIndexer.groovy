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

package com.ainrif.apiator.doclet

import com.ainrif.apiator.doclet.model.*
import com.sun.javadoc.ClassDoc
import com.sun.javadoc.FieldDoc
import com.sun.javadoc.MethodDoc

class JavaDocInfoIndexer {
    JavaDocInfo index

    JavaDocInfoIndexer(ClassDoc[] context) {
        this.index = context.collect { processClass(it) }
                .collectEntries { [ClassInfo.createKey(it), it] }
                .with { new JavaDocInfo(classes: it) }
    }

    private static ClassInfo processClass(ClassDoc context) {
        def result = new ClassInfo()

        result.name = context.qualifiedTypeName()
        result.description = context.commentText() ?: null

        result.methods = context.methods()
                .collect { processMethod(it) }
                .collectEntries { [MethodInfo.createKey(it), it] }

        result.fields = context.fields()
                .collect { processField(it) }
                .collectEntries { [FieldInfo.createKey(it), it] }

        return result
    }

    private static MethodInfo processMethod(MethodDoc methodDoc) {
        def result = new MethodInfo()

        result.name = methodDoc.name()
        result.paramTypeNames = methodDoc.parameters()*.type()*.qualifiedTypeName()
        result.description = methodDoc.commentText() ?: null

        result.params = methodDoc.paramTags()
                .collectEntries {
            def info = new ParamInfo(name: it.parameterName(), description: it.parameterComment())

            return [ParamInfo.createKey(info), info]
        }

        return result
    }

    private static FieldInfo processField(FieldDoc fieldDoc) {
        return new FieldInfo(name: fieldDoc.name(), description: fieldDoc.commentText() ?: null)
    }
}
