/*
   Event handlers defined here become available for clients and system interfaces to insert, modify, delete (else perform some other triggered action) here.

   New to Genesis? Take 5 minutes to learn the basics around event handler >> https://learn.genesis.global/docs/server/event-handler/introduction/

   Review the basics section to further your understanding >> https://learn.genesis.global/docs/server/event-handler/basics/

   Be sure to learn about the advanced capabilities of event handler when you have a moment >> https://learn.genesis.global/docs/server/event-handler/advanced/

   If you're looking to do something new, there are a range of annotated examples to help >> https://learn.genesis.global/docs/server/event-handler/examples/

   Else if you want to do something but are still not sure how it could be achieved? Detail your requirement on stack overflow, and let the community help >> https://stackoverflowteams.com/c/genesis-global/questions/ask?tags=events
   Tip: Use the tag "events" and search other questions tagged "events" in case someone has asked the same.
*/

eventHandler {
  eventHandler<global.genesis.gen.dao.Instrument>("INSTRUMENT_INSERT") {
    onCommit { event ->
      val details = event.details
      val insertedRow = entityDb.insert(details)
      ack(listOf(mapOf(
        "INSTRUMENT_ID" to insertedRow.record.instrumentId,
      )))
    }
  }
  eventHandler<global.genesis.gen.dao.Instrument>("INSTRUMENT_MODIFY") {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.Instrument.ById>("INSTRUMENT_DELETE") {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.Counterparty>("COUNTERPARTY_INSERT") {
    onCommit { event ->
      val details = event.details
      val insertedRow = entityDb.insert(details)
      ack(listOf(mapOf(
        "COUNTERPARTY_ID" to insertedRow.record.counterpartyId,
      )))
    }
  }
  eventHandler<global.genesis.gen.dao.Counterparty>("COUNTERPARTY_MODIFY") {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.Counterparty.ById>("COUNTERPARTY_DELETE") {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.Order>("ORDER_INSERT") {
    onCommit { event ->
      val details = event.details
      val insertedRow = entityDb.insert(details)
      ack(listOf(mapOf(
        "ORDER_ID" to insertedRow.record.orderId,
      )))
    }
  }
  eventHandler<global.genesis.gen.dao.Order>("ORDER_MODIFY") {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.Order.ById>("ORDER_DELETE") {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.OrderTotals>("ORDER_TOTALS_INSERT") {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.OrderTotals>("ORDER_TOTALS_MODIFY") {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.OrderTotals.ByInstrumentId>("ORDER_TOTALS_DELETE") {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.OrderStatus>("ORDER_STATUS_INSERT") {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.OrderStatus>("ORDER_STATUS_MODIFY") {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.OrderStatus.ByOrderId>("ORDER_STATUS_DELETE") {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.Trade>("TRADE_INSERT") {
    onCommit { event ->
      val details = event.details
      val insertedRow = entityDb.insert(details)
      ack(listOf(mapOf(
        "TRADE_ID" to insertedRow.record.tradeId,
      )))
    }
  }
  eventHandler<global.genesis.gen.dao.Trade>("TRADE_MODIFY") {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<global.genesis.gen.dao.Trade.ById>("TRADE_DELETE") {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }

  //TODO 6.a Fine tune generated events, e.g. validations, permissions etc... as required by the application
  //TODO 6.b Add further events needed by the application here. See the comments at the top of this file for learning and guidance.
}
