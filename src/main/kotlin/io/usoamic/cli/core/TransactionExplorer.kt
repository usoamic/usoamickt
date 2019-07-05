package io.usoamic.cli.core

import io.usoamic.cli.model.Transaction
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger

// bool exist, uint256 txId, address from, address to, uint256 value, uint256 timestamp

open class TransactionExplorer constructor(filename: String) : TransactionManager(filename) {
    fun getTransaction(txId: BigInteger): Transaction {
        val function = Function(
            "getTransaction",
            listOf(Uint256(txId)),
            listOf(
                object: TypeReference<Bool>() {},
                object: TypeReference<Uint256>() {},
                object: TypeReference<Address>() {},
                object: TypeReference<Address>() {},
                object: TypeReference<Uint256>() {},
                object: TypeReference<Uint256>() {}
            )
        )
        val result = executeCall(function)
        return Transaction.Builder()
            .setExist(result[0] as Boolean)
            .setTxId(result[1] as BigInteger)
            .setFrom(result[2] as String)
            .setTo(result[3] as String)
            .setValue(result[4] as BigInteger)
            .setTimestamp(result[5] as BigInteger)
            .build()
    }

    fun getNumberOfTransactions(): BigInteger? = executeCallEmptyPassValueAndUint256Return("getNumberOfTransactions")

    fun getNumberOfTransactionsByAddress(owner: String): BigInteger? = executeCallUint256ValueReturn("getNumberOfTransactionsByAddress", listOf(Address(owner)))
}