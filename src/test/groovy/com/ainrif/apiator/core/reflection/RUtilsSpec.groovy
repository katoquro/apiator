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
package com.ainrif.apiator.core.reflection

import com.ainrif.apiator.test.model.m01.*
import com.ainrif.apiator.test.model.m02.ChildClass
import com.ainrif.apiator.test.model.m02.M02_Dto
import spock.lang.Specification

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Predicate

import static com.ainrif.apiator.core.reflection.RUtils.*
import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

class RUtilsSpec extends Specification {
    def "getAllMethods"() {
        given:
        Class type = ChildClass
        Predicate<Method> predicate = { Modifier.isPublic(it.modifiers) }
        and:
        def methodFromInterface = new MethodSignature(ChildClass.getMethod("inDtoOutObj", M02_Dto))
        def publicMethodWOInheritance = new MethodSignature(ChildClass.getMethod("justPublicMethod"))

        when:
        def map = getAllMethods(type, predicate)

        then: "count of all methods matched to predicate (public)"
        expect map.keySet(), hasSize(5)
        and: "ordered from parent to child"
        expect map[methodFromInterface].first().toString(), containsString("abstract")
        expect map[methodFromInterface].last().toString(), not(containsString("abstract"))
        and: "was not inherited from interface"
        expect map[publicMethodWOInheritance], hasSize(1)
    }

    def "getAllSuperTypes"() {
        expect:
        that getAllSuperTypes(ChildType), contains(
                GstInterface2,
                GstInterface1,
                StI1_Interface,
                StInterface1,
                ChildTypeInterface,
                GrandSuperType,
                SuperType,
                ChildType)
    }

    def "getAllSuperClasses"() {
        expect:
        that getAllSuperClasses(ChildType), contains(ChildType, SuperType, GrandSuperType)
    }

    def "getAllSuperInterfaces"() {
        expect:
        that getAllSuperInterfaces(ChildType), contains(ChildTypeInterface, StInterface1, StI1_Interface, GstInterface1, GstInterface2)
    }
}
