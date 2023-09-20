/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide table definition config for multi-pro-code-test.
 *
 * Modification History
 */

tables {

    table (name = "TRADE", id = 2000) {
        sequence(TRADE_ID, "TR")
        QUANTITY
        PRICE not null
        SYMBOL
        DIRECTION
        COUNTERPARTY_ID
        INSTRUMENT_ID

        primaryKey {
            TRADE_ID
        }
    }

    table (name = "COUNTERPARTY", id = 2002) {
        sequence(COUNTERPARTY_ID, "CP")
        COUNTERPARTY_NAME
        ENABLED
        COUNTERPARTY_LEI

        primaryKey {
            COUNTERPARTY_ID
        }
    }

    table (name = "INSTRUMENT", id = 2001) {
        sequence(INSTRUMENT_ID, "IN")
        INSTRUMENT_NAME
        MARKET_ID
        COUNTRY_CODE
        CURRENCY_ID
        ASSET_CLASS

        primaryKey {
            INSTRUMENT_ID
        }
    }

}