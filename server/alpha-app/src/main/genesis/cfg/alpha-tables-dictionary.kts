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

    table (name = "TRADE", id = 2000, audit = details(id=2100, sequence="TA")) {
        sequence(TRADE_ID, "TR")
        QUANTITY
        PRICE not null
        FX_RATE
        SYMBOL
        DIRECTION
        COUNTERPARTY_ID
        INSTRUMENT_ID not null
        TRADE_DATE
        ENTERED_BY
        TRADE_STATUS
        FX_RATE1

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

    table (name = "POSITION", id = 2003) {
        sequence(POSITION_ID, "PO")
        INSTRUMENT_ID not null
        QUANTITY
        NOTIONAL
        VALUE
        PNL

        primaryKey {
            POSITION_ID
        }

        indices {
            unique {
                INSTRUMENT_ID
            }
        }
    }

    table (name = "INSTRUMENT_PRICE", id = 2004) {
        INSTRUMENT_ID
        LAST_PRICE

        primaryKey {
            INSTRUMENT_ID
        }
    }

    table (name = "EXTERNAL_TRADE", id = 2005) {
        autoIncrement(TRADE_ID_INT)

        QUANTITY
        PRICE
        FX_RATE
        SYMBOL
        DIRECTION
        COUNTERPARTY_ID
        INSTRUMENT_ID
        TRADE_DATE
        ENTERED_BY
        TRADE_STATUS
        BROKER_FEE
        TRADER
        DESK
        BOOK
        SRC_SYSTEM
        INSTRUMENT_TYPE
        CALLABLE
        MC_CONTRACT_ID
        MC_RISK_CLASSIFICATION
        MC_EXPOSURE_TYPE
        MC_RATING
        
        primaryKey {
            TRADE_ID_INT
        }       
    }

    table( name = "LEGAL_ENTITY", id = 2006) {
        field("ID", INT).autoIncrement().primaryKey()
        field("NAME", STRING)
        field("COUNTRY_CODE", STRING(2))
    }

}