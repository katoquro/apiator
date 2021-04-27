package com.ainrif.apiator.renderer.postman

import com.ainrif.apiator.core.model.api.ApiScheme
import com.ainrif.apiator.core.spi.Renderer
import com.ainrif.apiator.renderer.core.json.CoreJsonRenderer
import com.ainrif.apiator.renderer.core.json.view.ApiSchemeView
import com.ainrif.apiator.renderer.postman.model_2_1.Collection
import com.ainrif.apiator.renderer.postman.model_2_1.Info
import com.ainrif.apiator.renderer.postman.model_2_1.Item
import com.ainrif.apiator.renderer.postman.model_2_1.Variable
import com.ainrif.apiator.renderer.postman.model_2_1.common.Description
import com.ainrif.apiator.renderer.postman.model_2_1.request.Body
import com.ainrif.apiator.renderer.postman.model_2_1.request.Request
import com.ainrif.apiator.renderer.postman.model_2_1.request.Url
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import java.util.regex.Matcher
import java.util.regex.Pattern

class PostmanRenderer implements Renderer {
    protected final Pattern PATH_VAR_REGEX = ~/^\{(.*?)}$/

    @Delegate
    final Config config

    static class Config extends CoreJsonRenderer.Config {
        String toFile
    }

    PostmanRenderer(@DelegatesTo(value = Config, strategy = Closure.DELEGATE_FIRST) Closure configurator) {
        this.config = new Config()
        this.config.tap configurator
    }

    PostmanRenderer(Config config) {
        this.config = config
    }

    @Override
    String render(ApiScheme scheme) {
        def apiScheme = new CoreJsonRenderer(config).with { configuredCorePlugins ->
            new ApiSchemeView(scheme, scheme.docletIndex)
        }

        return renderPostmanCollection(apiScheme)
    }

    protected String renderPostmanCollection(ApiSchemeView api) {
        def info = new Info(api.clientApiInfo.getOrDefault('apiName', 'No `apiName` provided at `clientApiInfo`')).tap {
            version = api.clientApiInfo.getOrDefault('apiVersion', 'No `apiVersion` provided at `clientApiInfo`')
        }

        def collection = new Collection(info, renderItems(api))

        collection.variable << new Variable(type: Variable.Type.STRING, key: 'api_protocol', value: 'https')
        collection.variable << new Variable(type: Variable.Type.STRING, key: 'api_host')

        def mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
        mapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

        mapper.serializationInclusion = JsonInclude.Include.NON_NULL

        return mapper.writeValueAsString(collection)
    }

    protected List<Item> renderItems(ApiSchemeView api) {
        def baseApiPath = api.clientApiInfo['basePath'] ?: ''

        return api.apiContexts.collectMany { ctx ->
            return ctx.apiEndpoints.collect { ep ->
                def request = new Request().tap {
                    url = new Url(protocol: '{{api_protocol}}', host: '{{api_host}}')
                    def path = (baseApiPath + '/' + ctx.apiPath + '/' + ep.path)
                            .replace('//', '/')
                            .split(/\//)
                            .findAll() as List<String>

                    List<Variable> pathParams = []
                    path = path.collect {
                        Matcher matcher = PATH_VAR_REGEX.matcher(it)
                        if (matcher.matches()) {
                            String varName = matcher.group(1)
                            pathParams << new Variable(key: varName)
                            return ':' + varName
                        } else {
                            return it
                        }
                    }

                    url.path = path
                    url.variable = pathParams

                    method = Request.Method.valueOf(ep.method)
                    description = new Description(ep.description)
                    body = new Body(mode: Body.Mode.RAW, raw: '{"key":"no implemented yet"}')
                }

                return new Item(request).tap {
                    name = ctx.name.split(/\./).last() + '.' + ep.name
                }
            }
        }
    }
}
