package com.yapp.bol.admin

import com.yapp.bol.AuditingEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "admin_role")
class AdminRoleEntity : AuditingEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_role_id")
    var id: Long = 0
        protected set

    @Column(name = "users_id")
    var userId: Long = 0
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    lateinit var role: AdminRole
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    lateinit var state: AdminRoleState
        protected set

    @Column(name = "memo")
    lateinit var memo: String
        protected set
}
