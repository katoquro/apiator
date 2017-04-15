/*
 * Copyright 2014-2017 Ainrif <support@ainrif.com>
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

package com.ainrif.apiator.renderer.core.json

import com.ainrif.apiator.core.model.api.ApiScheme
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Nullable
import java.nio.file.Path

import static groovy.io.FileType.FILES

class SourcePathDetector {
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
        List<String> classNames = apiScheme.apiContexts*.name
        if (!classNames) return null

        def classNamesToFind = new ArrayList(classNames)
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

        return paths.unique().join(':')
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

}
