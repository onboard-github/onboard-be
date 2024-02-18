package com.yapp.bol.file

import com.yapp.bol.AuditingEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "file")
@Entity
class FileEntity : AuditingEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    var id: Long = 0
        protected set

    @Column(name = "name")
    lateinit var name: String
        protected set

    @Column(name = "users_id")
    var userId: Long = 0
        protected set

    @Column(name = "purpose")
    @Enumerated(value = EnumType.STRING)
    lateinit var purpose: FilePurpose
        protected set

    fun toFileInfo(): FileInfo {
        return when (purpose) {
            FilePurpose.GAME_IMAGE,
            FilePurpose.GROUP_DEFAULT_IMAGE,
            FilePurpose.GROUP_IMAGE,
            ->
                createS3PublicFileInfo(this)

            FilePurpose.MATCH_IMAGE -> createS3PublicFileInfo(this) // TODO : 추후 알맞은 것으로 변경
        }
    }

    private fun createS3PublicFileInfo(entity: FileEntity): S3PublicFileInfo =
        S3PublicFileInfo(FileId(entity.id), entity.name)

    companion object {

        fun of(id: FileId): FileEntity = FileEntity().apply { this.id = id.value }
        fun of(
            name: String,
            userId: Long,
            purpose: FilePurpose,
        ): FileEntity = FileEntity().apply {
            this.name = name
            this.userId = userId
            this.purpose = purpose
        }
    }
}
