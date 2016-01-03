/*
 * Copyright 2014-2016 Ainrif <ainrif@outlook.com>
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
package com.ainrif.apiator.core.model.api;

import java.util.Collection;
import java.util.List;

public class ModelWithGenericBounds<TYPE_CLASS extends ModelWithGenericBounds.BoundsDto> {
    public <TYPE extends BoundsDto2> List<TYPE> getWithExtendsBound() { return null; }

    public void setWithExtendsWildcardBound(List<? extends BoundsDto> superBound) { }

    public void setWithExtendsBoundFromClass(List<TYPE_CLASS> superBound) { }

    public void setWithSuperWildcardBound(List<? super BoundsDto> superBound) { }

    public static class BoundsDto {}

    public static class BoundsDto2 {}

    public static class WildcardDto {
        Iterable<?> fieldWithWildcard;
        Iterable<? extends Collection<String>> fieldWithExtendsWildcardBound;
        Iterable<? super Collection<String>> fieldWithExtendsSuperBound;
    }
}
