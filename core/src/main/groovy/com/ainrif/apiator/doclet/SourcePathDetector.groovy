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

import com.ainrif.apiator.core.model.api.ApiScheme
import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.SystemUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Nullable
import java.nio.file.Path

import static groovy.io.FileType.FILES

class SourcePathDetector {
    static final String OS_PATH_DELIMITER = SystemUtils.IS_OS_WINDOWS ? ';' : ':'

    private static final List<String> JDK_PACKAGE_PREFIXES = ['java', 'com.sun', 'com.oracle', 'javax', 'sun', 'oracle']
    private static final Logger logger = LoggerFactory.getLogger(SourcePathDetector)
    private static final int MAX_STEP_OUT = 3

    final ApiScheme apiScheme

    SourcePathDetector(ApiScheme apiScheme) {
        this.apiScheme = apiScheme
    }

    /**
     * Tries to find out source class folders based on API Scheme
     *
     * @return concatenated sting list of found absolute paths otherwise null
     */
    @Nullable
    String detect() {
        List<String> classNames = collectClassesNamesFromScheme(apiScheme)

        if (!classNames) return null

        def classNamesToFind = classNames.collect { it.split(/\$/).first() }.unique()
        def startDir = System.getProperty('user.dir')
        def paths = []
        for (int i = 0; i < MAX_STEP_OUT; i++) {
            def detectionResult = detectStartingFrom(startDir, classNamesToFind)
            paths += detectionResult.first
            def detectedClassNames = detectionResult.second

            def totalSize = classNames.size()
            classNamesToFind -= detectedClassNames
            logger.debug('Source paths for {} of {} classes were detected on this step',
                    detectedClassNames.size(), totalSize)

            if (classNamesToFind) {
                logger.debug('{} classes are not still resolved to source path', classNamesToFind)
                if (i + 1 < MAX_STEP_OUT) {
                    startDir = new File(startDir).parent
                    if (!startDir) {
                        logger.warn('Root dir was reached. Stop detection')
                    }
                    logger.debug('Step out to {} detect rest source paths', startDir)
                } else {
                    logger.warn('Some classes are note resoled to source path after {} steps to parent dir', MAX_STEP_OUT)
                }
            } else {
                logger.info('All classes were resolved in {} steps', i + 1)
                break
            }
        }

        if (!paths) {
            logger.warn('Skip auto configuration! Cannot find source class for Api Contexts: {}', classNames)
            return null
        }

        def result = paths.unique().sort()
        logger.info('Auto-configured paths: {}', result)
        return result.join(OS_PATH_DELIMITER)
    }

    /**
     * @return list of detected source paths and class which are contained in them
     */
    protected Tuple2<List<String>, Set<String>> detectStartingFrom(String sourcePath, List<String> classNames) {
        List<File> files = findClassFilesRecursively(sourcePath)

        Map<String, Path> classToSource = files.findResults { file ->
            def className = classNames.findResult { file.absolutePath ==~ /.*?${it}\.java/ ? it : null }
            className ? [(className): file.toPath()] : null
        }.collectEntries()

        def paths = classToSource.collect { name, file ->
            '/' + file.subpath(0, file.nameCount - name.tokenize('.').size()).toString()
        }

        return new Tuple2<List<String>, Set<String>>(paths.unique(), classToSource.keySet())
    }

    protected List<File> findClassFilesRecursively(String sourcePath) {
        List<File> files = []
        new File(sourcePath)
                .eachDirRecurse { it.eachFileMatch(FILES, ~/.*?\.java$/) { files << it } }

        return files
    }

    /**
     * Collect classes used in return types and method params and filter out jdk by packages
     * and primitives
     *
     * @return list of classes names
     */
    protected List<String> collectClassesNamesFromScheme(ApiScheme scheme) {
        return scheme.apiContexts*.name +
                scheme.apiContexts.collectMany {
                    it.apiEndpoints.collectMany {
                        (it.returnTypes*.type + it.params*.type)*.rawType
                                .findAll { !ClassUtils.isPrimitiveOrWrapper(it) }
                                .findAll { !it.isArray() }
                                *.name
                                .findAll { typeName -> JDK_PACKAGE_PREFIXES.every() { !typeName.startsWith(it) } }
                    }
                }
    }

}
