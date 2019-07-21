package com.ainrif.apiator.test.model.jaxrs.smoke;

import com.ainrif.apiator.api.annotation.Api;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Api
@Path("jax-rs-specific")
public interface JaxRsSpecific {

    @CustomHttpVerb
    @Path("/custom-http-verb")
    void customHttpVerb();

    @Path("/default-value")
    void defaultValue(@QueryParam("q") @DefaultValue("default") String paramWithDefault);

    @Path("/bean-param/{p}")
    void beanParam(@BeanParam BeanParamDto beanParam);

    @Path("/implicit-verb-get")
    String implicitVerbGet();
}
