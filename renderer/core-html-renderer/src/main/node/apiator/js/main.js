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

import render from '/apiator/js/misc/hbsHelpers.js';
import * as search from '/apiator/js/header/search/search.js'
import * as sidebar from '/apiator/js/sidebar.js'
import * as card from '/apiator/js/card/card.js'
import * as linkProcessor from '/apiator/js/misc/linkProcessor.js'
import * as menu from '/apiator/js/header/menu.js'
import * as modal from '/apiator/js/misc/modal.js'
import embeddings from '/apiator/js/misc/embeddings'
import { router } from "./router/Router.js";

embeddings();

render(apiatorJson);
search.run();

sidebar.run();
card.run();
linkProcessor.run();
router.navigate(location.hash);
menu.run();
modal.run();
