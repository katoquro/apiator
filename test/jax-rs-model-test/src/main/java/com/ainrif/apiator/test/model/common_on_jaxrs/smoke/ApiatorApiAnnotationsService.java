package com.ainrif.apiator.test.model.common_on_jaxrs.smoke;

import com.ainrif.apiator.api.annotation.Api;
import com.ainrif.apiator.api.annotation.ConcreteTypes;
import com.ainrif.apiator.api.annotation.Param;
import com.ainrif.apiator.test.model.core.Dto00_MethodReturnDto;
import com.ainrif.apiator.test.model.core.Dto01_Plain;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Api
@Path("/apiator-api")
public interface ApiatorApiAnnotationsService {
    @ConcreteTypes({Dto00_MethodReturnDto.class, Dto01_Plain.class})
    @Path("/concrete-types-annotation")
    Object concreteTypesAnnotation();

    @Path("/param-annotation")
    void paramAnnotation(@Param(defaultValue = "default", optional = true) @QueryParam("q") String q);
}
