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

package com.ainrif.apiator.gradle

class GradleUtils {
    /**
     * Give a closure hint of delegate
     * There are no type checks. Use it only to get IDE sourcecode highlight
     *
     * @param type to highlight delegate
     * @param c closure with delegate
     * @return closure passed to parameter
     */
    static <T> Closure<?> hint(@DelegatesTo.Target Class<T> type,
                               @DelegatesTo(strategy = Closure.DELEGATE_FIRST, genericTypeIndex = 0) final Closure<?> c) {
        return c
    }
}
