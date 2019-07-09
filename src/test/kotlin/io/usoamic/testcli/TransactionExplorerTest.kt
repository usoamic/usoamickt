package io.usoamic.testcli

import io.usoamic.cli.core.Usoamic
import io.usoamic.cli.util.Coin
import io.usoamic.testcli.other.TestConfig
import org.junit.jupiter.api.Test
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.protocol.Web3j
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

class TransactionExplorerTest {
    @Inject
    lateinit var usoamic: Usoamic

    @Inject
    lateinit var web3j: Web3j

    init {
        BaseUnitTest.componentTest.inject(this)
    }

    @Test
    fun getNumberOfTransactionsByAddress() {
        val credentials = Credentials.create(Keys.createEcKeyPair())
        val numberOfTx = usoamic.getNumberOfTransactionsByAddress(credentials.address)
        assert(numberOfTx == BigInteger.ZERO)

        val defaultNumberOfTx = usoamic.getNumberOfTransactionsByAddress(TestConfig.DEFAULT_ADDRESS)!!
        assert(defaultNumberOfTx >= BigInteger.ZERO)
    }


    @Test
    fun getTransactionTest() {
        val credentials = Credentials.create(Keys.createEcKeyPair())
        val value = Coin.fromCoin("10.231").toSat()

        val txHash = usoamic.transfer(TestConfig.PASSWORD, credentials.address, value)

        usoamic.waitTransactionReceipt(txHash) {
            val numberOfTx = usoamic.getNumberOfTransactions()!!.subtract(BigInteger.ONE)
            val transaction = usoamic.getTransaction(numberOfTx)
            println("Transaction: $transaction")
            assert(transaction.isExist)
            assert(transaction.from == TestConfig.DEFAULT_ADDRESS)
            assert(transaction.to == credentials.address)
            assert(transaction.txId == numberOfTx)
            assert(transaction.value == value)
        }
    }
}