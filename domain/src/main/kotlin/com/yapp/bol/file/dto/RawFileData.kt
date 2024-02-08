package com.yapp.bol.file.dto

import com.yapp.bol.auth.UserId
import com.yapp.bol.file.FilePurpose

data class RawFileData(
    val userId: UserId,
    val contentType: String,
    val content: ByteArray,
    val purpose: FilePurpose,
)
