package com.yapp.bol.file

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.yapp.bol.IllegalFileStateException
import com.yapp.bol.auth.UserId
import com.yapp.bol.aws.AwsProperties
import com.yapp.bol.file.dto.RawFileData
import java.io.ByteArrayInputStream
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class FileClient(
    private val fileRepository: FileRepository,
    private val s3Client: AmazonS3,
    awsProperties: AwsProperties,
) : FileQueryRepository, FileCommandRepository {

    private val bucketName: String = awsProperties.s3.bucket

    override fun saveFile(file: RawFileData): FileInfo {
        val key = UUID.randomUUID().toString()

        val metadata = ObjectMetadata().apply {
            contentType = file.contentType
            addUserMetadata(METADATA_PURPOSE, file.purpose.toString())
            addUserMetadata(METADATA_USER_ID, file.userId.value.toString())
        }

        s3Client.putObject(bucketName, key, ByteArrayInputStream(file.content), metadata)

        val entity = FileEntity.of(key, file.userId.value, file.purpose)

        return fileRepository.save(entity).toFileInfo()
    }

    override fun getFile(uuid: String): RawFileData {
        val s3Object = s3Client.getObject(bucketName, uuid)
        val accessLevelValue =
            s3Object.objectMetadata.getUserMetaDataOf(METADATA_PURPOSE) ?: throw IllegalFileStateException
        val purpose = FilePurpose.valueOf(accessLevelValue)
        val userId = s3Object.objectMetadata.getUserMetaDataOf(METADATA_USER_ID) ?: throw IllegalFileStateException
        val contentType = s3Object.objectMetadata.contentType ?: throw IllegalFileStateException

        return RawFileData(
            userId = UserId(userId.toLong()),
            content = s3Object.objectContent.delegateStream.readAllBytes(),
            contentType = contentType,
            purpose = purpose,
        )
    }

    override fun getFileInfo(uuid: String): FileInfo? {
        return fileRepository.findByName(uuid)?.toFileInfo()
    }

    override fun getFiles(filePurpose: FilePurpose): List<FileInfo> {
        return fileRepository.findAllByPurpose(filePurpose).map { it.toFileInfo() }
    }

    companion object {
        private const val METADATA_PURPOSE = "purpose"
        private const val METADATA_USER_ID = "user_id"
    }
}
