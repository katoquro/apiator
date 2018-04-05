/*
 * Copyright 2014-2017 Ainrif <support@ainrif.com>
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
package com.ainrif.apiator.test.model.jaxrs.uidev.controller;

import com.ainrif.apiator.test.model.jaxrs.uidev.model.Author;

import javax.ws.rs.*;
import java.util.List;

@Path("/authors")
public class AuthorController {

    @POST
    public Author createAuthor(Author author) {
        return null;
    }

    @GET
    public List<Author> getAuthors(@QueryParam("search") String query) {
        return null;
    }

    @GET
    @Path("/{id}")
    public Author getAuthor(@PathParam("id") Long id) {
        return null;
    }

    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") Long id, Author author) {
        return null;
    }

    @DELETE
    @Path("/{id}")
    public Author deleteAuthor(@PathParam("id") Long id,
                               @QueryParam("with_books") @DefaultValue("false") boolean withBooks) {
        return null;
    }
}
