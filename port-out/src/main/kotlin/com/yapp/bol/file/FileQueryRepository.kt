package com.yapp.bol.file

import com.yapp.bol.file.dto.RawFileData

interface FileQueryRepository {
    fun getFile(uuid: String): RawFileData?

    /**
     * @return url 목록
     */
    fun getFiles(filePurpose: FilePurpose): List<String>
}
