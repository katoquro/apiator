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

package com.ainrif.apiator.doclet

import com.ainrif.apiator.doclet.model.*
import com.sun.source.doctree.DocTree
import com.sun.source.doctree.ParamTree
import com.sun.source.doctree.TextTree
import jdk.javadoc.doclet.DocletEnvironment

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class JavaDocInfoBuilder {
    DocletEnvironment docEnv
    JavaDocInfo content

    TextDocTreeVisitor visitor = new TextDocTreeVisitor()

    JavaDocInfoBuilder(DocletEnvironment docEnv) {
        this.docEnv = docEnv
        this.content = docEnv.includedElements
                .findAll { it instanceof TypeElement }
                .collect { processClass(it as TypeElement) }
                .collectEntries { Map.of(ClassInfo.createKey(it), it) }
                .with { new JavaDocInfo(classes: it as Map<String, ClassInfo>) }
    }

    private ClassInfo processClass(TypeElement typeCtx) {
        def result = new ClassInfo()

        result.name = typeCtx.qualifiedName

        result.description = docEnv.docTrees.getDocCommentTree(typeCtx)?.with {
            it.fullBody.each { it.accept(visitor, null) }
                    .join('')
        }

        result.methods = typeCtx.enclosedElements
                .findAll { it instanceof ExecutableElement }
                .collect { processMethod(it as ExecutableElement) }
                .collectEntries { Map.of(MethodInfo.createKey(it), it) }

        result.fields = typeCtx.enclosedElements
                .findAll { it instanceof VariableElement }
                .collect { processField(it as VariableElement) }
                .collectEntries { Map.of(FieldInfo.createKey(it), it) }

        return result
    }

    private MethodInfo processMethod(ExecutableElement methodCtx) {
        def result = new MethodInfo()

        result.name = methodCtx.simpleName
        result.paramTypeNames = methodCtx.parameters*.asType()*.toString()
        result.description = docEnv.docTrees.getDocCommentTree(methodCtx)?.with {
            it.fullBody.each { it.accept(visitor, null) }
                    .join('')
        }

        def paramMap = docEnv.docTrees.getDocCommentTree(methodCtx)?.with {
            it.blockTags
                    .findAll { DocTree.Kind.PARAM == it.kind }
                    .collectEntries {
                        def param = it as ParamTree
                        def description = param.description
                                .findAll { DocTree.Kind.TEXT == it.kind }
                                .collect { (it as TextTree).body }
                                .join('')
                        def info = new ParamInfo(name: param.name.name.toString(), description: description)

                        return Map.of(ParamInfo.createKey(info), info)
                    } as Map<String, ParamInfo>
        }

        result.params = paramMap ?: Map.<String, ParamInfo> of()

        return result
    }

    private FieldInfo processField(VariableElement fieldCtx) {
        String description = docEnv.elementUtils.getDocComment(fieldCtx)?.trim() ?: null
        return new FieldInfo(name: fieldCtx.simpleName.toString(), description: description)
    }
}
