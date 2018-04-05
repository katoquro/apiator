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

import com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine;

import javax.ws.rs.*;
import java.util.List;

@Path("/books")
public class MagazineController {
    @POST
    public Magazine createMagazine(Magazine magazine) {
        return null;
    }

    @GET
    public List<Magazine> getMagazines(@QueryParam("search") String query) {
        return null;
    }

    @GET
    @Path("/{isbn}")
    public Magazine getMagazine(@PathParam("isbn") String isbn) {
        return null;
    }

    @PUT
    @Path("/{isbn}")
    public Magazine updateMagazine(@PathParam("isbn") String isbn, Magazine magazine) {
        return null;
    }

    @DELETE
    @Path("/{isbn}")
    public Magazine deleteMagazine(@PathParam("isbn") String isbn) {
        return null;
    }
}
