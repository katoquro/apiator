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
package com.ainrif.apiator.core

import com.ainrif.apiator.core.model.api.ApiContext
import com.ainrif.apiator.core.model.api.ApiEndpoint
import com.ainrif.apiator.core.model.api.ApiScheme
import com.ainrif.apiator.core.model.api.ClientApiInfo
import com.google.common.base.Stopwatch
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit

class Apiator {

    private static final Logger logger = LoggerFactory.getLogger(Apiator)

    protected ApiatorConfig config
    protected ApiatorInfo info

    protected ApiScheme scheme

    Apiator(ApiatorConfig config) {
        this.config = config
        this.info = new ApiatorInfo(config)
    }

    ApiScheme getScheme() {
        scheme ?: {
            def stopwatch = Stopwatch.createStarted()
            scheme = new ApiScheme(apiatorInfo: info, clientApiInfo: new ClientApiInfo(config))

            Set<Class<?>> apiClasses = scanForApi()

            apiClasses.each {
                def ctxStack = config.provider.getContextStack(it)
                def apiCtx = new ApiContext(
                        name: ctxStack.name,
                        apiPath: ctxStack.apiContextPath,
                        apiType: ctxStack.apiType)

                apiCtx.apiEndpoints += config.provider
                        .getMethodStacks(ctxStack)
                        .collect {
                    new ApiEndpoint(
                            name: it.name,
                            path: it.path,
                            method: it.method,
                            returnTypes: it.returnTypes,
                            params: it.params,
                            methodSignature: it.methodSignature
                    )
                }

                scheme.apiContexts << apiCtx
            }

            logger.info("Api Scheme generating took ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms. ${apiClasses.size()} contexts were processed")
            scheme
        }()
    }

    String render() {
        def scheme = getScheme()
        def stopwatch = Stopwatch.createStarted()

        def render = config.renderer.render(scheme)

        logger.info("Scheme rendering took ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms. ")
        render
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
