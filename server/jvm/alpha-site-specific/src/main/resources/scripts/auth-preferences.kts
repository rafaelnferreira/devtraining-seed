import java.lang.System

security {

    heartbeatIntervalSecs = 30
    sessionTimeoutMins = 480
    expiryCheckMins = 5
    maxSimultaneousUserLogins = 100

    authentication {
        genesisPassword {
            selfServiceReset {
                timeoutInMinutes = 180
                notifyTopic = "CIA reset password"
                redirectUrl = "https://${System.getenv("HOSTNAME")}/login/reset-password"
                resetMessage {
                    body = """
                        Dear {{USER.firstName}} {{USER.lastName}},<br></br>
                        We received a request to reset the password for the account that's associated with this email address.<br></br>
                        If you made this request, please securely reset your password using the link below. This link will expire 15 minutes from when the password reset was requested.<br></br><a href="{{RESET_URL}}">{{RESET_URL}}</a><br></br>
                        Kind Regards,<br></br>
                        Support
                        """.trimIndent()
                }
            }
            validation {
                passwordSalt = ""
                passwordStrength {
                    minimumLength = 6
                    maximumLength = 256
                    minDigits = 1
                    maxRepeatCharacters = null
                    minUppercaseCharacters = 1
                    minLowercaseCharacters = 1
                    minNonAlphaNumericCharacters = 1
                    restrictWhiteSpace = true
                    restrictAlphaSequences = false
                    restrictQWERTY = false
                    restrictNumericalSequences = false
                    illegalCharacters = ""
                    historicalCheck = null
                    dictionaryWordSize = null
                    restrictDictionarySubstring = false
                    restrictPassword = false
                    restrictUserName = false
                    repeatCharacterRestrictSize = null
                    passwordExpiryDays = 365
                    passwordExpiryNotificationDays = 7
                }
            }
            retry {
                maxAttempts = 10
                waitTimeMins = 1
            }
        }

    }
}
