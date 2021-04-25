package com.ainrif.apiator.renderer.postman.model_2_1.request


/**
 * Using the Proxy, you can configure your custom proxy into the postman for particular url match
 */
class ProxyConfig {

    /**
     * The Url match for which the proxy config is defined
     */
    public String match = "http+https://*/*"

    /**
     * The proxy server host
     */
    public String host

    /**
     * The proxy server port
     */
    public long port = 8080

    /**
     * The tunneling details for the proxy config
     */
    public boolean tunnel = false

    /**
     * When set to true, ignores this proxy configuration entity
     */
    public boolean disabled = false

}
