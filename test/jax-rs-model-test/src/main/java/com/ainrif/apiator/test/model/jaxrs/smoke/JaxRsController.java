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

package com.ainrif.apiator.test.model.jaxrs.smoke;

import com.ainrif.apiator.test.model.core.Dto00_MethodReturnDto;
import com.ainrif.apiator.test.model.core.Dto02_Complex;

import javax.ws.rs.*;

@Path("/jax-rs")
public interface JaxRsController {

    @POST
    @Path("/post")
    void post();

    @GET
    @Path("/get")
    Object get();

    @PUT
    @Path("/put")
    void put();

    @DELETE
    @Path("/delete")
    void delete();

    @OPTIONS
    @Path("/options")
    void options();

    @HEAD
    @Path("/head")
    void head();

    @POST
    @Path("/checkParams/{p}")
    Dto00_MethodReturnDto checkParams(@PathParam("p") String p,
                                      @QueryParam("q") String q,
                                      @HeaderParam("h") String h,
                                      @CookieParam("c") String c,
                                      @FormParam("f") String f,
                                      Dto02_Complex body);
}
