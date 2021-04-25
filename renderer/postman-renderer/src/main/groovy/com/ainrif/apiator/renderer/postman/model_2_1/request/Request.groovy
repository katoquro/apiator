package com.ainrif.apiator.renderer.postman.model_2_1.request


import com.ainrif.apiator.renderer.postman.model_2_1.common.Auth
import com.ainrif.apiator.renderer.postman.model_2_1.common.Description


/**
 * A request represents an HTTP request. If a string, the string is assumed to be the request URL and the method is assumed to be 'GET'.
 */
class Request {

    /**
     * If object, contains the complete broken-down URL for this request. If string, contains the literal request URL.
     */
    public Url url

    /**
     * Represents authentication helpers provided by Postman
     */
    public Auth auth

    /**
     * Using the Proxy, you can configure your custom proxy into the postman for particular url match
     */
    public ProxyConfig proxy

    /**
     * A representation of an ssl certificate
     */
    public Certificate certificate

    /**
     * Http Method
     */
    public Method method

    /**
     * A Description can be a raw text, or be an object, which holds the description along with its format.
     */
    public Description description

    /**
     * A representation for a list of headers
     */
    public List<Header> header = []

    /**
     * This field contains the data usually contained in the request body.
     */
    public Body body


    enum Method {
        GET,
        PUT,
        POST,
        PATCH,
        DELETE,
        COPY,
        HEAD,
        OPTIONS,
        LINK,
        UNLINK,
        PURGE,
        LOCK,
        UNLOCK,
        PROPFIND,
        VIEW;
    }
}
