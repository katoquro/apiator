package com.ainrif.apiator.test.model.jaxrs.smoke;

import com.ainrif.apiator.api.annotation.Api;
import com.ainrif.apiator.test.model.core.Dto00_MethodReturnDto;
import com.ainrif.apiator.test.model.core.Dto02_Complex;

import javax.ws.rs.Path;

@Api
public class JaxRsControllerInheritance implements JaxRsController {
    @Override
    public void post() {
    }

    @Override
    public Object get() {
        return null;
    }

    @Override
    public void put() {
    }

    @Override
    public void delete() {
    }

    @Override
    public void options() {
    }

    @Override
    public void head() {
    }

    @Override
    public Dto00_MethodReturnDto checkParams(String p, String q, String h, String c, String f, Dto02_Complex body) {
        return null;
    }

    @Path("/from-child-class")
    public void fromChildClass() {
    }
}
