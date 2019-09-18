import { run as linkProcessorRun } from './misc/linkProcessor'
import app from '../app';
import embeddings from './misc/embeddings'

import { router } from "./router/Router.js";

embeddings();

app();

linkProcessorRun();
router.navigate(location.hash);
