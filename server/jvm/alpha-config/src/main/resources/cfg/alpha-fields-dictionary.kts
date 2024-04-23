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
    field("INSTRUMENT_ID", INT)
    field("SYMBOL", STRING)

    field("COUNTERPARTY_ID", STRING)
    field("COUNTPERPARTY_NAME", STRING)

    field("ORDER_ID", STRING)
    field("ORDER_QUANTITY", INT)
    field("COUNTERPARTY_ID", STRING)
    field("ORDER_PRICE", DOUBLE)
    field("TICKET_SIZE", INT)

    field("TOTAL_QUANTITY_ORDERED", INT)
    field("QUANTITY_FILLED", INT)
    field("AVG_PRICE", DOUBLE)

    field("TRADE_ID", STRING)
    field("TRADE_QUANTITY", INT)
    field("ORDER_ID", STRING)
    field("TRADE_PRICE", DOUBLE)

}
