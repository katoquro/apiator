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
package com.ainrif.apiator.core.reflection

import com.ainrif.apiator.api.annotation.ConcreteTypes
import com.ainrif.apiator.core.model.api.ApiEndpointParam
import com.ainrif.apiator.core.model.api.ApiEndpointReturnType
import com.ainrif.apiator.core.model.api.ApiType

import java.lang.annotation.Annotation
import java.lang.reflect.Method
import java.lang.reflect.Parameter

import static java.util.Collections.singletonList
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation

/**
 * List of method overrides from parent (interface/superclass) to child (implementation)
 */
abstract class MethodStack extends ArrayList<Method> {

    protected MethodStack(Collection<? extends Method> collection) {
        super(collection)
    }

    abstract String getName()

    abstract String getPath()

    abstract String getMethod()

    abstract List<ApiEndpointParam> getParams()

    /**
     * This method is used to merge apiator docs and java doc parser result
     *
     * @return not-unique identifier of this method
     */
    final MethodSignature getMethodSignature() {
        return new MethodSignature(this.last())
    }

    /**
     * @return type of implementation or defined in annotation
     *
     * @see com.ainrif.apiator.api.annotation.ConcreteTypes
     */
    List<ApiEndpointReturnType> getReturnTypes() {
        def leaf = this.last()
        return (findAnnotation(leaf, ConcreteTypes)?.value()?.toList() ?: singletonList(leaf.genericReturnType))
                .collect { new ApiType(it) }
                .collect { new ApiEndpointReturnType(type: it) }
    }

    /**
     * Collects annotations from method parameters from hierarchy tree in order from child to parent.
     * Annotation List can be empty if param is not annotated at all
     *
     * @return [ < param index > : [inherited annotations] ]
     */
    protected Map<Integer, List<Annotation>> getParametersAnnotationsLists() {
        def result = new HashMap<Integer, List<Annotation>>().withDefault { [] }
        this.each {
            (it.parameterAnnotations as List<Annotation[]>).eachWithIndex { Annotation[] entry, int i ->
                result[i].addAll(entry as List)
            }
        }

        result.each { i, l -> l.reverse(true) }

        return result
    }

    /**
     * Collect method parameter names from stack in order from child to parent.
     * This method collects only names provided by class, i.e. compile args like `-parameters` (for java) were used.
     * Parameter names list can be empty if there are no information provided by compiler.
     *
     * @return [ < param index > : [param names] ]
     */
    protected Map<Integer, List<String>> getParametersNameLists() {
        def result = new HashMap<Integer, List<String>>().withDefault { [] }
        this.each {
            it.parameters.eachWithIndex { Parameter p, int i ->
                if (p.namePresent) {
                    result[i] << p.name
                }
            }
        }

        result.each { i, l -> l.reverse(true) }

        return result
    }
}
