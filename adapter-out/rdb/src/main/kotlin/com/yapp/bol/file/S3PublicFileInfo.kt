package com.yapp.bol.file

class S3PublicFileInfo(
    override val id: FileId,
    override val uuid: String,
) : FileInfo {
    override fun getUrl(): String = FileNameConverter.convertFileUrl(uuid)
}
