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
package com.ainrif.apiator.test.model.jaxrs.uidev;

public class Book {
    private Author author;
    private Publisher publisher;

    public String isbn;
    public String title;
    public Status status;

    public Long getAuthorId() {
        return author.getId();
    }

    public Long getPublisherUid() {
        return publisher.getUid();
    }

    public enum Status {
        PUBLISHED, DRAFT, REPRINT
    }
}
