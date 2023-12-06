/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide fields config for multi-pro-code-test.
 *
 * Modification History
 */

fields {

    // trade
    field("TRADE_ID", type = STRING)
    field("QUANTITY", type = INT)
    field("PRICE", type = DOUBLE)
    field("FX_RATE", type = BIGDECIMAL(22,12))
    field("SYMBOL", type = STRING)
    field("DIRECTION", type = ENUM("BUY", "SELL", default = "BUY"))
    field("TRADE_DATE", type = DATETIME )
    field("ENTERED_BY", type = STRING)
    field("TRADE_STATUS", type=ENUM("NEW", "ALLOCATED", "CANCELLED", default = "NEW"))

    // counterparty
    field("COUNTERPARTY_ID", type = STRING)
    field("COUNTERPARTY_NAME", type = STRING)
    field("ENABLED", type = BOOLEAN)
    field("COUNTERPARTY_LEI", type = STRING)

    // instrument
    field("INSTRUMENT_ID", type = STRING)
    field("INSTRUMENT_NAME", type = STRING)
    field("MARKET_ID", type = STRING)
    field("COUNTRY_CODE", type = STRING)
    field("CURRENCY_ID", type = STRING)
    field("ASSET_CLASS", type = STRING)
    field("LAST_PRICE", type = DOUBLE)
    field("ISIN", type = STRING, maxSize = 20)

    // position
    field("POSITION_ID", type = STRING)
    field("NOTIONAL", type = DOUBLE)
    field("VALUE", type = DOUBLE)
    field("PNL", type = DOUBLE)

}
