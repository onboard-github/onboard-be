package com.yapp.bol.file

@JvmInline
value class FileId(val value: Long) {
    override fun toString(): String = value.toString()
}

interface FileInfo {
    val id: FileId
    val uuid: String

    fun getUrl(): String
}
