/*
   Dataserver queries defined here become available for clients to connect to and stream real-time data.

   New to Genesis? Take 5 minutes to learn the basics around dataserver >> https://learn.genesis.global/docs/server/data-server/introduction/

   Review the basics section to further your understanding >> https://learn.genesis.global/docs/server/data-server/basics/

   Be sure to learn about the advanced capabilities of dataserver when you have a moment >> https://learn.genesis.global/docs/server/data-server/advanced/

   If you're looking to do something new, there are a range of annotated examples to help >> https://learn.genesis.global/docs/server/data-server/examples/

   Else if you want to do something but are still not sure how it could be achieved? Detail your requirement on stack overflow, and let the community help >> https://stackoverflowteams.com/c/genesis-global/questions/ask?tags=dataserver
   Tip: Use the tag "dataserver" and search other questions tagged "dataserver" in case someone has asked the same.
*/

dataServer {
  query("ALL_ORDERS", ORDER_VIEW) {
    fields {
      SYMBOL
      COUNTPERPARTY_NAME
      ORDER_QUANTITY
      ORDER_PRICE
      ORDER_ID
      INSTRUMENT_ID
      COUNTERPARTY_ID
      QUANTITY_FILLED
      AVG_PRICE
    }
  }
  query("ALL_ORDER_TOTALS", ORDER_TOTAL_VIEW) {
    fields {
      SYMBOL
      TOTAL_QUANTITY_ORDERED
      INSTRUMENT_ID
    }
  }
  query("ALL_TRADES", TRADE_VIEW) {
    fields {
      SYMBOL
      COUNTPERPARTY_NAME
      ORDER_QUANTITY
      ORDER_PRICE
      INSTRUMENT_ID
      COUNTERPARTY_ID
      TRADE_QUANTITY
      TRADE_PRICE
      TRADE_ID
      ORDER_ID
    }
  }

  //TODO 5.a Add authorisation, where clauses, indices and restrict fields on the generated dataservers as required by the application.
  //TODO 5.b Add further real-time queries needed by the application here. See the comments at the top of this file for learning and guidance.
}
