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
package com.ainrif.apiator.test.model.jaxrs.smoke;

import com.ainrif.apiator.api.annotation.Api;

import javax.ws.rs.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Api("description")
@Path("/entities") //todo test w/o class level @Path
public class ServiceImpl implements Service {

    @Path("/new")
    @Override
    public Object postDtoObject(Dto1 obj) {
        return null;
    }

    @Override
    public Dto1 getStringDtoInImpl(String id) {
        return null;
    }

    @PUT
    @Path("/{id}")
    @Override
    public Dto1 putStringDto(@PathParam("id") String id) {
        return null;
    }

    @Override
    public List<List<Dto2>> getAll() {
        return null;
    }

    @GET
    @Path("/flatten")
    public List<Dto3> getAllFlatten() {
        return null;
    }

    @Path("/type-value")
    public DtoTypeValue<String> getDtoTypeValue() {
        return null;
    }

    @Path("/wildcard-type")
    public DtoWithBounds getDtoWildcardType() {
        return null;
    }

    @Path("/void")
    public void doVoidMethod() {
    }

    @Path("/byte-array")
    public byte[] getByteArray(@BeanParam ParamsWrapper param) {
        return null;
    }

    @CustomPOST
    @Path("/enum")
    public void setStatus(@QueryParam("enum") @DefaultValue("THIRD") EnumApiType status) {
    }

    public void justPublicMethod() {
    }

    private void somePrivateLogic() {
    }

    @POST
    @Path("/binary")
    public OutputStream getStream(@FormParam("file") InputStream fileIS) {
        return null;
    }

    @GET
    @Path("/system")
    public Class<Class> getClassType() {
        return null;
    }
}
