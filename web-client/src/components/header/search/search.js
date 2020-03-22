import { searchValue, suggestedItems } from './stores';
import {
    getPageLinkToEndpoint,
    getPageLinkToType,
} from '../../../services/services';
import Searcher from './searcher';

import {
    apiContexts,
    usedApiTypes,
    usedEnumerations,
} from '../../../js/apiator-data';

const endpointDataSet = apiContexts
    .flatMap(context =>
        context.apiEndpoints.map(endpoint => ({
            apiPath: context.apiPath,
            endpoint,
        }))
    )
    .map(it => ({
        index: {
            endpoint: it.apiPath + it.endpoint.path,
        },
        payload: {
            showAs: 'endpoint',
            method: it.endpoint.method,
            apiPath: it.apiPath,
            path: it.endpoint.path,
        },
    }));

const modelDataSet = usedApiTypes.map(it => {
    return {
        index: {
            model: it.type,
        },
        payload: {
            showAs: 'model',
            type: it.type,
            simpleName: it.type.split('.').pop(),
        },
    };
});

const enumDataSet = usedEnumerations.map(it => {
    const { type } = it;
    const typeNames = type.split('.');

    return {
        index: {
            enum: type,
            model: type,
        },
        payload: {
            showAs: 'enum',
            type,
            simpleName: typeNames.pop(),
            enclosingType: typeNames.length === 2 ? typeNames.pop() : null,
        },
    };
});

const bangDataSet = [
    ['endpoint', 'Search trough url addresses'],
    ['model', 'Search trough model & enum names'],
].map(it => ({
    index: {
        bang: `!${it[0]}`,
    },
    payload: {
        showAs: 'bang',
        bang: `!${it[0]}`,
        name: it[0],
        description: it[1],
    },
}));

const searcher = new Searcher({})
    .addToDataSet(endpointDataSet)
    .addToDataSet(modelDataSet)
    .addToDataSet(enumDataSet)
    .addToDataSet(bangDataSet);

export function search(input) {
    const query = input;

    let pattern = query;

    let indexType = 'endpoint';

    if (query.startsWith('!')) {
        const rawBang = query.match(/^!\w*/g);

        if (rawBang.length === 0) {
            return [];
        }

        const bang = rawBang[0];

        if (!/\s/.test(query)) {
            return searcher.search(bang, 'bang');
        } else {
            indexType = bang.substring(1);
            pattern = query.replace(bang, '');
        }
    }

    return searcher.search(pattern, indexType);
}

export function onChange(key, item) {
    searchValue.set('');
    suggestedItems.set([]);

    if (!item) {
        return;
    }

    if (item.showAs === 'bang') {
        searchValue.set(`${item.bang} `);
    } else if (key) {
        let navigateTo;

        switch (item.showAs) {
            case 'endpoint':
                navigateTo = getPageLinkToEndpoint(item);
                break;
            case 'model':
                navigateTo = getPageLinkToType(item.type);
                break;
            case 'enum':
                navigateTo = getPageLinkToType(item.type);
                break;
            default:
                throw new Error('Not supported item type');
        }

        location.hash = navigateTo;
    }
}
