import { searchValue, suggestedItems } from './stores';
import { getPageLinkToType, getPageLinkToEndpoint } from '../../../services/services';
import Searcher from './searcher';
import _ from "lodash";

let endpointDataSet = _
    .chain(apiatorJson.apiContexts)
    .flatMap(function (context) {
        let apiPath = context.apiPath;
        return _.map(context.apiEndpoints, function (endpoint) {
            return { apiPath: apiPath, endpoint: endpoint }
        });
    })
    .map(function (it) {
        return {
            index: {
                endpoint: it.apiPath + it.endpoint.path
            },
            payload: {
                showAs: 'endpoint',
                method: it.endpoint.method,
                apiPath: it.apiPath,
                path: it.endpoint.path
            }
        }
    })
    .value();

let modelDataSet = _
    .chain(apiatorJson.usedApiTypes)
    .map(function (it) {
        return {
            index: {
                model: it.type
            },
            payload: {
                showAs: 'model',
                type: it.type,
                simpleName: _.last(_.split(_.last(_.split(it.type, '.')), '$'))
            }
        }
    })
    .value();

let enumDataSet = _
    .chain(apiatorJson.usedEnumerations)
    .map(function (it) {
        let typeNames = _.split(_.last(_.split(it.type, '.')), '$');
        return {
            index: {
                enum: it.type,
                model: it.type
            },
            payload: {
                showAs: 'enum',
                type: it.type,
                simpleName: _.last(typeNames),
                enclosingType: 2 === typeNames.length ? typeNames[0] : null
            }
        }
    })
    .value();

let bangDataSet = _
    .chain([
        ['endpoint', 'Search trough url addresses'],
        ['model', 'Search trough model & enum names']])
    .map(function (it) {
        return {
            index: {
                bang: '!' + it[0]
            },
            payload: {
                showAs: 'bang',
                bang: '!' + it[0],
                name: it[0],
                description: it[1]
            }
        }
    })
    .value();

let searcher = new Searcher({})
    .addToDataSet(endpointDataSet)
    .addToDataSet(modelDataSet)
    .addToDataSet(enumDataSet)
    .addToDataSet(bangDataSet);

export function search(input) {
    let query = _.trimStart(input);

    let pattern = query;
    let indexType = 'endpoint';
    if (query.startsWith('!')) {
        let rawBang = query.match(/^!\w*/g);
        if (_.isEmpty(rawBang)) {
            return [];
        }
        let bang = rawBang[0];

        if (!/\s/.test(query)) {
            return searcher.search(bang, 'bang');
        } else {
            indexType = bang.substring(1);
            pattern = query.replace(bang, '')
        }
    }

    return searcher.search(pattern, indexType);
}

export function onChange(key, item) {
    searchValue.set('');
    suggestedItems.set([]);

    if (!item) {
        return
    }

    if ('bang' === item.showAs) {
        searchValue.set(item.bang + ' ');
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
                throw new Error('Not supported item type')
        }

        location.hash = navigateTo;
    }
}
