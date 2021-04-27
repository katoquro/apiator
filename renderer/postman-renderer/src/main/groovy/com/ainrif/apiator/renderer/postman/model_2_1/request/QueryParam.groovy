package com.ainrif.apiator.renderer.postman.model_2_1.request

import com.ainrif.apiator.renderer.postman.model_2_1.common.Description

class QueryParam {
    public String key
    public String value

    /**
     * If set to true, the current query parameter will not be sent with the request.
     */
    public boolean disabled = false

    public Description description
}
