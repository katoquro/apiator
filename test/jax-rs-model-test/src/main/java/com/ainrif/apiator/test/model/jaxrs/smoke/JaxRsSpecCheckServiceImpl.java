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

import com.ainrif.apiator.api.annotation.Api;
import com.ainrif.apiator.api.annotation.ConcreteTypes;
import com.ainrif.apiator.test.model.core.Dto01_Plain;
import com.ainrif.apiator.test.model.core.Dto02_Complex;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Api
@Path("/spec-check")
public class JaxRsSpecCheckServiceImpl implements JaxRsSpecCheckService {
    @Override
    public void supportCustomAnnotationsSubset() {
    }

    @Override
    public Object pathParam(String id) {
        return null;
    }

    @Override
    public Dto01_Plain getDtoFromImpl() {
        return null;
    }

    @Path("/path-from-impl")
    @Override
    public String pathFromImpl() {
        return null;
    }

    @Override
    @ConcreteTypes({Dto01_Plain.class, Dto02_Complex.class})
    public Object returnAbstractTypeWithConcreteTypesAnnotation() {
        return null;
    }

    @GET
    @Path("/impl-only-method")
    public List<List<Dto01_Plain>> implOnlyEndpoint() {
        return null;
    }

    @Path("/get-method-by-default")
    public String implicitHttpMethodDeclaration() {
        return null;
    }

    public void justPublicMethod() {
    }

    private void somePrivateLogic() {
    }
}
