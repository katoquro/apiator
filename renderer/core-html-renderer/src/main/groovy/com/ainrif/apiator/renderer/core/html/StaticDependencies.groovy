/*
 * Copyright 2014-2017 Ainrif <support@ainrif.com>
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

/**
 * Library to file maps for js and css.
 * Dependencies provided by <a href="http://www.webjars.org/">WebJars</a>.
 * To add a new dependency, do it in build.gradle
 *
 * key - library name
 * value - file from library bundle
 */
class StaticDependencies {
    static def js = [modulejs  : 'modulejs.min.js',
                     jquery    : 'jquery.min.js',
                     lodash    : 'lodash.min.js',
                     handlebars: 'handlebars.min.js',
                     clipboard : 'clipboard.min.js']

    static def css = ['font-awesome': 'font-awesome.min.css']
}
