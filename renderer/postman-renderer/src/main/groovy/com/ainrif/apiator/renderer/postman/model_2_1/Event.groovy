package com.ainrif.apiator.renderer.postman.model_2_1

import com.ainrif.apiator.renderer.postman.model_2_1.common.Script


/**
 * Defines a script associated with an associated event name
 */
class Event {

    /**
     * A unique identifier for the enclosing event.
     */
    public String id

    /**
     * Can be set to `test` or `prerequest` for test scripts or pre-request scripts respectively.
     * (Required)
     */
    public String listen

    /**
     * A script is a snippet of Javascript code that can be used to to perform setup or teardown operations on a particular response.
     */
    public Script script

    /**
     * Indicates whether the event is disabled. If absent, the event is assumed to be enabled.
     */
    public boolean disabled = false


    /**
     * Default constructor for deserialization purposes
     */
    protected Event() {}

    Event(String listen) {
        this.listen = listen
    }

}
