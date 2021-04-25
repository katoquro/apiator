package com.ainrif.apiator.renderer.postman.model_2_1

import com.ainrif.apiator.renderer.postman.model_2_1.common.Description

class UrlEncodedData {

    public String key
    public String value
    public boolean disabled = false

    /**
     * A Description can be a raw text, or be an object, which holds the description along with its format.
     */
    public Description description

    /**
     * Default constructor for deserialization purposes
     */
    protected UrlEncodedData() {}


    UrlEncodedData(String key) {
        super()
        this.key = key
    }

}
