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

import com.sun.javadoc.DocErrorReporter
import com.sun.javadoc.LanguageVersion
import com.sun.javadoc.RootDoc
import com.sun.tools.javadoc.Main
import groovy.json.JsonBuilder
import groovy.transform.Immutable

import javax.annotation.Nullable

import static java.io.File.createTempFile

/**
 * @see com.sun.javadoc.Doclet
 */
class ApiatorDoclet {
    public static final String OF_PARAM = '--output-file'

    /**
     * @param sourcePath
     * @param docletClassPath extra classpath
     * @param basePackage if null then '.' package will be used
     * @param outputFile if null then tmp file will be created
     * @return {@link Result}
     */
    static Result runDoclet(String sourcePath,
                            @Nullable String docletClassPath,
                            @Nullable String basePackage,
                            @Nullable String outputFile) {
        outputFile = outputFile ?: createTempFile('apiator', 'doclet').with { it.deleteOnExit(); it }.absolutePath
        basePackage = basePackage ?: '.'

        def javaDocArgs = ['-sourcepath', sourcePath,
                           '-doclet', ApiatorDoclet.class.name,
                           '-quiet',
                           OF_PARAM, outputFile,
                           '-subpackages', basePackage]

        if (docletClassPath) {
            javaDocArgs.add('-classpath')
            javaDocArgs.add(docletClassPath)
        }

        return new Result(Main.execute(javaDocArgs as String[]), outputFile)
    }

    static boolean start(RootDoc root) {
        def docInfo = new JavaDocInfoBuilder(root.classes()).content

        def filePath = getOptionValue(root.options(), OF_PARAM)[1]
        new File(filePath).write(new JsonBuilder(docInfo).toString())

        return true
    }

    static int optionLength(String option) {
        switch (option) {
            case OF_PARAM:
                return 2
            default:
                return 0
        }
    }

    static boolean validOptions(String[][] options, DocErrorReporter reporter) {
        String[] ofArray = getOptionValue(options, OF_PARAM)
        if (!(ofArray && ofArray[1])) {
            reporter.printError("Required parameter is absent: ${OF_PARAM} <file>")
            return false
        } else {
            def of = ofArray[1]
            if (!new File(of).canWrite()) {
                reporter.printError("Cannot write to file: ${of}")
                return false
            }
        }

        return true
    }

    static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5
    }

    @Immutable
    static class Result {
        int code
        String outputFile
    }

    @Nullable
    private static String[] getOptionValue(String[][] options, String name) {
        return options.find { it[0] == name }
    }
}
