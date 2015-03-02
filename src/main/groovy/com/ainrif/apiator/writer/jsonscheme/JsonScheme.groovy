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
package com.ainrif.apiator.writer.jsonscheme

import com.ainrif.apiator.core.model.api.ApiEndpointMethod

/**
 * temporarily suspended
 */
class JsonScheme {

    String version
    String basePath
    List<Api> apis = []
    Map<String, ApiModel> definitions = [:]

    static class Api {
        String title
        String apiPath
        Map<String, Object> properties = [:]
        List<ApiEndpoint> links = []
    }

    static class ApiEndpoint {
        String title
        String rel
        String href
        ApiEndpointMethod method
        ApiModel schema
        ApiModel targetSchema
    }

    static class ApiModel {
        String type
        Map<String, ApiProperty> properties = [:]
        List<String> required = []
    }

    static class ApiProperty {
        String type
        String $ref
    }

    static final class Type {
        static final String OBJECT = 'object'
        static final String DICTIONARY = 'dictionary'
        static final String ARRAY = 'array'
        static final String STRING = 'string'
        static final String BOOLEAN = 'boolean'
        static final String INTEGER = 'integer'
        static final String LONG = 'long'
        static final String DATE = 'date'
    }

}
