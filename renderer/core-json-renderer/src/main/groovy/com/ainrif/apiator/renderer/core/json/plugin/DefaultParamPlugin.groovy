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

package com.ainrif.apiator.renderer.core.json.plugin

import com.ainrif.apiator.api.annotation.Param
import com.ainrif.apiator.core.model.api.ApiEndpointParam
import com.ainrif.apiator.renderer.plugin.spi.param.ParamPlugin
import com.ainrif.apiator.renderer.plugin.spi.param.ParamViewData
import org.apache.commons.lang3.StringUtils

import javax.annotation.Nullable

class DefaultParamPlugin implements ParamPlugin {
    @Override
    ParamViewData process(ApiEndpointParam endpointParam) {
        def result = new ParamViewData()
        def annotation = endpointParam.annotations
                .find { Param.isAssignableFrom(it.annotationType()) }

        if (annotation) {
            def paramAnnotation = annotation as Param
            result.defaultValue = StringUtils.trimToNull(paramAnnotation.defaultValue())
            result.optional = paramAnnotation.optional()
        }

        annotation = endpointParam.annotations
                .find { Nullable.isAssignableFrom(it.annotationType()) }

        if (annotation) {
            result.optional = true
        }

        return result
    }
}
