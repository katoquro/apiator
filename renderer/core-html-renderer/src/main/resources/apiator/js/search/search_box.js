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

modulejs.define('search_box', [], function () {

    const KEY_CODES = Object.freeze({
        UP: 38,
        DOWN: 40,
        ENTER: 13
    });

    const ACTIVE_CLASS = 'search__suggest-item_active';

    /**
     * @param {?int} key - code of pressed key, otherwise null
     * @param {Object} item - data associated with this selection
     * @constructor
     */
    function ChangeEvent(key, item) {
        this.key = key;
        this.item = item;
    }

    $.fn.search_box = function (options) {
        var settings = $.extend({
            searchFunc: function (value) {
                throw new Error('Need search function for value ' + value)
            },
            renderSuggestFunc: function (item) {
                return item;
            },
            /**
             * Activates when one of suggested items was selected
             * @param {ChangeEvent} changeEvent - event related data
             * @param {Object} box - methods on element
             */
            onChangeFunc: function (changeEvent, box) {
                box.clearSuggest();
            }
        }, options);

        var box = {};
        var input = this;
        var activeIndex = 0;
        var suggest = $('.search__suggest').get()[0] || $('<ul class="search__suggest"></ul>');

        suggest
            .on('mouseover', 'li', function () {
                var newActiveIndex = $(this).index();
                changeCursorPosition(newActiveIndex)
            })
            .on('mouseout', 'li', function () {
                $(this).removeClass(ACTIVE_CLASS);
            })
            .on('click', 'li', function () {
                var item = $(this).data('item');
                settings.onChangeFunc(new ChangeEvent(null, item), box);
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
                case KEY_CODES.ENTER: {
                    var item = suggest.children().eq(activeIndex).data('item');
                    settings.onChangeFunc(new ChangeEvent(KEY_CODES.ENTER, item), box);

                    break;
                }
                case KEY_CODES.UP:
                case KEY_CODES.DOWN: {
                    var newActiveIndex = KEY_CODES.UP === event.keyCode ? activeIndex - 1 : activeIndex + 1;
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
            if (-1 < [KEY_CODES.ENTER, KEY_CODES.UP, KEY_CODES.DOWN].indexOf(event.keyCode)) {
                return;
            }

            searchOnInput();
        });

        function searchOnInput() {
            box.clearSuggest();

            var pattern = input.val();
            if (!pattern.length) {
                return;
            }

            $.each(settings.searchFunc(pattern), function (index, item) {
                $(settings.renderSuggestFunc(item))
                    .data('item', item)
                    .appendTo(suggest);
            });

            suggest
                .insertAfter(input)
                .children().first().addClass(ACTIVE_CLASS);
        }

        this.click(function () {
            if (suggest.is(':empty')) {
                searchOnInput()
            }
        });

        box.clearSuggest = function () {
            activeIndex = 0;
            suggest.children().remove();
        };
        box.getInput = function () {
            return input;
        };

        return box;
    };
});