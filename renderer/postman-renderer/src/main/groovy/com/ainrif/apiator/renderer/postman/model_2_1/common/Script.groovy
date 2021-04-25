package com.ainrif.apiator.renderer.postman.model_2_1.common

import com.ainrif.apiator.renderer.postman.model_2_1.request.Url


/**
 * A script is a snippet of Javascript code that can be used to to perform setup or teardown operations on a particular response.
 */
class Script {

    /**
     * A unique, user defined identifier that can  be used to refer to this script from requests.
     */
    public String id

    /**
     * Type of the script. E.g: 'text/javascript'
     */
    public String type

    /**
     * This is an array of strings, where each line represents a single line of code. Having lines separate makes it possible to easily track changes made to scripts.
     */
    public List<String> exec = []

    /**
     * If object, contains the complete broken-down URL for this request. If string, contains the literal request URL.
     */
    public Url src

    /**
     * Script name
     */
    public String name

}
