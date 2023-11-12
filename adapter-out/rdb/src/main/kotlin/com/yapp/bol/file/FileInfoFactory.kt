package com.yapp.bol.file

import java.lang.UnsupportedOperationException
import java.util.UUID

object FileInfoFactory {
    fun convert(entity: FileEntity): FileInfo {
        return when (entity.purpose) {
            FilePurpose.GAME_IMAGE,
            FilePurpose.GROUP_DEFAULT_IMAGE,
            FilePurpose.GROUP_IMAGE ->
                createS3PublicFileInfo(entity)

            FilePurpose.MATCH_IMAGE -> createS3PublicFileInfo(entity) // TODO : 추후 알맞은 것으로 변경
        }
    }

    fun create(id: FileId): FileInfo = RequestCreateFileInfo(id)

    private fun createS3PublicFileInfo(entity: FileEntity): S3PublicFileInfo =
        S3PublicFileInfo(FileId(entity.id), entity.name)
}

internal class RequestCreateFileInfo(
    override val id: FileId,
) : FileInfo {
    override val uuid: String = UUID.randomUUID().toString()
    override fun getUrl(): String {
        throw UnsupportedOperationException()
    }
}
