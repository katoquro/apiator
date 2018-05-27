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

package com.ainrif.apiator.doclet;

import com.ainrif.apiator.core.model.api.ApiContext
import com.ainrif.apiator.core.model.api.ApiScheme
import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.doclet.SourcePathDetector
import spock.lang.Specification
import spock.lang.Unroll

import static com.ainrif.apiator.doclet.SourcePathDetector.OS_PATH_DELIMITER

class SourcePathDetectorSpec extends Specification {

    def "source directories should be detected for classes, inner classes and enums"() {
        given:
        def scheme = Mock(ApiScheme) {
            getApiContexts() >> [new ApiContext(name: 'org.example.MyClass'),
                                 new ApiContext(name: 'org.example.MyClass2')]
            getUsedApiTypes() >> [new ApiType(InnerClass)]
            getUsedEnumerations() >> [new ApiType(InnerEnum)]
        }
        def detector = Spy(SourcePathDetector, constructorArgs: [scheme]) {
            detectStartingFrom(_ as String, _ as List<String>) >>> detecten_step
        }

        expect:
        detector.detect() == expected

        where:
        detecten_step << [
                [new Tuple2<>(['/NO_ROOT/prj/src/main/java'], ['org.example.MyClass', 'org.example.MyClass2'])],
                [new Tuple2<>(['/NO_ROOT/prj/src/main/java'], ['org.example.MyClass']),
                 new Tuple2<>(['/NO_ROOT/prj2/src/main/java'], ['org.example.MyClass2'])],
                [new Tuple2<>(['/NO_ROOT/prj/src/main/java'], ['com.ainrif.apiator.renderer.core.json.SourcePathDetectorSpec']),
                 new Tuple2<>(['/NO_ROOT/prj2/src/main/java'], ['com.ainrif.apiator.renderer.core.json.SourcePathDetectorSpec'])]
        ]
        expected << ['/NO_ROOT/prj/src/main/java',
                     ['/NO_ROOT/prj/src/main/java', '/NO_ROOT/prj2/src/main/java'].join(OS_PATH_DELIMITER),
                     ['/NO_ROOT/prj/src/main/java', '/NO_ROOT/prj2/src/main/java'].join(OS_PATH_DELIMITER)]
    }

    @Unroll
    def ".detectStartingFrom"() {
        given:
        def detector = Spy(SourcePathDetector, constructorArgs: [null]) {
            findClassFilesRecursively(_ as String) >> {
                return [new File('/NO_ROOT/prj/src/main/java/org/example/MyClass.java'),
                        new File('/NO_ROOT/prj/src/main/java/org/example/MyClass2.java'),
                        new File('/NO_ROOT/prj/sub-prj/src/main/java/org/example/MyClass3.java'),
                        new File('/NO_ROOT/prj/build/classes/org/example/MyClass.class')]
            }
        }

        when:
        def actual = detector.detectStartingFrom(starting_sp, starting_cn)

        then:
        actual.first.size() == sp_count
        actual.first.containsAll(sp)
        and:
        actual.second.size() == cn_count
        actual.second.containsAll(cn)

        where:
        starting_sp << ['/NO_ROOT/prj/sub-prj/src',
                        '/NO_ROOT/prj/sub-prj/src',
                        '/NO_ROOT/prj']
        starting_cn << [['org.example.MyClass3'],
                        ['org.example.MyClass3', 'org.example.AnotherClass'],
                        ['org.example.MyClass3', 'org.example.MyClass2']]
        sp_count << [1,
                     1,
                     2]
        sp << [['/NO_ROOT/prj/sub-prj/src/main/java'],
               ['/NO_ROOT/prj/sub-prj/src/main/java'],
               ['/NO_ROOT/prj/sub-prj/src/main/java', '/NO_ROOT/prj/src/main/java']]
        cn_count << [1,
                     1,
                     2]
        cn << [['org.example.MyClass3'],
               ['org.example.MyClass3'],
               ['org.example.MyClass3', 'org.example.MyClass2']]
    }

    static class InnerClass {}

    enum InnerEnum {}
}
