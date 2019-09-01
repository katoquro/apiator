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
import render from './misc/hbsHelpers';
import { run as runSearch } from './header/search/search';
import { run as runCard } from './card/card';
import { run as linkProcessorRun } from './misc/linkProcessor'
import { run as modalRun } from './misc/modal';
import embeddings from './misc/embeddings'

import header from '../components/header/header';
import sidebar from '../components/sidebar/sidebar';

import { router } from "./router/Router.js";

embeddings();

render(apiatorJson);
header();
sidebar();
runSearch();

runCard();
linkProcessorRun();
router.navigate(location.hash);

modalRun();
