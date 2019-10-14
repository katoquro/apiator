package com.ainrif.apiator.test.model.micronaut.smoke;


import com.ainrif.apiator.test.model.core.Dto01_Plain;
import com.ainrif.apiator.test.model.core.Dto02_Complex;
import io.micronaut.http.annotation.*;

import javax.annotation.Nullable;

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
}
