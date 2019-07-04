package io.usoamic.cli.core

import io.usoamic.cli.enum.IdeaStatus
import io.usoamic.cli.model.Idea
import io.usoamic.cli.model.Vote
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.generated.Uint256
import java.math.BigInteger
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint8
import java.lang.Exception


class Usoamic constructor(filename: String) : TransactionManager(filename) {
    init {

    }

    @Throws(Exception::class)
    fun balanceOf(address: String): BigInteger? {
        val function = Function(
            "balanceOf",
            listOf(Address(address)),
            listOf(object: TypeReference<Uint256>() { })
        )
        val result = executeCallSingleValueReturn(function)
        return if(result == null) result else result as BigInteger
    }

    @Throws(Exception::class)
    fun addIdea(password: String, description: String): String {
        val function = Function(
            "addIdea",
            listOf(Utf8String(description)),
            emptyList()
        )
        return executeTransaction(password, function)
    }

    @Throws(Exception::class)
    fun getIdea(ideaId: BigInteger): Idea {
        val function = Function(
            "getIdea",
            listOf(Uint256(ideaId)),
            listOf(
                object: TypeReference<Bool>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Address>() { },
                object: TypeReference<Utf8String>() { },
                object: TypeReference<Uint8>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Uint256>() { }
            )
        )
        val result = executeCall(function)
        val ideaStatusId = result[4].value as BigInteger

        return Idea.Builder()
            .setIsExist(result[0].value as Boolean)
            .setIdeaId(result[1].value as BigInteger)
            .setAuthor(result[2].value as String)
            .setDescription(result[3].value as String)
            .setIdeaStatus(IdeaStatus.values()[ideaStatusId.toInt()])
            .setTimestamp(result[5].value as BigInteger)
            .setNumberOfSupporters(result[6].value as BigInteger)
            .setNumberOfAbstained(result[7].value as BigInteger)
            .setNumberOfVotedAgainst(result[8].value as BigInteger)
            .setNumberOfParticipants(result[9].value as BigInteger)
            .build()
    }

    @Throws(Exception::class)
    fun getVote(ideaId: BigInteger, voteId: BigInteger): Vote {
        val function = Function(
            "getVote",
            listOf(
                Uint256(ideaId),
                Uint256(voteId)
            ),
            listOf(
                object: TypeReference<Bool>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Address>() { },
                object: TypeReference<Utf8String>() { },
                object: TypeReference<Utf8String>() { }
            )
        )
        val result = executeCall(function)

        return Vote.Builder()
            .setIsExist(result[0].value as Boolean)
            .setIdeaId(result[1].value as BigInteger)
            .setVoteId(result[2].value as BigInteger)
            .setVoter(result[3].value as String)
            .setVoteType(result[4].value as String)
            .setComment(result[5].value as String)
            .build()
    }
}