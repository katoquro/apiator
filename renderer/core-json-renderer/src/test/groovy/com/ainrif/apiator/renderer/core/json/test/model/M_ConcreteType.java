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
package com.ainrif.apiator.renderer.core.json.test.model;

import java.util.List;

public class M_ConcreteType {

    int fieldPackagePrivate;
    protected int fieldProtected;
    private int fieldPrivate;
    public int fieldPublic;

    public List<String> getPublicBeanPropGetSet() {
        return null;
    }

    public void setPublicBeanPropGetSet(List<String> value) {
    }


    public List<String> getPublicBeanPropOnlyGet() {
        return null;
    }

    public void setPublicBeanPropOnlySet(List<String> value) {
    }


    List<String> getNotPublicBeanProp1() {
        return null;
    }

    protected List<String> getNotPublicBeanProp2() {
        return null;
    }

    private List<String> getNotPublicBeanProp3() {
        return null;
    }
}