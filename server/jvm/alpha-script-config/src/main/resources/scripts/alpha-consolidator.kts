/*
   Consolidators defined here become available for real-time and one-off aggregations as required by the application.

   New to Genesis? Take 5 minutes to learn the basics around consolidator >> https://learn.genesis.global/docs/server/consolidator/introduction/

   Review the basics section to further your understanding >> https://learn.genesis.global/docs/server/consolidator/basics/

   Be sure to learn about the advanced capabilities of consolidator when you have a moment >> https://learn.genesis.global/docs/server/consolidator/advanced/

   If you're looking to do something new, there are a range of annotated examples to help >> https://learn.genesis.global/docs/server/consolidator/examples/

   Else if you want to do something but are still not sure how it could be achieved? Detail your requirement on stack overflow, and let the community help >> https://stackoverflowteams.com/c/genesis-global/questions/ask?tags=consolidator
   Tip: Use the tag "consolidator" and search other questions tagged "consolidator" in case someone has asked the same.
*/

consolidators {
  consolidator("ORDER_STATUS_CONS", TRADE, ORDER_STATUS) {
    select {
      ORDER_STATUS {
        sum { tradeQuantity } into QUANTITY_FILLED
        avg { tradePrice } into AVG_PRICE
      }
    }
    groupBy {
      OrderStatus.ByOrderId(orderId)
    } into {
      indexScan { Trade.ByOrderId(groupId.orderId) }
      build {
        OrderStatus {
          orderId = groupId.orderId
        }
      }
    }
  }
  consolidator("ORDER_TOTAL_CONS", ORDER, ORDER_TOTALS) {
    select {
      ORDER_TOTALS {
        sum { orderQuantity } into TOTAL_QUANTITY_ORDERED
      }
    }
    groupBy {
      OrderTotals.ByInstrumentId(instrumentId)
    }
  }

  //TODO 7.a Fine tune generated consolidators, e.g. adding where blocks, new group bys, etc... as required by the application.
  //TODO 7.b Add further consolidators needed by the application here. See the comments at the top of this file for learning and guidance.
}
