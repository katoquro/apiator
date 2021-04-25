package com.ainrif.apiator.renderer.postman.model_2_1

import com.ainrif.apiator.renderer.postman.model_2_1.common.Description


/**
 * Detailed description of the info block
 */
class Info {

    /**
     * A collection's friendly name is defined by this field. You would want to set this field to a value that would allow you to easily identify this collection among a bunch of other collections, as such outlining its usage or content.
     * (Required)
     */
    public String name

    /**
     * A Description can be a raw text, or be an object, which holds the description along with its format.
     */
    public Description description

    /**
     * Postman allows you to version your collections as they grow, and this field holds the version number. While optional, it is recommended that you use this field to its fullest extent!
     */
    public String version

    /**
     * This should ideally hold a link to the Postman schema that is used to validate this collection. E.g: https://schema.getpostman.com/collection/v1
     * (Required)
     */
    public final String schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"

    /**
     * Default constructor for deserialization purposes
     */
    protected Info() {}

    Info(String name) {
        super()
        this.name = name
    }

}
