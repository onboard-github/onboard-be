package com.yapp.bol.file

data class MockFileInfo(
    override val id: FileId,
    override val uuid: String,
) : FileInfo {
    constructor() : this(FileId(0), "uuid")

    override fun getUrl(): String = uuid
}
