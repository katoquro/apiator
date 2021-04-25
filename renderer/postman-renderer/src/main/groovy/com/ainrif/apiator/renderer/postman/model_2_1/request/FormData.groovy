package com.ainrif.apiator.renderer.postman.model_2_1.request

import com.ainrif.apiator.renderer.postman.model_2_1.common.Description


class FormData {

    public String key
    public String value

    /**
     * When set to true, prevents this form data entity from being sent.
     */
    public boolean disabled = false
    public String type = 'text'

    /**
     * Override Content-Type header of this form data entity.
     */
    public String contentType

    /**
     * A Description can be a raw text, or be an object, which holds the description along with its format.
     */
    public Description description

    /**
     * Default constructor for deserialization purposes
     */
    protected FormData() {}

    FormData(String key) {
        this.key = key
    }
}
