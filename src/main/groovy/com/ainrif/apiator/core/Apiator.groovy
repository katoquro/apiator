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
package com.ainrif.apiator.core

import com.ainrif.apiator.core.model.api.ApiContext
import com.ainrif.apiator.core.model.api.ApiEndpoint
import com.ainrif.apiator.core.model.api.ApiScheme
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder

class Apiator {

    private ApiatorConfig config;

    private ApiScheme scheme

    Apiator(ApiatorConfig config) {
        this.config = config
    }

    ApiScheme getScheme() {
        scheme ?: {
            scheme = new ApiScheme(basePath: config.basePath, version: config.apiVersion)
            Set<Class<?>> apiClasses = scanForApi()

            apiClasses.each {
                def ctxStack = config.provider.getContextStack(it)
                def apiCtx = new ApiContext(title: ctxStack.title, apiPath: ctxStack.apiContextPath)

                apiCtx.apiEndpoints += config.provider
                        .getMethodStacks(ctxStack)
                        .collect {
                    new ApiEndpoint(
                            name: it.title,
                            path: it.path,
                            method: it.method,
                            returnType: it.returnType,
                            params: it.params,
                            usedEnumerations: it.usedEnumerations,
                            usedApiTypes: it.usedApiTypes)
                }

                apiCtx.usedEnumerations = apiCtx.apiEndpoints
                        .collect { it.usedEnumerations }
                        .flatten()

                apiCtx.usedApiTypes = apiCtx.apiEndpoints
                        .collect { it.usedApiTypes }
                        .flatten()

                scheme.usedEnumerations += apiCtx.usedEnumerations
                scheme.usedApiTypes += apiCtx.usedApiTypes

                scheme.apiContexts << apiCtx
            }

            scheme
        }()
    }

    String render() {
        config.renderer.render(getScheme())
    }

    protected Set<Class<?>> scanForApi() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .addUrls(ClasspathHelper.forPackage(config.basePackage))
                        .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner())
                        .filterInputsBy(new FilterBuilder().includePackage(config.basePackage))
        )

        reflections.getTypesAnnotatedWith(config.apiClass)
    }
}
