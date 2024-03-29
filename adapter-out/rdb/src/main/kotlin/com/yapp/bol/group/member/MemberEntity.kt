package com.yapp.bol.group.member

import com.yapp.bol.AuditingEntity
import com.yapp.bol.InvalidGuestIdException
import com.yapp.bol.InvalidMemberRoleException
import com.yapp.bol.auth.UserId
import com.yapp.bol.game.member.GameMemberEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where

@Entity
@Table(name = "member")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE member SET deleted=true, nickname=null WHERE member_id = ?")
class MemberEntity(
    id: Long = 0,
    userId: Long? = null,
    groupId: Long = 0,
    role: MemberRole,
    nickname: String,
) : AuditingEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    val id: Long = id

    @Column(name = "users_id")
    var userId: Long? = userId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    var role: MemberRole = role
        protected set

    @Column(name = "nickname")
    val nickname: String = nickname

    @Column(name = "level")
    val level: Int = 0

    @Column(name = "deleted")
    val deleted: Boolean = false

    @Column(name = "group_id", nullable = false)
    val groupId: Long = groupId

    @ManyToMany(mappedBy = "memberId", fetch = FetchType.LAZY)
    lateinit var gameMembers: List<GameMemberEntity>
        protected set

    // guest -> host
    fun toHost(userId: Long) {
        if (this.userId != null) throw InvalidGuestIdException
        this.userId = userId
        this.role = MemberRole.HOST
    }

    fun toHost() {
        if (this.userId == null) {
            throw InvalidMemberRoleException
        }

        this.role = MemberRole.HOST
    }

    fun toOwner() {
        if (this.userId == null) {
            throw InvalidMemberRoleException
        }

        this.role = MemberRole.OWNER
    }
}

fun MemberEntity.toDomain(): Member {
    if (this.userId == null) return toGuestMember()

    return when (this.role) {
        MemberRole.GUEST -> toGuestMember()
        MemberRole.HOST -> HostMember(
            id = MemberId(this.id),
            userId = UserId(this.userId ?: throw InvalidMemberRoleException),
            nickname = this.nickname,
            level = this.level,
        )

        MemberRole.OWNER -> OwnerMember(
            id = MemberId(this.id),
            userId = UserId(this.userId ?: throw InvalidMemberRoleException),
            nickname = this.nickname,
            level = this.level,
        )
    }
}

private fun MemberEntity.toGuestMember(): GuestMember =
    GuestMember(
        id = MemberId(this.id),
        nickname = this.nickname,
        level = this.level,
    )

fun Member.toEntity(groupId: Long): MemberEntity = MemberEntity(
    id = this.id.value,
    userId = this.userId?.value,
    role = this.role,
    nickname = this.nickname,
    groupId = groupId,
)
