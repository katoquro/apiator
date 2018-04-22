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

package com.ainrif.apiator.renderer.core.json.mapper

import com.ainrif.apiator.core.model.api.ApiField
import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.core.reflection.RUtils
import com.ainrif.apiator.core.spi.PropertyMapper

import java.beans.Introspector
import java.lang.reflect.Modifier
import java.util.function.Predicate

import static java.util.Collections.singletonMap
import static java.util.Objects.nonNull

/**
 * Tries to resolve properties according to next rules
 *
 * <ol>
 *     <li>java bean properties</li>
 *     <li>public fields</li>
 * </ol>
 *
 * @return found properties
 */
class DefaultPropertyMapper implements PropertyMapper {

    @Override
    Collection<ApiField> mapProperties(Class<?> type) {
        def beanInfo = type.interface ?
                Introspector.getBeanInfo(type) :
                Introspector.getBeanInfo(type, Object)

        Map<String, ApiField> props = beanInfo.propertyDescriptors
                .collectEntries(
                {
                    def name = it.name
                    def field = new ApiField(
                            name: name,
                            type: nonNull(it.readMethod)
                                    ? new ApiType(it.readMethod.genericReturnType)
                                    : new ApiType(it.writeMethod.genericParameterTypes[0]),
                            readable: nonNull(it.readMethod),
                            writable: nonNull(it.writeMethod))

                    return singletonMap(name, field)
                })

        props += RUtils.getAllFields(type, { Modifier.isPublic(it.modifiers) } as Predicate)
                .collectEntries(
                {
                    def name = it.name

                    def field = new ApiField(
                            name: it.name,
                            type: new ApiType(it.genericType),
                            readable: true,
                            writable: true)
                    return singletonMap(name, field)
                })

        return props.values()
    }
}
