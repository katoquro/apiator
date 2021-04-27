package com.ainrif.apiator.renderer.postman.model_2_1.request

import com.ainrif.apiator.renderer.postman.model_2_1.Variable


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

    /**
     * The port number present in this URL. An empty value implies 80/443 depending on whether the protocol field contains http/https.
     */
    public String port

    /**
     * An array of QueryParams, which is basically the query string part of the URL, parsed into separate variables
     */
    public List<QueryParam> query = []

    /**
     * Contains the URL fragment (if any). Usually this is not transmitted over the network, but it could be useful to store this in some cases.
     */
    public String hash

    /**
     * Postman supports path variables with the syntax `/path/:variableName/to/somewhere`. These variables are stored in this field.
     */
    public List<Variable> variable = []
}
