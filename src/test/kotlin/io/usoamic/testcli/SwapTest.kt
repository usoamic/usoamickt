package io.usoamic.testcli

import io.usoamic.cli.core.Usoamic
import io.usoamic.cli.util.Coin
import io.usoamic.testcli.other.TestConfig
import org.junit.jupiter.api.Test
import org.web3j.utils.Convert
import java.math.BigInteger
import javax.inject.Inject

class SwapTest {
    @Inject
    lateinit var usoamic: Usoamic

    init {
        BaseUnitTest.componentTest.inject(this)
    }

    @Test
    fun depositEthTest() {
        val value = Convert.toWei("0.1", Convert.Unit.ETHER).toBigInteger()
        val txHash = usoamic.transferEther(TestConfig.PASSWORD, TestConfig.CONTRACT_ADDRESS, value)
        usoamic.waitTransactionReceipt(txHash) {
            assert(it.status == "0x0")
        }
    }

    @Test
    fun burnSwapTest() {
        val value = Coin.HUNDRED.toSat()

        val address = usoamic.account.address

        val accountBalance = usoamic.getBalance(address)
        val accountTokenBalance = usoamic.balanceOf(address)
        val contractBalance = usoamic.getBalance(TestConfig.CONTRACT_ADDRESS)

        val txHash = usoamic.burnSwap(TestConfig.PASSWORD, value)
        usoamic.waitTransactionReceipt(txHash) {
            val newAccountBalance = usoamic.getBalance(address)
            val newAccountTokenBalance = usoamic.balanceOf(address)
            val newContractBalance = usoamic.getBalance(TestConfig.CONTRACT_ADDRESS)

            assert(accountTokenBalance!!.subtract(newAccountTokenBalance).compareTo(value) == 0)

            val swapRate = usoamic.getSwapRate()
            val ethValue = value.multiply(swapRate)

            val maxFee = BigInteger("-10000000000000000")

            assert(newAccountBalance.subtract(accountBalance).subtract(ethValue) >= maxFee)
            assert(contractBalance.subtract(newContractBalance).compareTo(ethValue) == 0)
        }
    }

}