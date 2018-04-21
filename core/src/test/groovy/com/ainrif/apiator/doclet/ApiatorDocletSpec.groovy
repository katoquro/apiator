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

import com.ainrif.apiator.doclet.model.JavaDocInfo
import groovy.json.JsonSlurper
import spock.lang.Specification

import java.nio.file.Paths

class ApiatorDocletSpec extends Specification {

    def "smoke test; all information should be stored/restored correctly"() {
        given:
        def sourcePath = Paths.get(System.getProperty('user.dir'), 'src', 'test', 'groovy').toString()
        def basePackage = 'com.ainrif.apiator.doclet'
        and:
        def testClassCanonicalName = DocletTestModel.class.canonicalName

        when:
        def result = ApiatorDoclet.runDoclet(sourcePath, basePackage, null)
        def index = new JsonSlurper().parse(new File(result.outputFile)) as JavaDocInfo

        then:
        index.classes[testClassCanonicalName].description == 'class level doc'
        index.classes[testClassCanonicalName].fields['stringField'].description == 'field level doc'
        def methodInfo = index.classes[testClassCanonicalName].methods['method_[java.lang.String, int]']
        methodInfo.description == 'method level doc'
        methodInfo.params['stringParam'].description == '1st param doc'
        methodInfo.params['intParam'].description == '2nd param doc'
    }
}
