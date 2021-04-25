package com.ainrif.apiator.renderer.postman.model_2_1

import com.ainrif.apiator.renderer.postman.model_2_1.common.Description
import com.ainrif.apiator.renderer.postman.model_2_1.request.Request


/**
 * Items are entities which contain an actual HTTP request, and sample responses attached to it.
 */
class Item {

    /**
     * A unique ID that is used to identify collections internally
     */
    public String id

    /**
     * A human readable identifier for the current item.
     */
    public String name

    /**
     * A Description can be a raw text, or be an object, which holds the description along with its format.
     */
    public Description description

    /**
     * Collection variables allow you to define a set of variables, that are a *part of the collection*, as opposed to environments, which are separate entities.
     * *Note: Collection variables must not contain any sensitive information.*
     */
    public List<Variable> variable = []

    /**
     * Postman allows you to configure scripts to run when specific events occur. These scripts are stored here, and can be referenced in the collection by their ID.
     */
    public List<Event> event = []

    /**
     * A request represents an HTTP request. If a string, the string is assumed to be the request URL and the method is assumed to be 'GET'.
     * (Required)
     */
    public Request request

    /**
     * Default constructor for deserialization purposes
     */
    protected Item() {}

    Item(Request request) {
        super()
        this.request = request
    }

}
