package com.ainrif.apiator.renderer.postman.model_2_1.common

import com.ainrif.apiator.renderer.postman.model_2_1.AuthAttribute


/**
 * Represents authentication helpers provided by Postman
 */
class Auth {

    /**
     * (Required)
     */
    public Type type

    public Object noauth

    /**
     * API Key Authentication
     * <p>
     * The attributes for API Key Authentication.
     */
    public List<AuthAttribute> apikey = []

    /**
     * AWS Signature v4
     * <p>
     * The attributes for [AWS Auth](http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html).
     */
    public List<AuthAttribute> awsv4 = []

    /**
     * Basic Authentication
     * <p>
     * The attributes for [Basic Authentication](https://en.wikipedia.org/wiki/Basic_access_authentication).
     */
    public List<AuthAttribute> basic = []

    /**
     * Bearer Token Authentication
     * <p>
     * The helper attributes for [Bearer Token Authentication](https://tools.ietf.org/html/rfc6750)
     */
    public List<AuthAttribute> bearer = []

    /**
     * Digest Authentication
     * <p>
     * The attributes for [Digest Authentication](https://en.wikipedia.org/wiki/Digest_access_authentication).
     */
    public List<AuthAttribute> digest = []

    /**
     * EdgeGrid Authentication
     * <p>
     * The attributes for [Akamai EdgeGrid Authentication](https://developer.akamai.com/legacy/introduction/Client_Auth.html).
     */
    public List<AuthAttribute> edgegrid = []

    /**
     * Hawk Authentication
     * <p>
     * The attributes for [Hawk Authentication](https://github.com/hueniverse/hawk)
     */
    public List<AuthAttribute> hawk = []

    /**
     * NTLM Authentication
     * <p>
     * The attributes for [NTLM Authentication](https://msdn.microsoft.com/en-us/library/cc237488.aspx)
     */
    public List<AuthAttribute> ntlm = []

    /**
     * OAuth1
     * <p>
     * The attributes for [OAuth2](https://oauth.net/1/)
     */
    public List<AuthAttribute> oauth1 = []

    /**
     * OAuth2
     * <p>
     * Helper attributes for [OAuth2](https://oauth.net/2/)
     */
    public List<AuthAttribute> oauth2 = []


    /**
     * Default constructor for deserialization purposes
     */
    protected Auth() {}


    Auth(Type type) {
        super()
        this.type = type
    }

    enum Type {

        APIKEY("apikey"),
        AWSV4("awsv4"),
        BASIC("basic"),
        BEARER("bearer"),
        DIGEST("digest"),
        EDGEGRID("edgegrid"),
        HAWK("hawk"),
        NOAUTH("noauth"),
        OAUTH1("oauth1"),
        OAUTH2("oauth2"),
        NTLM("ntlm");

        final String value

        Type(String value) {
            this.value = value
        }

        @Override
        String toString() {
            return this.value
        }

        static Type fromValue(String value) {
            return valueOf(value.toUpperCase())
        }

    }

}
