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

package com.ainrif.apiator.provider.micronaut

import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.core.reflection.ContextStack
import com.ainrif.apiator.core.reflection.MethodStack
import com.ainrif.apiator.core.reflection.RUtils
import com.ainrif.apiator.core.spi.WebServiceProvider
import io.micronaut.aop.Intercepted
import io.micronaut.http.annotation.*

import java.lang.annotation.Annotation
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Predicate

class MicronautProvider implements WebServiceProvider {

    List<Class<? extends Annotation>> wsAnnotations = [Controller,
                                                       Post,
                                                       Get,
                                                       Put,
                                                       Delete,
                                                       Options,
                                                       Head,
                                                       Patch,
                                                       Trace,
                                                       Produces,
                                                       Consumes,
                                                       Header,
                                                       Headers,
                                                       CookieValue,
                                                       QueryValue,
                                                       Part,
                                                       Body]

    @Override
    ContextStack getContextStack(ApiType apiClass) {
        if (Intercepted.isAssignableFrom(apiClass.rawType)) {
            return null
        }

        return new MicronautContextStack(RUtils.getAllSuperTypes(apiClass))
    }

    @Override
    List<? extends MethodStack> getMethodStacks(ContextStack contextStack) {
        def ctxStack = contextStack as MicronautContextStack
        return RUtils.getAllMethods(contextStack.apiType, { Method m -> Modifier.isPublic(m.modifiers) } as Predicate)
                .findAll { it.value.any { method -> wsAnnotations.any { method.isAnnotationPresent(it) } } }
                .collect { signature, methods -> new MicronautMethodStack(methods, ctxStack) }
    }
}
