import global.genesis.TradeStateMachine

/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide event handler config for multi-pro-code-test.
 *
 * Modification History
 */
eventHandler {

    val stateMachine = inject<TradeStateMachine>()

    eventHandler<Trade>(name = "TRADE_INSERT") {
        schemaValidation = false

        onValidate { event ->
            val message = event.details

            verify {
                message.counterpartyId?.let { counterpartyId ->
                    entityDb hasEntry Counterparty.ById(counterpartyId)
                }

                entityDb hasEntry Instrument.ById(message.instrumentId)
            }

            val qty = message.quantity ?: 0
            require(qty > 0) { "Quantity must be greater than zero " }
            ack()
        }

        onCommit { event ->
            val trade = event.details
            trade.enteredBy = event.userName
            stateMachine.insert(trade)
            ack()
        }
    }

    eventHandler<Trade>(name = "TRADE_MODIFY") {
        schemaValidation = false
        onValidate {
            val entity = entityDb.get(it.details)
            require(entity != null) { "Trade must exist to be modified" }
            ack()
        }
        onCommit { event ->
            stateMachine.modify(event.details)
            ack()
        }
    }

    eventHandler<Trade>(name = "TRADE_ALLOCATED", transactional = true) {
        onCommit { event ->
            val message = event.details
            stateMachine.modify(message.tradeId) { trade ->
                trade.tradeStatus = TradeStatus.ALLOCATED
            }
            ack()
        }
    }

    eventHandler<Trade>(name = "TRADE_CANCELLED", transactional = true) {
        onCommit { event ->
            val message = event.details
            stateMachine.modify(message.tradeId) { trade ->
                trade.tradeStatus = TradeStatus.CANCELLED
            }
            ack()
        }
    }

    eventHandler<Counterparty>(name = "COUNTERPARTY_INSERT") {
        schemaValidation = false
        onCommit { event ->
            entityDb.insert(event.details)
            ack()
        }
    }

    eventHandler<Counterparty>(name = "COUNTERPARTY_DELETE") {
        schemaValidation = false

        onCommit { event ->
            entityDb.delete(event.details)
            ack()
        }
    }

    eventHandler<Counterparty>(name = "COUNTERPARTY_MODIFY") {
        schemaValidation = false
        onValidate {
            val entity = entityDb.get(it.details)
            require(entity != null) { "Counterparty must exist to be modified" }
            ack()
        }
        onCommit { event ->
            entityDb.modify(event.details)
            ack()
        }
    }


    eventHandler<Instrument>(name = "INSTRUMENT_INSERT") {
        schemaValidation = false
        onCommit { event ->
            entityDb.insert(event.details)
            ack()
        }
    }

    eventHandler<Instrument>(name = "INSTRUMENT_DELETE") {
        schemaValidation = false

        onCommit { event ->
            entityDb.delete(event.details)
            ack()
        }
    }

    eventHandler<Instrument>(name = "INSTRUMENT_MODIFY") {
        schemaValidation = false
        onValidate {
            val entity = entityDb.get(it.details)
            require(entity != null) { "Instrument must exist to be modified" }
            ack()
        }
        onCommit { event ->
            entityDb.modify(event.details)
            ack()
        }
    }


}