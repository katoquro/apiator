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
package com.ainrif.apiator.renderer.core.json.plugin.modeltype

import com.ainrif.apiator.renderer.plugin.spi.ModelType
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.Date as SQL_Date
import java.sql.Timestamp

class OldDateModelTypePluginSpec extends Specification {
    @Unroll
    def "resolve"() {
        given:
        def resolver = new OldDateModelTypePlugin()

        expect:
        resolver.resolve(type) == expected

        where:
        type                 || expected
        CustomUnresolvedType || null
        Object               || null
        Date                 || ModelType.DATE
        Calendar             || ModelType.DATE
        Timestamp            || ModelType.DATE
        SQL_Date             || ModelType.DATE
    }
}
