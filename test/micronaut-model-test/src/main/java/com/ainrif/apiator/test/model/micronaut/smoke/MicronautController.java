package com.ainrif.apiator.test.model.micronaut.smoke;


import com.ainrif.apiator.test.model.core.Dto01_Plain;
import com.ainrif.apiator.test.model.core.Dto02_Complex;
import io.micronaut.http.annotation.*;

import javax.annotation.Nullable;
import java.security.Principal;

public interface MicronautController {
    @Post()
    Dto02_Complex postWithCookieParam(@CookieValue String cookie, @Body Dto02_Complex body);

    @Get(uri = "/path/{id}{?option}{&exploded*}")
    Dto02_Complex getWithParams(String id, @Nullable String option, @Nullable Dto01_Plain exploded);

    @Put("/put/{path}/{entity-id}")
    Dto02_Complex postWithCookieNamedParam(String path,
                                           @PathVariable("entity-id") String id,
                                           @Body Dto02_Complex body);

    @Delete("{id}")
    void deleteWithPathParamAndVoidReturn(String id);

    @Patch("/patch")
    Dto02_Complex patch(@Body Dto02_Complex body);

    @Get("/get/micronaut-system-type-wo-body")
    void methodWithMicronautSystemTypeWithoutBody(Principal user);

    @Post("/post/micronaut-system-type-w-body")
    void methodWithMicronautSystemTypeWithBody(Principal user, @Body Dto01_Plain body);

    @Post("/post/micronaut-system-type-w-exploded-body")
    void methodWithMicronautSystemTypeWithExplodedBody(Principal user, String explodedBody);
}
