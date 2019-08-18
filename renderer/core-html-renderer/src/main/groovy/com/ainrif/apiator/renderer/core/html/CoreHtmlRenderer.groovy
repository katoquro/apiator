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
package com.ainrif.apiator.renderer.core.html

import com.ainrif.apiator.core.model.api.ApiScheme
import com.ainrif.apiator.core.spi.Renderer
import com.ainrif.apiator.renderer.core.json.CoreJsonRenderer

import java.nio.file.*
import java.util.regex.Matcher
import java.util.regex.Pattern

import static java.util.Collections.emptyMap

class CoreHtmlRenderer implements Renderer {
    private static final String APIATOR_JSON_HTML_PLACEHOLDER = '<!--apiatorJson placeholder-->'

    @Delegate
    final Config config

    static class Config extends CoreJsonRenderer.Config {
        String toFile
    }

    CoreHtmlRenderer(@DelegatesTo(Config) Closure configurator) {
        this()
        this.config.with configurator
    }

    CoreHtmlRenderer(Config config) {
        this.config = config
    }

    CoreHtmlRenderer() {
        this.config = new Config()
    }

    @Override
    String render(ApiScheme scheme) {
        def json = new CoreJsonRenderer(config).render(scheme)

        return renderTemplate("var apiatorJson = ${json}")
    }

    protected String renderTemplate(String json) {
        def tmpl = resolveResourcePath('/apiator.min.html').text
        def html = Pattern.compile(APIATOR_JSON_HTML_PLACEHOLDER, Pattern.LITERAL)
                .matcher(tmpl)
                .replaceFirst(Matcher.quoteReplacement(json))

        if (toFile) {
            new File(toFile).write(html)
        }

        return html
    }

    private Path resolveResourcePath(String path) {
        def resource = getClass().getResource(path)

        if (!resource) {
            resource = getClass().classLoader.getResource(path)
        }

        URI uri = resource.toURI()
        Path resourcePath
        if (uri.scheme == 'jar') {
            FileSystem fileSystem = getFileSystem(uri)
            resourcePath = fileSystem.getPath(path)
        } else {
            resourcePath = Paths.get(uri)
        }

        return resourcePath
    }

    private static FileSystem getFileSystem(URI uri) {
        def fs
        try {
            fs = FileSystems.getFileSystem(uri)
        } catch (FileSystemNotFoundException ignored) {
            fs = FileSystems.newFileSystem(uri, emptyMap())
        }

        return fs
    }
}
