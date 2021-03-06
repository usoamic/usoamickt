package io.usoamic.usoamickt.core

import io.usoamic.usoamickt.enumcls.NoteVisibility
import io.usoamic.usoamickt.enumcls.TxSpeed
import io.usoamic.usoamickt.model.Note
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import java.math.BigInteger

open class Notes constructor(fileName: String, filePath: String, contractAddress: String, node: String) : Ideas(fileName, filePath, contractAddress, node) {
    fun addPublicNote(password: String, content: String): String = addNote(password, NoteVisibility.PUBLIC, content)

    fun addUnlistedNote(password: String, content: String): String = addNote(password, NoteVisibility.UNLISTED, content)

    private fun addNote(password: String, noteVisibility: NoteVisibility, content: String, txSpeed: TxSpeed = TxSpeed.Auto): String = executeTransaction(
        password,
        when(noteVisibility) {
            NoteVisibility.PUBLIC -> "addPublicNote"
            NoteVisibility.UNLISTED -> "addUnlistedNote"
        },
        listOf(Utf8String(content)),
        txSpeed
    )

    fun getNumberOfPublicNotes(): BigInteger? = executeCallEmptyPassValueAndUint256Return("getNumberOfPublicNotes")

    fun getNumberOfNotesByAuthor(address: String): BigInteger? = executeCallUint256ValueReturn(
        "getNumberOfNotesByAuthor",
        listOf(Address(address))
    )

    fun getLastNoteId(): BigInteger? = getNumberOfPublicNotes()!!.subtract(BigInteger.ONE)

    fun getLastNoteIdByAddress(address: String): BigInteger? = getNumberOfNotesByAuthor(address)!!.subtract(BigInteger.ONE)

    fun getNoteByAuthor(author: String, noteId: BigInteger): Note =
        getAndPrepareNote(
            "getNoteByAuthor",
            listOf(
                Address(author),
                Uint256(noteId)
            )
        )

    fun getNote(noteRefId: BigInteger): Note = getAndPrepareNote("getNote", listOf(Uint256(noteRefId)))

    private fun getAndPrepareNote(name: String, inputParameters: List<Type<out Any>>): Note {
        val function = Function(
            name,
            inputParameters,
            listOf(
                object: TypeReference<Bool>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Uint8>() { },
                object: TypeReference<Uint256>() { },
                object: TypeReference<Utf8String>() { },
                object: TypeReference<Address>() { },
                object: TypeReference<Uint256>() { }
            )
        )
        val result = executeCall(function)
        val visibilityId = result[2].value as BigInteger

        return Note.Builder()
            .setIsExist(result[0].value as Boolean)
            .setNoteId(result[1].value as BigInteger)
            .setVisibility(NoteVisibility.values()[visibilityId.toInt()])
            .setNoteRefId(result[3].value as BigInteger)
            .setContent(result[4].value as String)
            .setAuthor(result[5].value as String)
            .setTimestamp(result[6].value as BigInteger)
            .build()
    }
}