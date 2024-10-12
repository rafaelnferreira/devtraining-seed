pipelines {
    val batchPollSource = dbBatchQuery<LegalEntity> {
        source = "MY_SOURCE"
        index = LegalEntity.ById
        dbLookupDelayMs = 5000
    }

    pipeline("LEGAL_ENTITY_PIPELINE") {
        source(batchPollSource)
            .sink(logSink())
    }
}