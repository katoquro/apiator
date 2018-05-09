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

package com.ainrif.apiator.renderer.plugin.spi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ModelType {
    ANY,

    OBJECT, ENUMERATION,

    DICTIONARY, ARRAY, SET,

    VOID, BOOLEAN, BYTE, INTEGER, LONG, FLOAT, DOUBLE, STRING,

    DATE, BINARY,

    SYSTEM;

    public static ModelType[] customModelTypes = {OBJECT, ENUMERATION};

    public static ModelType[] primitiveTypes = {VOID, BOOLEAN, BYTE, INTEGER, LONG, FLOAT, DOUBLE, STRING};

    public static ModelType[] notPrimitiveTypes = calcNotPrimitiveTypes();

    private static ModelType[] calcNotPrimitiveTypes() {
        List<ModelType> values = new ArrayList<>(Arrays.asList(values()));
        values.removeAll(Arrays.asList(primitiveTypes));

        return values.toArray(new ModelType[0]);
    }
}
