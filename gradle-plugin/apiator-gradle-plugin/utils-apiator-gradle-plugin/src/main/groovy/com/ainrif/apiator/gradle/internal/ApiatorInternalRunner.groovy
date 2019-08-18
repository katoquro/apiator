package com.ainrif.apiator.gradle.internal

import apiator.ApiatorGradleRunner

import static com.ainrif.apiator.gradle.GradleUtils.assertArg

class ApiatorInternalRunner {
    static void main(String[] args) {
        String buildApiatorDir = args[0]
        String configurationClass = args[1]

        def runnerClass = Class.forName(configurationClass)
        assertArg(ApiatorGradleRunner.isAssignableFrom(runnerClass),
                "Runner class (${configurationClass}) must extend ${ApiatorGradleRunner.canonicalName} " +
                        "and have default constructor")

        def runner = runnerClass.getConstructor().newInstance() as ApiatorGradleRunner

        runner.apiatorBuildDir = new File(buildApiatorDir)

        runner.execute()
    }
}
