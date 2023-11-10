package com.yapp.bol.file

import com.yapp.bol.auth.UserId
import com.yapp.bol.file.dto.RawFileData

interface FileService {
    fun uploadFile(request: RawFileData): FileInfo
    fun downloadFile(userId: UserId?, uuid: String): RawFileData

    /**
     * @return Default Image URL
     */
    fun getDefaultGroupImageUrl(): String
}
