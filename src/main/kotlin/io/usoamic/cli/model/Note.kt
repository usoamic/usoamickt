package io.usoamic.cli.model

import io.usoamic.cli.enum.NoteVisibility
import java.math.BigInteger

data class Note constructor(
    val noteId: BigInteger,
    val visibility: NoteVisibility,
    val refId: BigInteger,
    val content: String,
    val author: String,
    val timestamp: BigInteger
){
    class Builder {
        private lateinit var noteId: BigInteger
        private lateinit var visibility: NoteVisibility
        private lateinit var refId: BigInteger
        private lateinit var content: String
        private lateinit var author: String
        private lateinit var timestamp: BigInteger

        fun setNoteId(noteId: BigInteger) = apply {
            this.noteId = noteId
        }

        fun setVisibility(visibility: NoteVisibility) = apply {
            this.visibility = visibility
        }

        fun setRefId(refId: BigInteger) = apply {
            this.refId = refId
        }

        fun setContent(content: String) = apply {
            this.content = content
        }

        fun setAuthor(author: String) = apply {
            this.author = author
        }

        fun setTimestamp(timestamp: BigInteger) = apply {
            this.timestamp = timestamp
        }

        fun build() = Note(
            noteId,
            visibility,
            refId,
            content,
            author,
            timestamp
        )
    }
}