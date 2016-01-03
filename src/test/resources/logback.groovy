statusListener(OnConsoleStatusListener)

context.name = "apiator"

appender("consoleTestAppender", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d %-5level [%thread] [%logger{36}] %msg %n%xEx"
    }
}

logger("com.ainrif.apiator", TRACE)

root(INFO, ["consoleTestAppender"])