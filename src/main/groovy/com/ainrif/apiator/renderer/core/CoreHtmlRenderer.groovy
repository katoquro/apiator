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
package com.ainrif.apiator.renderer.core

import com.ainrif.apiator.api.Renderer
import com.ainrif.apiator.core.model.api.ApiScheme
import groovy.text.StreamingTemplateEngine
import org.webjars.WebJarAssetLocator

import javax.annotation.Nullable

class CoreHtmlRenderer implements Renderer {

    String js
    String css
    String jsLocal
    String cssLocal
    String hbs

    String toFile

    CoreHtmlRenderer() {
        def webJarsLocator = new WebJarAssetLocator()

        def jsPaths = []
        jsPaths << webJarsLocator.getFullPath('jquery', 'jquery.min.js')
        jsPaths << webJarsLocator.getFullPath('lodash', 'lodash.min.js')
        jsPaths << webJarsLocator.getFullPath('bootstrap', 'bootstrap.min.js')
        jsPaths << webJarsLocator.getFullPath('handlebars', 'handlebars.min.js')
        jsPaths << webJarsLocator.getFullPath('fuse.js', 'fuse.min.js')
        jsPaths << webJarsLocator.getFullPath('clipboard', 'clipboard.min.js')

        def cssPaths = []
        cssPaths << webJarsLocator.getFullPath('bootstrap', 'bootstrap.min.css')
        cssPaths << webJarsLocator.getFullPath('font-awesome', 'font-awesome.min.css')

        def jsLocalPaths = []
        jsLocalPaths << '/js/app.js'

        def cssLocalPaths = []
        cssLocalPaths << '/css/restyle.css'
        cssLocalPaths << '/css/feedback.css'

        def hbsPath = []
        hbsPath << 'app'
        hbsPath << 'nav'
        hbsPath << 'content'
        hbsPath << 'feedback'
        hbsPath << 'sidebar'
        hbsPath << 'main'
        hbsPath << 'fuzzy-response'

        js = concatExternalResources(jsPaths)
        css = concatExternalResources(cssPaths)

        jsLocal = concatLocalResources(jsLocalPaths)
        cssLocal = concatLocalResources(cssLocalPaths)

        hbs = hbsPath
                .collect { [name: it, content: this.class.classLoader.getResource("hbs/${it}.hbs").text] }
                .collect { "<script type='text/x-handlebars-template' id='${it.name}'>${it.content}</script>" }
                .join('\r\n')

        cssLocal = new StreamingTemplateEngine()
                .createTemplate(this.class.getResource('/fontsInlining.css').text)
                .make([fa_woff: encodeToBase64(webJarsLocator, 'font-awesome', 'fontawesome-webfont.woff')])
                .toString() + cssLocal
    }

    CoreHtmlRenderer(@Nullable toFile) {
        this()
        this.toFile = toFile
    }

    @Override
    String render(ApiScheme scheme) {
        def json = new CoreJsonRenderer().render(scheme)

        return renderTemplate(json)
    }

    protected String concatExternalResources(List<String> paths) {
        paths.collect { this.class.classLoader.getResource(it).text }
                .join('\r\n')
    }

    protected String concatLocalResources(List<String> paths) {
        paths.collect { this.class.getResource(it).text }
                .join('\r\n')
    }

    protected String encodeToBase64(WebJarAssetLocator locator, String webjar, String partialPath) {
        def assetPath = locator.getFullPath(webjar, partialPath)
        def resourceStream = this.class.classLoader.getResourceAsStream(assetPath)

        return Base64.encoder.encodeToString(resourceStream.bytes)
    }

    protected String renderTemplate(String json) {
        def html = new StreamingTemplateEngine()
                .createTemplate(this.class.getResource('/index.html').text)
                .make([json    : json,
                       js      : js,
                       css     : css,
                       jsLocal : jsLocal,
                       cssLocal: cssLocal,
                       hbs     : hbs,
                       favicon : Base64.encoder.encodeToString(this.class.getResource('/bw_favicon.png').bytes)])
                .toString()

        if (toFile) {
            new File(toFile).write(html);
        }

        return html
    }
}
