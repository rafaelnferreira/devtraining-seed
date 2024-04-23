/*
   Request servers defined here become available for clients to query application data.

   New to Genesis? Take 5 minutes to learn the basics around request server >> https://learn.genesis.global/docs/server/request-server/introduction/

   Review the basics section to further your understanding >> https://learn.genesis.global/docs/server/request-server/basics/

   Be sure to learn about the advanced capabilities of request server when you have a moment >> https://learn.genesis.global/docs/server/request-server/advanced/

   If you're looking to do something new, there are a range of annotated examples to help >> https://learn.genesis.global/docs/server/request-server/examples/

   Else if you want to do something but are still not sure how it could be achieved? Detail your requirement on stack overflow, and let the community help >> https://stackoverflowteams.com/c/genesis-global/questions/ask?tags=requestserver
   Tip: Use the tag "request servers" and search other questions tagged "request servers" in case someone has asked the same.
*/

requestReplies {
  requestReply(INSTRUMENT)
  requestReply(COUNTERPARTY)
  requestReply(ORDER)
  requestReply(ORDER_TOTALS)
  requestReply(ORDER_STATUS)
  requestReply(TRADE)
  requestReply(ORDER_VIEW)
  requestReply(ORDER_TOTAL_VIEW)
  requestReply(TRADE_VIEW)

  //TODO 4.a Add authorisation, where clauses, indices and restrict fields on the generated request server queries as required by the application.
  //TODO 4.b Add further request server queries (including custom request servers) as needed by the application here. See the comments at the top of this file for learning and guidance.
}
