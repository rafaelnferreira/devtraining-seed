/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide view config for multi-pro-code-test.
 *
 * Modification History
 */
views {

    view("TRADE_VIEW", TRADE) {

        joins {
            joining(INSTRUMENT, backwardsJoin = true) {
                on(TRADE.INSTRUMENT_ID to INSTRUMENT { INSTRUMENT_ID })
            }

            joining(COUNTERPARTY, backwardsJoin = true) {
                on(TRADE.COUNTERPARTY_ID to COUNTERPARTY { COUNTERPARTY_ID })
            }
        }

        fields {
            TRADE.allFields()

            COUNTERPARTY.COUNTERPARTY_NAME

            INSTRUMENT.INSTRUMENT_NAME
            INSTRUMENT.MARKET_ID withPrefix "SYMBOL"
            INSTRUMENT.CURRENCY_ID withAlias "CURRENCY"

            derivedField("CONSIDERATION", DOUBLE) {
                withInput(TRADE.QUANTITY, TRADE.PRICE) { qty, price -> qty * price }
            }

            derivedField("ASSET_CLASSX", STRING) {
                withInput(INSTRUMENT.ASSET_CLASS) { assetClass -> assetClass?.ifBlank { "UNKNOWN" } }
            }

        }

    }

}
