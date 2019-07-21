package com.ainrif.apiator.test.model.jaxrs.smoke;

import javax.ws.rs.*;

public class BeanParamDto {
    @PathParam("p")
    String p;
    @QueryParam("q")
    String q;
    @HeaderParam("h")
    String h;
    @CookieParam("c")
    String c;
    @FormParam("f")
    String f;
}
