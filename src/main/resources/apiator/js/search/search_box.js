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

modulejs.define('search_box', [], function () {

    var UP_KEY_CODE = 38;
    var DOWN_KEY_CODE = 40;
    var ENTER_KEY_CODE = 13;

    var ACTIVE_CLASS = 'suggest_active';

    $.fn.search_box = function (options) {
        var settings = $.extend({
            searchFunc: function (value) {
                throw new Error('Need search function for value ' + value)
            },
            renderSuggestFunc: function (item) {
                return item;
            },
            onChangeFunc: function (value, box, input) {
                input.val(value);
                box.clearSuggest();
            }
        }, options);

        var box = {};
        var input = this;
        var activeIndex = 0;
        var suggest = $('#search_box-div').get()[0] || $('<div id="search_box-div"></div>');

        suggest
            .on('mouseover', 'li', function () {
                var newActiveIndex = $(this).index();
                changeCursorPosition(newActiveIndex)
            })
            .on('mouseout', 'li', function () {
                $(this).removeClass(ACTIVE_CLASS);
            })
            .on('click', 'li', function () {
                var value = $(this).text();
                settings.onChangeFunc(value, box, input);
            });

        function changeCursorPosition(position) {
            if (-1 < position && position < suggest.children().length) {
                suggest.children()
                    .eq(activeIndex).removeClass(ACTIVE_CLASS)
                    .end()
                    .eq(position).addClass(ACTIVE_CLASS);

                activeIndex = position;
            }
        }

        this.keydown(function (event) {
            switch (event.keyCode) {
                case ENTER_KEY_CODE: {
                    var value = suggest.children().eq(activeIndex).text();
                    settings.onChangeFunc(value, box, input);

                    break;
                }
                case UP_KEY_CODE:
                case DOWN_KEY_CODE: {
                    var newActiveIndex = UP_KEY_CODE == event.keyCode ? activeIndex - 1 : activeIndex + 1;
                    changeCursorPosition(newActiveIndex);

                    break;
                }
                default:
                    return;
            }

            event.preventDefault();
            event.stopPropagation();

            return false;
        });

        this.keyup(function (event) {
            if (-1 < [ENTER_KEY_CODE, UP_KEY_CODE, DOWN_KEY_CODE].indexOf(event.keyCode)) {
                return;
            }

            box.clearSuggest();

            var pattern = input.val();
            if (!pattern.length) {
                return;
            }

            $.each(settings.searchFunc(pattern), function (index, item) {
                suggest.append(settings.renderSuggestFunc(item));
            });

            suggest
                .insertAfter(input)
                .children().first().addClass(ACTIVE_CLASS);
        });

        box.clearSuggest = function () {
            activeIndex = 0;
            suggest.children().remove();
        };

        return box;
    };
});