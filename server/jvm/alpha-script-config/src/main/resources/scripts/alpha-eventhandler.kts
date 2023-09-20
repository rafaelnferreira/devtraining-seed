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
    eventHandler<Trade>(name = "TRADE_INSERT") {
        schemaValidation = false

        onValidate { event ->
            val qty = event.details.quantity ?: 0
            require(qty > 0 ) { "Quantity must be greater than zero " }
            ack()
        }

        onCommit { event ->
                entityDb.insert(event.details)
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
            require(entity != null ) { "Counterparty must exist to be modified"}
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
            require(entity != null ) { "Instrument must exist to be modified"}
            ack()
        }
        onCommit { event ->
            entityDb.modify(event.details)
            ack()
        }
    }


}