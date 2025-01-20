eventHandler {

    eventHandler<CharlieTable>(name = "CHARLIE_TABLE_INSERT") {
        onCommit { event ->
            val table = event.details
            entityDb.insert(table)
            ack()
        }
    }

}