package com.example.grpc.entity.member

import com.example.grpc.service.member.dto.MemberDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("member")
class MemberEntity(
    @Id
    val id: Int? = null,
    val name: String,
    val createdBy: Int,
    val modifiedBy: Int? = null,
    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val modifiedDate: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun of(request: MemberDto.CreateMemberRequest): MemberEntity {
            return with(request) {
                MemberEntity(
                    name = name,
                    createdBy = createdBy,
                )
            }
        }
    }
}
