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
package com.ainrif.apiator.core.model

import com.ainrif.apiator.api.ModelTypeResolver
import com.ainrif.apiator.core.modeltype.PrimitivesModelTypeResolver
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import spock.lang.Specification

class ModelTypeRegisterSpec extends Specification {

    def "default init should include all core resolvers"() {
        given:
        def corePackage = PrimitivesModelTypeResolver.package.name
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .addUrls(ClasspathHelper.forPackage(corePackage))
                        .setScanners(new SubTypesScanner())
                        .filterInputsBy(new FilterBuilder().includePackage(corePackage))
        )

        def coreResolversCount = reflections.getSubTypesOf(ModelTypeResolver).size()

        expect:
        new ModelTypeRegister().modelTypeResolvers.size() == coreResolversCount
    }

    def "getTypeByClass"() {
        given:
        def register = new ModelTypeRegister()

        expect:
        register.getTypeByClass(Object) == ModelType.OBJECT
        register.getTypeByClass(EnumerationType) == ModelType.OBJECT
    }

    def "getTypeByClass; w/ custom resolver"() {
        given:
        def register = new ModelTypeRegister([{ EnumerationType.isAssignableFrom(it) ? ModelType.ENUMERATION : null }])

        expect:
        register.getTypeByClass(Object) == ModelType.OBJECT
        register.getTypeByClass(Object) == ModelType.OBJECT
        register.getTypeByClass(EnumerationType) == ModelType.ENUMERATION
    }

    static class EnumerationType {}
}
