package com.ainrif.apiator.renderer.postman.model_2_1.request


/**
 * If object, contains the complete broken-down URL for this request. If string, contains the literal request URL.
 */
class Url {

    /**
     * The string representation of the request URL, including the protocol, host, path, hash, query parameter(s) and path variable(s).
     */
    public String raw

    /**
     * The protocol associated with the request, E.g: 'http'
     */
    public String protocol

    /**
     * The host for the URL, E.g: api.yourdomain.com.
     */
    public String host

    /**
     * The complete path of the current url, broken down into segments. A segment could be a string, or a path variable.
     */
    public List<String> path = []

}
