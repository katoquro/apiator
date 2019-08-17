package com.ainrif.apiator.gradle.internal

import apiator.ApiatorGradleRunner

class ApiatorInternalRunner {
    static void main(String[] args) {
        String buildApiatorDir = args[0]
        String configurationClass = args[1]

        def runner = Class.forName(configurationClass).getConstructor().newInstance() as ApiatorGradleRunner

        runner.apiatorBuildDir = new File(buildApiatorDir)

        runner.execute()
    }
}
