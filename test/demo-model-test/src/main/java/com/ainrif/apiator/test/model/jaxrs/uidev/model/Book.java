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

import com.ainrif.apiator.api.annotation.Param;

import static com.ainrif.apiator.test.model.jaxrs.uidev.model.Book.Status.DRAFT;

public class Book extends Issue {
    private Author author;
    private Publisher publisher;

    public @Param(defaultValue = "DRAFT")
    Status status = DRAFT;

    public Long getAuthorId() {
        return author.getId();
    }

    public Long getPublisherUid() {
        return publisher.getUid();
    }

    /**
     * Current stage of book lifecycle
     */
    public enum Status {
        PUBLISHED, DRAFT, REPRINT
    }
}
