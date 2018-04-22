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

package com.ainrif.apiator.renderer.mapper.jackson.test.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class M_JacksonProperties {
    public String plainPublicField;

    @JsonProperty("jackson_public_field")
    public String jacksonPublicField;

    @JsonProperty("jackson_private_field")
    private String jacksonPrivateField;

    @JsonProperty("jackson_getter_public_field")
    public String getJacksonPublicField() {
        return jacksonPublicField;
    }

    @JsonProperty("jackson_setter_public_field")
    public void setJacksonPublicField(String jacksonPublicField) {
        this.jacksonPublicField = jacksonPublicField;
    }

    @JsonProperty("jackson_getter_private_field")
    public String getJacksonPrivateField() {
        return jacksonPrivateField;
    }

    @JsonProperty("jackson_setter_private_field")
    public void setJacksonPrivateField(String jacksonPrivateField) {
        this.jacksonPrivateField = jacksonPrivateField;
    }

    @JsonProperty("jackson_asymmetric_property")
    public void setJacksonAsymmetricProperty(String jacksonPrivateField) {
        this.jacksonPrivateField = jacksonPrivateField;
    }
}
