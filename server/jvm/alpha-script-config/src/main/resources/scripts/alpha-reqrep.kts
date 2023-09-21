/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide request reply config for multi-pro-code-test.
 *
 * Modification History
 */
requestReplies {

    requestReply("TRADE", TRADE_VIEW) {
        permissioning {
            auth(mapName = "ENTITY_VISIBILITY") {
                TRADE_VIEW.COUNTERPARTY_ID
            }
        }
    }

}
