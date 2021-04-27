package com.ainrif.apiator.renderer.postman.model_2_1.request

import com.ainrif.apiator.renderer.postman.model_2_1.UrlEncodedData
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * This field contains the data usually contained in the request body.
 */
class Body {

    /**
     * Postman stores the type of data associated with this request in this field.
     */
    public Mode mode
    public String raw
    public List<UrlEncodedData> urlencoded = []
    public List<FormData> formdata = []
    public FileData file

    /**
     * Additional configurations and options set for various body modes.
     */
    public Map<Mode, Map> options

    /**
     * When set to true, prevents request body from being sent.
     */
    public boolean disabled = false


    /**
     * Postman stores the type of data associated with this request in this field.
     */
    enum Mode {
        RAW("raw"),
        URLENCODED("urlencoded"),
        FORMDATA("formdata"),
        FILE("file"),

        @JsonValue
        final String value

        Mode(String value) {
            this.value = value
        }

        @JsonCreator
        static Mode fromValue(String value) {
            return valueOf(value.toUpperCase())
        }
    }

}
