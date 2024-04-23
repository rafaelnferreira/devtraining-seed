/*
   Tables are defined here, and can include fields defined in the project or its dependencies.

   New to Genesis? Take 5 minutes to learn the basics around tables >> https://learn.genesis.global/docs/database/fields-tables-views/tables/tables-basics/

   Be sure to learn about the advanced capabilities of tables when you have a moment >> https://learn.genesis.global/docs/database/fields-tables-views/tables/tables-advanced/

   If you're looking to do something new, there are a range of annotated examples to help >> https://learn.genesis.global/docs/database/fields-tables-views/tables/tables-examples/

   Else if you want to do something but are still not sure how it could be achieved? Detail your requirement on stack overflow, and let the community help >> https://stackoverflowteams.com/c/genesis-global/questions/ask?tags=table
   Tip: Use the tag "tables" and search other questions tagged "tables" in case someone has asked the same.
*/

tables {
  table(name = "INSTRUMENT", id = 11_000, audit = details(id = 11_006, sequence = "IA")) {
    autoIncrement(INSTRUMENT_ID)
    INSTRUMENT_ID
    SYMBOL

    primaryKey {
      INSTRUMENT_ID
    }

  }
  table(name = "COUNTERPARTY", id = 11_001, audit = details(id = 11_007, sequence = "CA")) {
   COUNTERPARTY_ID
   COUNTPERPARTY_NAME
    primaryKey {
      COUNTERPARTY_ID 
    }

  }
  table(name = "ORDER", id = 11_002, audit = details(id = 11_008, sequence = "OA")) {
    ORDER_ID
    ORDER_QUANTITY
    INSTRUMENT_ID not null
    COUNTERPARTY_ID
    ORDER_PRICE
    TICKET_SIZE

    primaryKey {
      ORDER_ID
    }

  }
  table(name = "ORDER_TOTALS", id = 11_003, audit = details(id = 11_009, sequence = "OT")) {
    TOTAL_QUANTITY_ORDERED
    INSTRUMENT_ID

    primaryKey {
      INSTRUMENT_ID
    }

  }
  table(name = "ORDER_STATUS", id = 11_004, audit = details(id = 11_010, sequence = "OS")) {
    ORDER_ID
    QUANTITY_FILLED
    AVG_PRICE

    primaryKey {
      ORDER_ID
    }

  }
  table(name = "TRADE", id = 11_005, audit = details(id = 11_011, sequence = "TA")) {
    TRADE_ID
    TRADE_QUANTITY
    ORDER_ID
    TRADE_PRICE

    primaryKey {
      TRADE_ID
    }

    indices {
      nonUnique { 
        ORDER_ID
      }
    }
  }

  // TODO 2. Add further tables you wish to add to the application here. See the comments at the top of this file for learning and guidance.
}
