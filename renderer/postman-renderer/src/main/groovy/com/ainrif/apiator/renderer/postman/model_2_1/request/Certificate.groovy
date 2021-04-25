package com.ainrif.apiator.renderer.postman.model_2_1.request


import java.nio.file.Path


/**
 * A representation of an ssl certificate
 */
class Certificate {

    /**
     * A name for the certificate for user reference
     */
    public String name

    /**
     * A list of Url match pattern strings, to identify Urls this certificate can be used for.
     */
    public List<String> matches = []

    /**
     * An object containing path to file containing private key, on the file system
     */
    public FilePath key

    /**
     * An object containing path to file certificate, on the file system
     */
    public FilePath cert

    /**
     * The passphrase for the certificate
     */
    public String passphrase

    static class FilePath {

        /**
         * The path to file on the file system
         */
        public Path src
    }
}
