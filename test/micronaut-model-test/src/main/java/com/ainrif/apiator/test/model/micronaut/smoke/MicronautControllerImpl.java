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

package com.ainrif.apiator.test.model.micronaut.smoke;

import com.ainrif.apiator.test.model.core.Dto01_Plain;
import com.ainrif.apiator.test.model.core.Dto02_Complex;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import javax.annotation.Nullable;
import java.security.Principal;

@Controller("/controller")
public class MicronautControllerImpl implements MicronautController {
    @Override
    public Dto02_Complex postWithCookieParam(String cookie, Dto02_Complex body) {
        return null;
    }

    @Override
    public Dto02_Complex getWithParams(String id,
                                       @Nullable String option,
                                       @Nullable Dto01_Plain exploded) {
        return null;
    }

    @Override
    public Dto02_Complex postWithCookieNamedParam(String path, String id, Dto02_Complex body) {
        return null;
    }

    @Override
    public void deleteWithPathParamAndVoidReturn(String id) {
    }

    @Override
    public Dto02_Complex patch(Dto02_Complex body) {
        return null;
    }

    @Post("/micronaut/expanded-body")
    public void expandedBodyParam(@Body("name") String name) {
    }

    @Override
    public void methodWithMicronautSystemTypeWithoutBody(Principal user) {
    }

    @Override
    public void methodWithMicronautSystemTypeWithBody(Principal user, Dto01_Plain body) {
    }

    @Override
    public void methodWithMicronautSystemTypeWithExplodedBody(Principal user, String explodedBody) {
    }
}
