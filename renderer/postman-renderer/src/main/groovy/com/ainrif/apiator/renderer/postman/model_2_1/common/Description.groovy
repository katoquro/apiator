package com.ainrif.apiator.renderer.postman.model_2_1.common


/**
 * A Description can be a raw text, or be an object, which holds the description along with its format.
 */
class Description {

    /**
     * The content of the description goes here, as a raw string.
     */
    public String content

    /**
     * Holds the mime type of the raw description content. E.g: 'text/markdown' or 'text/html'.
     * The type is used to correctly render the description when generating documentation, or in the Postman app.
     */
    public String type

    /**
     * Description can have versions associated with it, which should be put in this property.
     */
    public String version

    protected Description() {}

    Description(String content) {
        this.content = content
    }
}
