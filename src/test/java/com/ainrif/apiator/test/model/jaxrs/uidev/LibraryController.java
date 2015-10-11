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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/library")
public class LibraryController {

    @GET
    @Path("/books/search")
    public List<Book> searchBooks(@QueryParam("status") Book.Status status,
                                  @QueryParam("title") String title) {
        return null;
    }


    @GET
    @Path("/authors/search")
    public List<Author> searchAuthors(@QueryParam("book_title") String bookTitle) {
        return null;
    }

    @GET
    @Path("/publishers")
    public List<Publisher> searchByAuthors() {
        return null;
    }

    @GET
    @Path("/publishers/{uid}")
    public Publisher searchByAuthors(@PathParam("uid") Long uid) {
        return null;
    }
}
