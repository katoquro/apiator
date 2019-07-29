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
package com.ainrif.apiator.test.model.jaxrs.uidev.model;

import java.util.List;

public class Publisher {
    /**
     * Unique Id across the whole library
     */
    protected Long uid;

    public String title;

    /**
     * List of ISBN numbers that belong to Publisher
     */
    public List<String> isbns;

    public Long getUid() {
        return uid;
    }
}
