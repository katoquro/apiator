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

import groovy.json.JsonBuilder
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.util.logging.Slf4j
import jdk.javadoc.doclet.Doclet
import jdk.javadoc.doclet.DocletEnvironment
import jdk.javadoc.doclet.Reporter

import javax.annotation.Nullable
import javax.lang.model.SourceVersion
import javax.tools.Diagnostic
import javax.tools.ToolProvider

import static java.io.File.createTempFile

@Slf4j
@CompileStatic
class ApiatorDoclet implements Doclet {
    public static final String OF_PARAM = '--output-file'

    private Reporter reporter
    private String outputFilePath

    @Override
    void init(Locale locale, Reporter reporter) {
        this.reporter = reporter
    }

    @Override
    String getName() {
        return 'Apiator'
    }

    @Override
    SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_11
    }

    @Override
    boolean run(DocletEnvironment environment) {
        def docInfo = new JavaDocInfoBuilder(environment).content
        new File(outputFilePath).write(new JsonBuilder(docInfo).toString())

        return true
    }

    @Override
    Set<? extends Option> getSupportedOptions() {
        def ofOption = new Option() {
            @Override
            int getArgumentCount() {
                return 1
            }

            @Override
            String getDescription() {
                return 'Output file path'
            }

            @Override
            Option.Kind getKind() {
                return Option.Kind.STANDARD
            }

            @Override
            List<String> getNames() {
                return List.of(OF_PARAM)
            }

            @Override
            String getParameters() {
                return 'file'
            }

            @Override
            boolean process(String option, List<String> arguments) {
                if (arguments) {
                    outputFilePath = arguments[0]

                    if (!new File(outputFilePath).canWrite()) {
                        reporter.print(Diagnostic.Kind.ERROR, "Cannot write to file: ${outputFilePath}")
                        return false
                    }
                } else {
                    reporter.print(Diagnostic.Kind.ERROR, "File path is absent")
                }

                return true
            }
        }

        return Set.of(ofOption)
    }

    /**
     * @param sourcePath
     * @param docletClassPath extra classpath
     * @param basePackage if null then '.' package will be used
     * @param outputFile if null then tmp file will be created
     * @return {@link Result}
     */
    static Result runDoclet(String sourcePath,
                            @Nullable String docletClassPath,
                            String basePackage,
                            @Nullable String outputFile) {
        outputFile = outputFile ?: createTempFile('apiator', 'doclet').with { it.deleteOnExit(); it }.absolutePath

        def javaDocArgs = ['-sourcepath', sourcePath,
                           '-doclet', ApiatorDoclet.class.name,
                           '-quiet',
                           OF_PARAM, outputFile,
                           '-subpackages', basePackage]

        if (docletClassPath) {
            javaDocArgs.add('-classpath')
            javaDocArgs.add(docletClassPath)
        }

        try {
            boolean success = ToolProvider
                    .getSystemDocumentationTool()
                    .getTask(null, null, null, ApiatorDoclet, javaDocArgs, null)
                    .call()

            if (!success) {
                log.warn('Result of doclet was not successful and there are no exceptions')
            }

            new Result(success, outputFile)
        } catch (Throwable e) {
            log.error('Error during doclet executing.', e)

            new Result(false, outputFile)
        }
    }

    @Immutable
    static class Result {
        boolean success
        String outputFile
    }
}
