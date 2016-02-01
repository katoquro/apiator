/*
 * Copyright 2014-2016 Ainrif <ainrif@outlook.com>
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

import javax.ws.rs.*;
import java.util.List;

@Path("/books")
public class BookController {
    @POST
    public Book createBook() {
        return null;
    }

    @GET
    public List<Book> getBooks(@QueryParam("search") String query) {
        return null;
    }

    @GET
    @Path("/{isbn}")
    public Book getBook(@PathParam("isbn") String isbn) {
        return null;
    }

    @PUT
    @Path("/{isbn}")
    public Book updateBook(@PathParam("isbn") String isbn, Book book) {
        return null;
    }

    @DELETE
    @Path("/{isbn}")
    public Book deleteBook(@PathParam("isbn") String isbn) {
        return null;
    }
}
