package com.yapp.bol.group

import com.yapp.bol.AuditingEntity
import com.yapp.bol.file.FileEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where

@Entity
@Table(name = "group_table")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE group_table SET deleted=true WHERE group_id = ?")
internal class GroupEntity(
    id: GroupId = GroupId(0),
    name: String,
    description: String,
    organization: String?,
    profileImage: FileEntity,
    accessCode: String,
) : AuditingEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    val id: Long = id.value

    @Column(name = "name")
    val name: String = name

    @Column(name = "description")
    val description: String = description

    @Column(name = "organization")
    val organization: String? = organization

    @ManyToOne
    @JoinColumn(name = "profile_image_id")
    val profileImage: FileEntity = profileImage

    @Column(name = "access_code")
    val accessCode: String = accessCode

    @Column(name = "deleted")
    val deleted: Boolean = false
}

internal fun GroupEntity.toDomain(): Group = Group(
    id = GroupId(id),
    name = name,
    description = description,
    organization = organization,
    profileImage = profileImage.toFileInfo(),
    accessCode = accessCode,
)
