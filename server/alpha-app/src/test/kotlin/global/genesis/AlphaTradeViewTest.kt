package global.genesis

import global.genesis.db.util.AbstractDatabaseTest
import global.genesis.db.util.TestUtil
import global.genesis.dictionary.GenesisDictionary
import global.genesis.gen.dao.enums.Direction
import global.genesis.gen.view.entity.TradeView
import global.genesis.gen.dao.Trade
import global.genesis.gen.config.view.TRADE_VIEW
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import org.joda.time.DateTime

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

class AlphaTradeViewTest : AbstractDatabaseTest() {
    
    override fun createMockDictionary(): GenesisDictionary = prodDictionary()

    @BeforeEach
    fun setup() {
        TestUtil.loadData(resolvePath("data/TEST_DATA.csv"), rxDb)
    }

    private fun buildTrade(tradeId: String, now: DateTime = DateTime.now()) =
        Trade.builder()
            .setTradeDate(now)
            .setCounterpartyId("2") // COUNTERPARTY_NAME = "Testing AG"
            .setInstrumentId("2")   // INSTRUMENT_NAME = "BAR.L"
            .setPrice(12.0)
            .setQuantity(100)
            .setDirection(Direction.BUY)
            .setTradeId(tradeId)
            .build()

    @Test
    fun test_get_single_trade_by_id() {
        val now = DateTime.now()
        val trade = buildTrade("1L", now)
        rxEntityDb.insert(trade).blockingGet()
        val tradeView = rxEntityDb.get(TradeView.byId(trade.tradeId)).blockingGet()
        if (tradeView != null) {
            assertEquals("Testing AG", tradeView.counterpartyName)
            assertEquals("BAR.L", tradeView.instrumentName)
            assertEquals(now, tradeView.tradeDate)
            assertEquals(12.0, tradeView.price, 0.0)
            assertEquals((100).toInt(), tradeView.quantity)
            assertEquals(Direction.BUY, tradeView.direction)
        }
    }

    @Test
    fun test_with_single_trade__use_getBulk() {
        val now = DateTime.now()
        val trade = buildTrade("1L", now)
        rxEntityDb.insert(trade).blockingGet()
        val tradeViewList = rxEntityDb.getBulk(TRADE_VIEW).toList().blockingGet()
        
        assertEquals(1, tradeViewList.size)
        val tradeView = tradeViewList.first()
        assertEquals("Testing AG", tradeView.counterpartyName)
        assertEquals("BAR.L", tradeView.instrumentName)
        assertEquals(now, tradeView.tradeDate)
        assertEquals(12.0, tradeView.price, 0.0)
        assertEquals((100).toInt(), tradeView.quantity)
        assertEquals(Direction.BUY, tradeView.direction)
    }

    @Test
    fun test_get_multiple_trades() {
        rxEntityDb.insertAll(
            buildTrade("1T"),
            buildTrade("2T"),
            buildTrade("3T"),
            buildTrade("4T"),
            buildTrade("5T"),
        ).blockingGet()
        val count = rxEntityDb.getBulk(TRADE_VIEW).count().blockingGet()
        assertEquals(5, count)
    }
}
