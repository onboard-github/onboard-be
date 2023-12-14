package com.yapp.bol.file

@JvmInline
value class FileId(val value: Long)

interface FileInfo {
    val id: FileId
    val uuid: String

    fun getUrl(): String
}
