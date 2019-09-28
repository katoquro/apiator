package com.ainrif.apiator.renderer.core.json.plugin

import spock.lang.Specification

class DefaultPathPluginSpec extends Specification {
    def "path should be normalized by default"() {
        expect:
        DefaultPathPlugin.normalizePath(path) == expected

        where:
        path          || expected
        '/a/../c'     || '/c'
        'a/./b'       || '/a/b'
        '/a///b'      || '/a/b'
        '/../..'      || '/'
        'a/.././b//c' || '/b/c'
    }
}
