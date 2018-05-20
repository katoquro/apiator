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
package com.ainrif.apiator.renderer.core.json.view

import com.ainrif.apiator.core.model.api.ApiField
import com.ainrif.apiator.renderer.core.json.CoreJsonRenderer
import com.ainrif.apiator.renderer.core.json.javadoc.FieldMergedInfo

import javax.annotation.Nullable

class ApiFieldView extends ModelTypeBasedView.ApiTypeGenericView {
    String name
    String description
    boolean readable
    boolean writable
    String defaultValue
    boolean optional

    ApiFieldView(ApiField field, @Nullable FieldMergedInfo fieldInfo) {
        super(field.type)

        this.name = field.name
        this.description = fieldInfo?.description
        this.readable = field.readable
        this.writable = field.writable

        def pluginData = CoreJsonRenderer.pluginsConfig.propertyPlugin.process(field)

        this.defaultValue = pluginData.defaultValue
        this.optional = pluginData.optional
    }
}
