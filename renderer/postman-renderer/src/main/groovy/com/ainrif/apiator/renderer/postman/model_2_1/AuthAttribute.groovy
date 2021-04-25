package com.ainrif.apiator.renderer.postman.model_2_1


/**
 * Represents an attribute for any authorization method provided by Postman. For example `username` and `password` are set as auth attributes for Basic Authentication method.
 */
class AuthAttribute {

    public String key
    public Object value
    public String type

    /**
     * Default constructor for deserialization purposes
     */
    protected AuthAttribute() {}

    AuthAttribute(String key) {
        this.key = key
    }

}
