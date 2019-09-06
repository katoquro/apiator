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
package com.ainrif.apiator.core

import com.ainrif.apiator.api.annotation.Api
import com.ainrif.apiator.core.spi.Renderer
import com.ainrif.apiator.core.spi.WebServiceProvider

import java.lang.annotation.Annotation

class ApiatorConfig {
    /**
     * Classes which are not in this package (or subpackages) are ignored
     */
    String basePackage = ''

    WebServiceProvider provider = null
    Renderer renderer = null
    String basePath = '/api' // TODO katoquro: 27/05/2018 revise usage logic
    String apiVersion = '1.0.0-SNAPSHOT'

    /**
     * Marker annotation to detect classes to parse
     */
    Class<? extends Annotation> apiClass = Api

    DocletConfig docletConfig = new DocletConfig()
}
