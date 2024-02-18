package com.yapp.bol.group

import com.yapp.bol.NotFoundFileException
import com.yapp.bol.file.FileRepository
import com.yapp.bol.group.dto.GroupMemberList
import com.yapp.bol.group.member.MemberList
import com.yapp.bol.group.member.MemberRepository
import com.yapp.bol.group.member.OwnerMember
import com.yapp.bol.group.member.toDomain
import com.yapp.bol.group.member.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
internal class GroupCommandRepositoryImpl(
    private val fileRepository: FileRepository,
    private val groupRepository: GroupRepository,
    private val memberRepository: MemberRepository,
) : GroupCommandRepository {
    @Transactional
    override fun createGroup(group: Group, owner: OwnerMember): GroupMemberList {
        val groupEntity = groupRepository.save(group.toEntity())

        val ownerEntity = memberRepository.save(owner.toEntity(groupEntity.id))
        val ownerMember = ownerEntity.toDomain() as OwnerMember

        val memberList = MemberList(ownerMember)

        return GroupMemberList(groupEntity.toDomain(), memberList)
    }

    private fun Group.toEntity(): GroupEntity = GroupEntity(
        id = id,
        name = name,
        description = description,
        organization = organization,
        profileImage = fileRepository.findByIdOrNull(profileImage.id.value) ?: throw NotFoundFileException,
        accessCode = accessCode,
    )

    override fun deleteGroup(groupId: GroupId) {
        groupRepository.deleteById(groupId.value)
    }
}
