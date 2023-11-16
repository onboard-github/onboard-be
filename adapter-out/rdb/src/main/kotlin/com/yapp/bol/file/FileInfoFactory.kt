package com.yapp.bol.file

import java.lang.UnsupportedOperationException
import java.util.UUID

fun FileInfo(id: FileId): FileInfo = RequestCreateFileInfo(id)

internal class RequestCreateFileInfo(
    override val id: FileId,
) : FileInfo {
    override val uuid: String = UUID.randomUUID().toString()
    override fun getUrl(): String {
        throw UnsupportedOperationException()
    }
}
