/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide dataserver config for multi-pro-code-test.
 *
 * Modification History
 */
dataServer {
    query ("ALL_TRADES", TRADE_VIEW)

    query ("ALL_PRICES", TRADE) {
        fields {
            PRICE
            SYMBOL
        }

        where { it.price > 0 }
    }

    query ( "USD_GBP", TRADE) {
        where { it.symbol == "USDGBP" }
    }


    query ("ALL_COUNTERPARTIES", COUNTERPARTY)

    query ("ALL_INSTRUMENTS", INSTRUMENT)

    query ("ALL_CPS_NAMES", COUNTERPARTY) {
        fields {
            COUNTERPARTY_NAME
        }
    }

    query ("ALL_CPS_NAMES_1", COUNTERPARTY) {
        fields {
            COUNTERPARTY_NAME
        }
    }

    query ("ALL_CPS_NAMES_2", COUNTERPARTY) {
        fields {
            COUNTERPARTY_NAME
        }
    }

}