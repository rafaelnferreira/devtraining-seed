package mycompany.events

import com.google.inject.Inject
import com.google.inject.Provider
import global.genesis.commons.annotation.Module
import global.genesis.eventhandler.typed.async.AsyncEventHandler
import global.genesis.message.core.event.Event
import global.genesis.message.core.event.EventReply
import global.genesis.net.GenesisMessageClient

data class NoOp(val id: String)

@Module
class EventNoOp @Inject constructor() : AsyncEventHandler<NoOp, EventReply> {
    override suspend fun process(message: Event<NoOp>): EventReply {
        return ack()
    }


}