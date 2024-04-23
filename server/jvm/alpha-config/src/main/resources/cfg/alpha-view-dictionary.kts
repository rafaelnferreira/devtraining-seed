/*
   Views are defined here, and can include tables fields defined in the project or its dependencies.

   New to Genesis? Take 5 minutes to learn the basics around views >> https://learn.genesis.global/docs/database/fields-tables-views/views/views-basics/

   Be sure to learn about the advanced capabilities of views when you have a moment >> https://learn.genesis.global/docs/database/fields-tables-views/views/views-advanced/

   If you're looking to do something new, there are a range of annotated examples to help >> https://learn.genesis.global/docs/database/fields-tables-views/views/views-examples/

   Else if you want to do something but are still not sure how it could be achieved? Detail your requirement on stack overflow, and let the community help >> https://stackoverflowteams.com/c/genesis-global/questions/ask?tags=view
   Tip: Use the tag "views" and search other questions tagged "views" in case someone has asked the same.
*/

views {
  view("ORDER_VIEW", ORDER) {
    joins {
      joining(ORDER_STATUS, backwardsJoin = true) {
        on(ORDER { ORDER_ID } to ORDER_STATUS { ORDER_ID })
      }
      joining(INSTRUMENT, backwardsJoin = true) {
        on(ORDER { INSTRUMENT_ID } to INSTRUMENT { INSTRUMENT_ID })
      }
      joining(COUNTERPARTY, backwardsJoin = true) {
        on(ORDER { COUNTERPARTY_ID } to COUNTERPARTY { COUNTERPARTY_ID })
      }
    }
    fields {
      INSTRUMENT.SYMBOL
      COUNTERPARTY.COUNTPERPARTY_NAME
      ORDER.ORDER_QUANTITY
      ORDER.ORDER_PRICE
      ORDER.ORDER_ID
      ORDER.INSTRUMENT_ID
      ORDER.COUNTERPARTY_ID
      ORDER.TICKET_SIZE
      ORDER_STATUS.QUANTITY_FILLED
      ORDER_STATUS.AVG_PRICE
    }
  }
  view("ORDER_TOTAL_VIEW", ORDER_TOTALS) {
    joins {
      joining(INSTRUMENT, backwardsJoin = true) {
        on(ORDER_TOTALS { INSTRUMENT_ID } to INSTRUMENT { INSTRUMENT_ID })
      }
    }
    fields {
      INSTRUMENT.SYMBOL
      ORDER_TOTALS.TOTAL_QUANTITY_ORDERED
      ORDER_TOTALS.INSTRUMENT_ID
    }
  }
  view("TRADE_VIEW", TRADE) {
    joins {
      joining(ORDER, backwardsJoin = true) {
        on(TRADE { ORDER_ID } to ORDER { ORDER_ID })
        .joining(INSTRUMENT, backwardsJoin = true) {
          on(ORDER { INSTRUMENT_ID } to INSTRUMENT { INSTRUMENT_ID })
        }
        .joining(COUNTERPARTY, backwardsJoin = true) {
          on(ORDER { COUNTERPARTY_ID } to COUNTERPARTY { COUNTERPARTY_ID })
        }
      }
    }
    fields {
      INSTRUMENT.SYMBOL
      COUNTERPARTY.COUNTPERPARTY_NAME
      ORDER.ORDER_QUANTITY
      ORDER.ORDER_PRICE
      ORDER.INSTRUMENT_ID
      ORDER.COUNTERPARTY_ID
      TRADE.TRADE_QUANTITY
      TRADE.TRADE_PRICE
      TRADE.TRADE_ID
      TRADE.ORDER_ID
    }
  }

  //TODO 3. Add any other views you wish to add to the application here. See the comments at the top of this file for learning and guidance.
}
