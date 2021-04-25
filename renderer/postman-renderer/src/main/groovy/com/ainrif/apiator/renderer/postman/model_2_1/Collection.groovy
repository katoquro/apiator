package com.ainrif.apiator.renderer.postman.model_2_1

import com.ainrif.apiator.renderer.postman.model_2_1.common.Auth


class Collection {

    /**
     * Detailed description of the info block
     * (Required)
     */
    public Info info

    /**
     * Items are the basic unit for a Postman collection. You can think of them as corresponding to a single API endpoint. Each Item has one request and may have multiple API responses associated with it.
     * (Required)
     */
    public List<Item> item = []

    /**
     * Postman allows you to configure scripts to run when specific events occur. These scripts are stored here, and can be referenced in the collection by their ID.
     */
    public List<Event> event = []

    /**
     * Collection variables allow you to define a set of variables, that are a *part of the collection*, as opposed to environments, which are separate entities.
     * *Note: Collection variables must not contain any sensitive information.*
     */
    public List<Variable> variable = []

    /**
     * Represents authentication helpers provided by Postman
     */
    public Auth auth

    /**
     * Default constructor for deserialization purposes
     */
    protected Collection() {}

    Collection(Info info, List<Item> item) {
        super()
        this.info = info
        this.item = item
    }

}
