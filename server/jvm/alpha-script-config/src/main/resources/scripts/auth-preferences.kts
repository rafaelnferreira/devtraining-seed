security {
    authentication {
        ssoToken()
        genesisPassword {
            retry {
                maxAttempts = 3
                waitTimeMins = 5
            }
        }
    }

    heartbeatIntervalSecs = 30
    sessionTimeoutMins = 60
    expiryCheckMins = 5
    maxSimultaneousUserLogins = 0
}