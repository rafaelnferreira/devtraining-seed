package global.genesis

import com.google.inject.name.Named
import global.genesis.commons.annotation.Module
import global.genesis.commons.standards.ProcessConstants
import global.genesis.db.engine.WriteResult
import global.genesis.db.updatequeue.NotAnUpdateQueue
import global.genesis.db.updatequeue.RecordUpdate
import global.genesis.db.updatequeue.UpdateType
import javax.inject.Inject
import kotlin.math.max

// borrowed from RXDbImpl
@Module
class PersistentEventPublisher @Inject constructor(
    @Named(ProcessConstants.PROCESS_NAME) private val processName: String,
) {

    private val updateQueue = NotAnUpdateQueue.INSTANCE

    fun createRecordUpdates(
        writeResult: WriteResult,
        vararg otherWriteResults: WriteResult
    ): List<RecordUpdate> = buildList {
        add(writeResult)
        addAll(otherWriteResults)
    }.flatMap { buildUpdate(it) }

    private fun buildUpdate(writeResult: WriteResult): List<RecordUpdate> {
        val savedRecords = writeResult.savedRecords
        val removedRecords = writeResult.removedRecords

        val updates = max(savedRecords.size, removedRecords.size)

        return (0 until updates)
            .mapNotNull {
                val savedRecord = if (it < savedRecords.size) savedRecords[it] else null
                val removedRecord = if (it < removedRecords.size) removedRecords[it] else null

                val updateType = if (savedRecord == null && removedRecord == null) {
                    return@mapNotNull null
                } else if (removedRecord == null) {
                    UpdateType.INSERT
                } else if (savedRecord == null) {
                    UpdateType.DELETE
                } else {
                    UpdateType.MODIFY
                }

                val update = updateQueue.nextRecordUpdate
                val table = (savedRecord ?: removedRecord)!!.tableName

                update.tableName = table
                update.updateType = updateType
                update.record = if (updateType == UpdateType.DELETE) removedRecord else savedRecord
                update.prevRecord = if (updateType == UpdateType.MODIFY) removedRecord else null
                update.modifiedFields = writeResult.modifiedFields.toList()
                update.emitter = processName
                update
            }
    }

}
