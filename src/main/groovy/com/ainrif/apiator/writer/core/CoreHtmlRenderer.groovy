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
package com.ainrif.apiator.writer.core

import com.ainrif.apiator.api.Renderer
import com.ainrif.apiator.core.model.api.ApiScheme
import groovy.text.GStringTemplateEngine
import org.webjars.WebJarAssetLocator

import javax.annotation.Nullable

class CoreHtmlRenderer implements Renderer {

    String js
    String css
    String hbs

    String template
    String toFile

    CoreHtmlRenderer() {
        def webJarsLocator = new WebJarAssetLocator()

        def jsPaths = []
        jsPaths << webJarsLocator.getFullPath('jquery', 'jquery.min.js')
        jsPaths << webJarsLocator.getFullPath('bootstrap', 'bootstrap.min.js')
        jsPaths << webJarsLocator.getFullPath('handlebars', 'handlebars.min.js')
        jsPaths << 'fuse.min.js'

        def cssPaths = []
        cssPaths << webJarsLocator.getFullPath('bootstrap', 'bootstrap.min.css')

        def hbsPath = []
        hbsPath << 'first'
        hbsPath << 'second'

        js = jsPaths
                .collect { this.class.classLoader.getResource(it).text }
                .join('\r\n') + 'jq = jQuery'

        css = cssPaths
                .collect { this.class.classLoader.getResource(it).text }
                .join('\r\n')

        hbs = hbsPath
                .collect { [name: it, content: this.class.classLoader.getResource("hbs/${it}.hbs").text] }
                .collect { "<script type='text/x-handlebars-template' id='${it.name}'>${it.content}</script>" }
                .join('\r\n')

        template = this.class.getResource('/core-html-renderer-template.hbs').text
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

    protected String renderTemplate(String json) {
        def html = new GStringTemplateEngine()
                .createTemplate(template)
                .make([json: json, js: js, css: css, hbs: hbs])
                .toString()

        if (toFile) {
            new File(toFile).write(html);
        }

        return html
    }
}
