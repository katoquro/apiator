package com.ainrif.apiator.test.model.jaxrs.smoke;

import com.ainrif.apiator.api.annotation.Api;
import com.ainrif.apiator.test.model.core.Dto00_MethodReturnDto;
import com.ainrif.apiator.test.model.core.Dto02_Complex;

import javax.ws.rs.Path;

@Api
@Path("/jax-rs-override")
public class JaxRsControllerOverride implements JaxRsController {

    @Path("/post-override")
    @Override
    public void post() {
    }

    @Path("/get-override")
    @Override
    public Object get() {
        return null;
    }

    @Path("/put-override")
    @Override
    public void put() {

    }

    @Path("/delete-override")
    @Override
    public void delete() {

    }

    @Path("/options-override")
    @Override
    public void options() {

    }

    @Path("/head-override")
    @Override
    public void head() {

    }

    @Path("/checkParams-override/{p}")
    @Override
    public Dto00_MethodReturnDto checkParams(String p, String q, String h, String c, String f, Dto02_Complex body) {
        return null;
    }
}
