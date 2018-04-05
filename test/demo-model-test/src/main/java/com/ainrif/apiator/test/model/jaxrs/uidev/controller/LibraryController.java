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
package com.ainrif.apiator.test.model.jaxrs.uidev.controller;

import com.ainrif.apiator.api.annotation.ConcreteTypes;
import com.ainrif.apiator.test.model.jaxrs.uidev.ADMIN;
import com.ainrif.apiator.test.model.jaxrs.uidev.model.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/library")
public class LibraryController {

    @GET
    @Path("/books")
    public List<Book> findBooks(@QueryParam("status") Book.Status status,
                                @QueryParam("title") String title) {
        return null;
    }

    /**
     * searches over all items in the library
     *
     * @param isbn - number, dashes and spaces will be skipped
     * @return found issues
     */
    @GET
    @Path("/issues")
    @ConcreteTypes({Book.class, Magazine.class})
    public List<Issue> findByIsbn(@QueryParam("isbn") String isbn) {
        return null;
    }

    @GET
    @Path("/magazines")
    public List<Magazine> findMagazines(@QueryParam("title") String title) {
        return null;
    }

    @GET
    @Path("/publishers")
    public List<Publisher> findPublishers(@QueryParam("title") String title) {
        return null;
    }

    @GET
    @Path("/authors")
    public List<Author> findAuthors(@QueryParam("book_title") String bookTitle) {
        return null;
    }

    @ADMIN
    @Path("/admin/{token}")
    public void adminRequest(@PathParam("token") String token) {
    }
}
