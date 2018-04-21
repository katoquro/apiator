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
import com.ainrif.apiator.test.model.core.Dto07_JavaDocField;

import javax.ws.rs.Path;

/**
 * Class-level javadoc from impl
 */
@Api
@Path("/inheritance")
public class JavaDocInheritanceCheckServiceImpl implements JavaDocInheritanceCheckService {
    @Override
    public void checkJavaDocInheritance(String id) {}

    /**
     * override javadoc from interface
     */
    @Override
    public void checkJavaDocOverride() {}

    @Override public void checkJavaDocOnField(Dto07_JavaDocField dto) {}
}
