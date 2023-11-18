package com.yapp.bol.file

@JvmInline
value class FileId(val value: Long)

interface FileInfo {
    val id: FileId
    val uuid: String

    fun getUrl(): String
}

data class MockFileInfo(
    override val id: FileId,
    override val uuid: String,
) : FileInfo {
    constructor() : this(FileId(0), "uuid")

    override fun getUrl(): String = uuid
}
