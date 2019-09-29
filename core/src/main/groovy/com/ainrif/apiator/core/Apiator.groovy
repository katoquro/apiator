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

import com.ainrif.apiator.core.model.api.*
import com.ainrif.apiator.doclet.ApiatorDoclet
import com.ainrif.apiator.doclet.SourcePathDetector
import com.ainrif.apiator.doclet.javadoc.DocletInfoIndexer
import com.ainrif.apiator.doclet.model.JavaDocInfo
import com.google.common.base.Stopwatch
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.apache.groovy.lang.annotation.Incubating
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder

import javax.tools.ToolProvider
import java.util.concurrent.TimeUnit

@Slf4j
class Apiator {
    private URL[] extraClassPath

    protected ApiatorConfig config
    protected ApiatorInfo info

    protected ApiScheme scheme

    Apiator(ApiatorConfig config) {
        this.config = config
        this.info = new ApiatorInfo(config)
    }

    String render() {
        def scheme = getScheme()
        def stopwatch = Stopwatch.createStarted()

        def render = config.renderer.render(scheme)

        log.info('Scheme rendering took {}ms.', stopwatch.elapsed(TimeUnit.MILLISECONDS))
        return render
    }

    protected ApiScheme getScheme() {
        return scheme ?: {
            def stopwatch = Stopwatch.createStarted()
            scheme = new ApiScheme(apiatorInfo: info, clientApiInfo: new ClientApiInfo(config))

            Set<Class<?>> apiClasses = scanForApi()

            apiClasses
                    .findResults { config.provider.getContextStack(new ApiType(it)) }
                    .collect { ctxStack ->
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

                        return apiCtx
                    }
                    .with { scheme.apiContexts.addAll(it) }

            if (config.docletConfig.enabled) {
                if (!config.docletConfig.includeBasePackage) {
                    throw new RuntimeException('includeBasePackage is required parameter for doclet data source')
                }

                scheme.docletIndex = createJavadocIndexer(config.docletConfig)
            }

            log.info('Api Scheme generating took {}ms. {} contexts were processed',
                    stopwatch.elapsed(TimeUnit.MILLISECONDS),
                    apiClasses.size())

            return scheme
        }()
    }

    protected Set<Class<?>> scanForApi() {
        def configurationBuilder = new ConfigurationBuilder()
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner())
                .filterInputsBy(new FilterBuilder().includePackage(config.basePackage))

        if (extraClassPath) {
            def additionalClassLoader = new URLClassLoader(extraClassPath, Apiator.classLoader)
            configurationBuilder
                    .addClassLoader(additionalClassLoader)
                    .addUrls(ClasspathHelper.forPackage(config.basePackage, additionalClassLoader))
        } else {
            configurationBuilder
                    .addUrls(ClasspathHelper.forPackage(config.basePackage))
        }

        return new Reflections(configurationBuilder)
                .getTypesAnnotatedWith(config.apiClass)
    }

    protected DocletInfoIndexer createJavadocIndexer(DocletConfig dConf) {
        if (!dConf.sourcePath) {
            log.info('Source path is empty. An attempt to detect')
            dConf.sourcePath = new SourcePathDetector(scheme).detect()
        }

        if (dConf.sourcePath) {
            // TODO katoquro: 22/04/2018 support classpath search when all paths are wrong
            dConf.sourcePath.split(SourcePathDetector.OS_PATH_DELIMITER)
                    .findAll { !new File(it).exists() }
                    .each { log.warn('There are no source path like {}', it) }

            def docletClassPath = null
            if (extraClassPath) {
                docletClassPath = extraClassPath.findAll { new File(it.file).exists() }
                        .collect { it.file }
                        .join(SourcePathDetector.OS_PATH_DELIMITER)
            }

            if (!ToolProvider.getSystemDocumentationTool()) {
                log.info('JavaDoc Spi was not found. Jdk11+ required')
                return new DocletInfoIndexer([:])
            }

            def result = ApiatorDoclet.runDoclet(dConf.sourcePath, docletClassPath, dConf.includeBasePackage, null)
            if (!result.success) {
                log.warn('Execution of doclet was unsuccessful. Doc source will be skipped')
                return new DocletInfoIndexer([:])
            }

            def filePath = result.outputFile
            def javaDocInfo = new JsonSlurper().parse(new File(filePath)) as JavaDocInfo

            return new DocletInfoIndexer(javaDocInfo.classes)
        } else {
            log.info('Source path for Doclet was not detected. No Doclet info was found')
        }

        return new DocletInfoIndexer([:])
    }

    /**
     * Given urls will be used as extra classpath during generation
     * @param urls correspond to requirements {@link URLClassLoader}
     */
    @Incubating
    void setExtraClassPath(URL[] urls) {
        this.extraClassPath = urls
    }
}
