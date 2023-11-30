package global.genesis.alpha.message.event

data class CounterPartyUpload(val name: String, val id: String?)

data class BulkCounterPartyUpload(val quantity: Int)