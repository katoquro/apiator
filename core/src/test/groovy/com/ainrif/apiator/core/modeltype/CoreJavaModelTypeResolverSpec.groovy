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
package com.ainrif.apiator.core.modeltype

import com.ainrif.apiator.core.model.ModelType
import spock.lang.Specification
import spock.lang.Unroll

class CoreJavaModelTypeResolverSpec extends Specification {
    @Unroll
    def "resolve"() {
        given:
        def resolver = new CoreJavaModelTypeResolver()

        expect:
        resolver.resolve(type) == expected

        where:
        type                 || expected
        CustomUnresolvedType || null
        Object               || null
        void                 || ModelType.VOID
        Void                 || ModelType.VOID
        int                  || ModelType.INTEGER
        Integer              || ModelType.INTEGER
        long                 || ModelType.LONG
        Long                 || ModelType.LONG
        float                || ModelType.FLOAT
        Float                || ModelType.FLOAT
        double               || ModelType.DOUBLE
        Double               || ModelType.DOUBLE
        char                 || ModelType.STRING
        Character            || ModelType.STRING
        byte                 || ModelType.BYTE
        Byte                 || ModelType.BYTE
        short                || ModelType.INTEGER
        Short                || ModelType.INTEGER
        boolean              || ModelType.BOOLEAN
        Boolean              || ModelType.BOOLEAN
        String               || ModelType.STRING
        CharSequence         || ModelType.STRING
        URL                  || ModelType.STRING
        UUID                 || ModelType.STRING
        BigInteger           || ModelType.LONG
        BigDecimal           || ModelType.DOUBLE
        EnumModel1           || ModelType.ENUMERATION
        EnumModel2           || ModelType.ENUMERATION
    }

    enum EnumModel1 {}

    class EnumModel2 extends Enum<EnumModel2> {
        protected EnumModel2(String name, int ordinal) {
            super(name, ordinal)
        }
    }
}
