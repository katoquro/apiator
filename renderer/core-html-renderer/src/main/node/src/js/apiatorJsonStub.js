/*
 * Copyright 2014-2019 Ainrif <support@ainrif.com>
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
window.apiatorJson = {
    apiContexts: [
        {
            apiEndpoints: [
                {
                    method: 'GET',
                    name: 'getAuthors',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'QUERY',
                            index: 0,
                            modelType: 'STRING',
                            name: 'search',
                            optional: false,
                        },
                    ],
                    path: '/',
                    returnTypes: [
                        {
                            basedOn: [
                                {
                                    basedOn: [],
                                    modelType: 'OBJECT',
                                    type:
                                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
                                },
                            ],
                            modelType: 'ARRAY',
                        },
                    ],
                },
                {
                    method: 'POST',
                    name: 'createAuthor',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'BODY',
                            index: 0,
                            modelType: 'OBJECT',
                            optional: false,
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
                        },
                    ],
                    path: '/',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
                        },
                    ],
                },
                {
                    method: 'DELETE',
                    name: 'deleteAuthor',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'LONG',
                            name: 'id',
                            optional: false,
                        },
                        {
                            basedOn: [],
                            defaultValue: 'false',
                            httpParamType: 'QUERY',
                            index: 1,
                            modelType: 'BOOLEAN',
                            name: 'with_books',
                            optional: false,
                        },
                    ],
                    path: '/{id}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
                        },
                    ],
                },
                {
                    method: 'GET',
                    name: 'getAuthor',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'LONG',
                            name: 'id',
                            optional: false,
                        },
                    ],
                    path: '/{id}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
                        },
                    ],
                },
                {
                    method: 'PUT',
                    name: 'updateAuthor',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'LONG',
                            name: 'id',
                            optional: false,
                        },
                        {
                            basedOn: [],
                            httpParamType: 'BODY',
                            index: 1,
                            modelType: 'OBJECT',
                            optional: false,
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
                        },
                    ],
                    path: '/{id}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
                        },
                    ],
                },
            ],
            apiPath: '/authors',
            name:
                'com.ainrif.apiator.test.model.jaxrs.uidev.controller.AuthorController',
        },
        {
            apiEndpoints: [
                {
                    method: 'GET',
                    name: 'getBooks',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'QUERY',
                            index: 0,
                            modelType: 'STRING',
                            name: 'search',
                            optional: false,
                        },
                    ],
                    path: '/',
                    returnTypes: [
                        {
                            basedOn: [
                                {
                                    basedOn: [],
                                    modelType: 'OBJECT',
                                    type:
                                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                                },
                            ],
                            modelType: 'ARRAY',
                        },
                    ],
                },
                {
                    method: 'POST',
                    name: 'createBook',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'BODY',
                            index: 0,
                            modelType: 'OBJECT',
                            optional: false,
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                        },
                    ],
                    path: '/',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                        },
                    ],
                },
                {
                    method: 'DELETE',
                    name: 'deleteBook',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'STRING',
                            name: 'isbn',
                            optional: false,
                        },
                    ],
                    path: '/{isbn}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                        },
                    ],
                },
                {
                    method: 'GET',
                    name: 'getBook',
                    params: [
                        {
                            basedOn: [],
                            defaultValue: '*',
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'STRING',
                            name: 'isbn',
                            optional: false,
                        },
                    ],
                    path: '/{isbn}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                        },
                    ],
                },
                {
                    method: 'PUT',
                    name: 'updateBook',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'STRING',
                            name: 'isbn',
                            optional: false,
                        },
                        {
                            basedOn: [],
                            httpParamType: 'BODY',
                            index: 1,
                            modelType: 'OBJECT',
                            optional: false,
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                        },
                    ],
                    path: '/{isbn}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                        },
                    ],
                },
            ],
            apiPath: '/books',
            name:
                'com.ainrif.apiator.test.model.jaxrs.uidev.controller.BookController',
        },
        {
            apiEndpoints: [
                {
                    method: 'ADMIN',
                    name: 'adminRequest',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'STRING',
                            name: 'token',
                            optional: false,
                        },
                    ],
                    path: '/admin/{token}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'VOID',
                        },
                    ],
                },
                {
                    method: 'GET',
                    name: 'findAuthors',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'QUERY',
                            index: 0,
                            modelType: 'STRING',
                            name: 'book_title',
                            optional: false,
                        },
                    ],
                    path: '/authors',
                    returnTypes: [
                        {
                            basedOn: [
                                {
                                    basedOn: [],
                                    modelType: 'OBJECT',
                                    type:
                                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
                                },
                            ],
                            modelType: 'ARRAY',
                        },
                    ],
                },
                {
                    method: 'GET',
                    name: 'findBooks',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'QUERY',
                            index: 0,
                            modelType: 'ENUMERATION',
                            name: 'status',
                            optional: true,
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book$Status',
                        },
                        {
                            basedOn: [],
                            httpParamType: 'QUERY',
                            index: 1,
                            modelType: 'STRING',
                            name: 'title',
                            optional: false,
                        },
                    ],
                    path: '/books',
                    returnTypes: [
                        {
                            basedOn: [
                                {
                                    basedOn: [],
                                    modelType: 'OBJECT',
                                    type:
                                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                                },
                            ],
                            modelType: 'ARRAY',
                        },
                    ],
                },
                {
                    description: 'searches over all items in the library',
                    method: 'GET',
                    name: 'findByIsbn',
                    params: [
                        {
                            basedOn: [],
                            description:
                                '- number, dashes and spaces will be skipped',
                            httpParamType: 'QUERY',
                            index: 0,
                            modelType: 'STRING',
                            name: 'isbn',
                            optional: false,
                        },
                    ],
                    path: '/issues',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
                        },
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                        },
                    ],
                },
                {
                    method: 'GET',
                    name: 'findMagazines',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'QUERY',
                            index: 0,
                            modelType: 'STRING',
                            name: 'title',
                            optional: false,
                        },
                    ],
                    path: '/magazines',
                    returnTypes: [
                        {
                            basedOn: [
                                {
                                    basedOn: [],
                                    modelType: 'OBJECT',
                                    type:
                                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                                },
                            ],
                            modelType: 'ARRAY',
                        },
                    ],
                },
                {
                    method: 'GET',
                    name: 'findPublishers',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'QUERY',
                            index: 0,
                            modelType: 'STRING',
                            name: 'title',
                            optional: false,
                        },
                    ],
                    path: '/publishers',
                    returnTypes: [
                        {
                            basedOn: [
                                {
                                    basedOn: [],
                                    modelType: 'OBJECT',
                                    type:
                                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Publisher',
                                },
                            ],
                            modelType: 'ARRAY',
                        },
                    ],
                },
            ],
            apiPath: '/library',
            name:
                'com.ainrif.apiator.test.model.jaxrs.uidev.controller.LibraryController',
        },
        {
            apiEndpoints: [
                {
                    method: 'GET',
                    name: 'getMagazines',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'QUERY',
                            index: 0,
                            modelType: 'STRING',
                            name: 'search',
                            optional: false,
                        },
                    ],
                    path: '/',
                    returnTypes: [
                        {
                            basedOn: [
                                {
                                    basedOn: [],
                                    modelType: 'OBJECT',
                                    type:
                                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                                },
                            ],
                            modelType: 'ARRAY',
                        },
                    ],
                },
                {
                    method: 'POST',
                    name: 'createMagazine',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'BODY',
                            index: 0,
                            modelType: 'OBJECT',
                            optional: false,
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                        },
                    ],
                    path: '/',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                        },
                    ],
                },
                {
                    method: 'DELETE',
                    name: 'deleteMagazine',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'STRING',
                            name: 'isbn',
                            optional: false,
                        },
                    ],
                    path: '/{isbn}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                        },
                    ],
                },
                {
                    method: 'GET',
                    name: 'getMagazine',
                    params: [
                        {
                            basedOn: [],
                            defaultValue: '*',
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'STRING',
                            name: 'isbn',
                            optional: false,
                        },
                    ],
                    path: '/{isbn}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                        },
                    ],
                },
                {
                    method: 'PUT',
                    name: 'updateMagazine',
                    params: [
                        {
                            basedOn: [],
                            httpParamType: 'PATH',
                            index: 0,
                            modelType: 'STRING',
                            name: 'isbn',
                            optional: false,
                        },
                        {
                            basedOn: [],
                            httpParamType: 'BODY',
                            index: 1,
                            modelType: 'OBJECT',
                            optional: false,
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                        },
                    ],
                    path: '/{isbn}',
                    returnTypes: [
                        {
                            basedOn: [],
                            modelType: 'OBJECT',
                            type:
                                'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
                        },
                    ],
                },
            ],
            apiPath: '/magazine',
            name:
                'com.ainrif.apiator.test.model.jaxrs.uidev.controller.MagazineController',
        },
    ],
    apiatorInfo: {
        provider: 'JaxRsProvider',
        renderer: 'CoreJsonRenderer',
        version: '0.0.0-dev_version',
    },
    clientApiInfo: {
        basePath: '/api',
        version: '1.0.0-SNAPSHOT',
    },
    usedApiTypes: [
        {
            fields: [
                {
                    basedOn: [],
                    modelType: 'LONG',
                    name: 'id',
                    optional: false,
                    readable: true,
                    writable: false,
                },
                {
                    basedOn: [
                        {
                            basedOn: [],
                            modelType: 'LONG',
                        },
                    ],
                    modelType: 'SET',
                    name: 'publishersUids',
                    optional: false,
                    readable: true,
                    writable: false,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'firstName',
                    optional: false,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'patronymicName',
                    optional: true,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'lastName',
                    optional: false,
                    readable: true,
                    writable: true,
                },
            ],
            modelType: 'OBJECT',
            type: 'com.ainrif.apiator.test.model.jaxrs.uidev.model.Author',
        },
        {
            description: 'Common type form library items',
            fields: [
                {
                    basedOn: [],
                    modelType: 'LONG',
                    name: 'authorId',
                    optional: false,
                    readable: true,
                    writable: false,
                },
                {
                    basedOn: [],
                    modelType: 'LONG',
                    name: 'publisherUid',
                    optional: false,
                    readable: true,
                    writable: false,
                },
                {
                    basedOn: [],
                    defaultValue: 'DRAFT',
                    modelType: 'ENUMERATION',
                    name: 'status',
                    optional: false,
                    readable: true,
                    type:
                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book$Status',
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'isbn',
                    optional: false,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'title',
                    optional: false,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'ENUMERATION',
                    name: 'language',
                    optional: false,
                    readable: true,
                    type:
                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Language',
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'secondTitle',
                    optional: true,
                    readable: true,
                    writable: true,
                },
            ],
            modelType: 'OBJECT',
            type: 'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book',
        },
        {
            description: 'Periodic publications use the same isbn',
            fields: [
                {
                    basedOn: [],
                    modelType: 'INTEGER',
                    name: 'year',
                    optional: false,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'INTEGER',
                    name: 'issueNumber',
                    optional: false,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'isbn',
                    optional: false,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'title',
                    optional: false,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'ENUMERATION',
                    name: 'language',
                    optional: false,
                    readable: true,
                    type:
                        'com.ainrif.apiator.test.model.jaxrs.uidev.model.Language',
                    writable: true,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'secondTitle',
                    optional: true,
                    readable: true,
                    writable: true,
                },
            ],
            modelType: 'OBJECT',
            type: 'com.ainrif.apiator.test.model.jaxrs.uidev.model.Magazine',
        },
        {
            fields: [
                {
                    basedOn: [],
                    description: 'Unique Id across the whole library',
                    modelType: 'LONG',
                    name: 'uid',
                    optional: false,
                    readable: true,
                    writable: false,
                },
                {
                    basedOn: [],
                    modelType: 'STRING',
                    name: 'title',
                    optional: false,
                    readable: true,
                    writable: true,
                },
                {
                    basedOn: [
                        {
                            basedOn: [],
                            modelType: 'STRING',
                        },
                    ],
                    description:
                        'List of ISBN numbers that belong to Publisher',
                    modelType: 'ARRAY',
                    name: 'isbns',
                    optional: false,
                    readable: true,
                    writable: true,
                },
            ],
            modelType: 'OBJECT',
            type: 'com.ainrif.apiator.test.model.jaxrs.uidev.model.Publisher',
        },
    ],
    usedEnumerations: [
        {
            description: 'Current stage of book lifecycle',
            modelType: 'ENUMERATION',
            type: 'com.ainrif.apiator.test.model.jaxrs.uidev.model.Book$Status',
            values: ['PUBLISHED', 'DRAFT', 'REPRINT'],
        },
        {
            modelType: 'ENUMERATION',
            type: 'com.ainrif.apiator.test.model.jaxrs.uidev.model.Language',
            values: [
                'EN',
                'ES',
                'ZH',
                'PT',
                'RU',
                'HI',
                'AR',
                'JA',
                'FR',
                'DE',
                'ID',
                'TR',
                'IT',
                'PL',
                'BE',
                'UK',
            ],
        },
    ],
};
