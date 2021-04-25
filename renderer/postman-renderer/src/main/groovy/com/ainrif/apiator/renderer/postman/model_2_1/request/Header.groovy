package com.ainrif.apiator.renderer.postman.model_2_1.request

import com.ainrif.apiator.renderer.postman.model_2_1.common.Description


/**
 * Represents a single HTTP Header
 */
class Header {

    /**
     * This holds the LHS of the HTTP Header, e.g ``Content-Type`` or ``X-Custom-Header``
     * (Required)
     */
    public String key

    /**
     * The value (or the RHS) of the Header is stored in this field.
     * (Required)
     */
    public String value

    /**
     * If set to true, the current header will not be sent with requests.
     */
    public boolean disabled = false

    /**
     * A Description can be a raw text, or be an object, which holds the description along with its format.
     */
    public Description description


    /**
     * Default constructor for deserialization purposes
     */
    protected Header() {}

    Header(String key, String value) {
        super()
        this.key = key
        this.value = value
    }

}
