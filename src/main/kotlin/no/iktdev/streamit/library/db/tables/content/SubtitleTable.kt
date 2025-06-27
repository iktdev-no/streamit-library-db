package no.iktdev.streamit.library.db.tables.content

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.io.File

object SubtitleTable : IntIdTable(name = "subtitle") {
    val associatedWithVideo: Column<String> = varchar("associatedWithVideo", 250)
    val language: Column<String> = varchar("language", 16)
    val subtitle: Column<String> = varchar("subtitle", 250)
    val collection: Column<String> = varchar("collection", 250)
    val format: Column<String> = varchar("format", 12)
    init {
        uniqueIndex(associatedWithVideo, language, format)
    }

    fun insertAndIgnore(collection: String, language: String, subtitleFile: String, correspondingVideoFile: String, format: String): InsertStatement<Long> {
        val fileName = File(correspondingVideoFile).nameWithoutExtension
        return SubtitleTable.insertIgnore {
            it[SubtitleTable.associatedWithVideo] = fileName
            it[SubtitleTable.language] = language
            it[SubtitleTable.subtitle] = subtitleFile
            it[SubtitleTable.format] = format
            it[SubtitleTable.collection] = collection
        }
    }

    fun findSubtitleForCollection(collection: String): Query {
        return SubtitleTable.selectAll()
            .where { SubtitleTable.collection eq collection }
    }

    /**
     * Takes in
     * @param videoFile
     * and removes file extension from name when querying
     */
    fun findSubtitleForVideo(videoFile: String): Query {
        val baseName = File(videoFile).nameWithoutExtension
        return SubtitleTable.selectAll()
            .where { associatedWithVideo eq baseName }
    }

    /**
     * Builds upon findSubtitleForVideo(videoFile)
     * Takes in format to limit response
     */
    fun findSubtitleForVideo(videoFile: String, format: String): Query {
        return findSubtitleForVideo(videoFile).andWhere { SubtitleTable.format eq format }
    }

}

