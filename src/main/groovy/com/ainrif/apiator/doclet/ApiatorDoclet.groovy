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

import com.sun.javadoc.Doclet
import com.sun.javadoc.RootDoc
import com.sun.tools.javadoc.Main
import groovy.json.JsonBuilder

import javax.annotation.Nullable

class ApiatorDoclet extends Doclet {
    public static final String OF_PARAM = '--output-file'

    /**
     * @param sourcePath
     * @param basePackage
     * @param outputFile if null then tmp file will be created
     * @return outputFile absolute path
     */
    public static String runDoclet(String sourcePath,
                                   String basePackage,
                                   @Nullable String outputFile) {
        outputFile = outputFile ?: File.createTempFile('apiator', 'doclet').absolutePath

        String[] javaDocArgs = ['-sourcepath', sourcePath,
                                '-doclet', ApiatorDoclet.class.name,
                                '-quiet',
                                OF_PARAM, outputFile,
                                '-subpackages', basePackage]
        Main.execute(javaDocArgs)

        return outputFile
    }

    public static boolean start(RootDoc root) {
        def docInfo = new JavaDocInfoIndexer(root.classes()).index

        def filePath = root.options().find { it[0] == OF_PARAM }[1]
        new File(filePath).write(new JsonBuilder(docInfo).toString())

        return true;
    }

    //todo validation: write access
    public static int optionLength(String option) {
        switch (option) {
            case OF_PARAM:
                return 2
            default:
                return 0
        }
    }
}
