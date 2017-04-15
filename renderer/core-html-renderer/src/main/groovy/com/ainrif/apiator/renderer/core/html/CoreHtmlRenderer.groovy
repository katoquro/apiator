/*
 * Copyright 2014-2016 Ainrif <support@ainrif.com>
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
import groovy.text.StreamingTemplateEngine
import org.lesscss.LessCompiler
import org.lesscss.LessSource
import org.slf4j.LoggerFactory
import org.webjars.WebJarAssetLocator

import java.nio.file.*

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING
import static java.util.Collections.emptyMap

class CoreHtmlRenderer implements Renderer {
    private static def logger = LoggerFactory.getLogger(CoreHtmlRenderer)

    protected String js
    protected String css
    protected String hbs

    protected WebJarAssetLocator webJarsLocator

    @Delegate
    final Config config

    private static class Config extends CoreJsonRenderer.Config {
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

        return renderTemplate(json)
    }

    private void buildStatic() {
        webJarsLocator = new WebJarAssetLocator()

        // js
        List<Path> jsPaths = StaticDependencies.js.collect { lib, file ->
            resolveResourcePath(lib, file)
        }

        js = jsPaths.collect { it.text }
                .join('\r\n')

        js += Files.walk(resolveResourcePath('/apiator/js'))
                .filter { !Files.isDirectory(it) }
                .collect { it.text }
                .join('\r\n')

        List<Path> cssPaths = StaticDependencies.css.collect { lib, file ->
            resolveResourcePath(lib, file)
        }

        // css
        def tmpdir = Files.createTempDirectory('apiator')
        def lessDir = resolveResourcePath('/apiator/less')
        Files.walk(lessDir)
                .each {
            def targetPath = tmpdir.resolve(lessDir.relativize(it).toString())

            if (Files.isDirectory(it)) {
                Files.createDirectories(targetPath)
            } else {
                Files.copy(it.newInputStream(), targetPath, REPLACE_EXISTING)
            }
        }

        css = cssPaths.collect { it.text }
                .join('\r\n')

        css += new LessCompiler()
                .compile(new LessSource(tmpdir.resolve('main.less').toFile()))

        css += new StreamingTemplateEngine()
                .createTemplate(resolveResourcePath('/fontsInlining.css').text)
                .make([fa_woff: encodeToBase64(resolveResourcePath('font-awesome', 'fontawesome-webfont.woff'))])
                .toString()

        // hbs
        hbs = Files.walk(resolveResourcePath('/apiator/hbs'))
                .filter { !Files.isDirectory(it) }
                .collect { [name: (it.fileName.toString() - '.hbs'), content: it.text] }
                .collect { "<script type='text/x-handlebars-template' id='${it.name}'>${it.content}</script>" }
                .join('\r\n')
    }

    private Path resolveResourcePath(String library, String file) {
        def path = webJarsLocator.getFullPath(library, file)
        resolveResourcePath(path)
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

        resourcePath
    }

    private static FileSystem getFileSystem(URI uri) {
        def fs
        try {
            fs = FileSystems.getFileSystem(uri)
        } catch (FileSystemNotFoundException ignored) {
            fs = FileSystems.newFileSystem(uri, emptyMap())
        }

        fs
    }

    protected static String encodeToBase64(Path path) {
        return Base64.encoder.encodeToString(path.bytes)
    }

    protected String renderTemplate(String json) {
        buildStatic()
        def html = new StreamingTemplateEngine()
                .createTemplate(resolveResourcePath('/index.html').text)
                .make([json   : json,
                       js     : js,
                       css    : css,
                       hbs    : hbs,
                       favicon: encodeToBase64(resolveResourcePath('/bw_favicon.png'))])
                .toString()

        if (toFile) {
            new File(toFile).write(html)
        }

        return html
    }
}
