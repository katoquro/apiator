/*
 * Copyright 2014-2015 Ainrif <ainrif@outlook.com>
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
package com.ainrif.apiator.test.model.jaxrs;

import java.util.List;

public class DtoWithBounds<TEC extends DtoWithBounds.GenericWildcardBound> {
    public <TE extends GenericWildcardBound> List<TE> getWithExtendsBound() { return null; }

    public void setWithExtendsWildcardBound(List<? extends GenericWildcardBound> superBound) { }

    public void setWithExtendsBoundFromClass(List<TEC> superBound) { }

    public void setWithSuperWildcardBound(List<? super GenericWildcardBound> superBound) { }

    public static class GenericWildcardBound {
        public int field;
    }
}