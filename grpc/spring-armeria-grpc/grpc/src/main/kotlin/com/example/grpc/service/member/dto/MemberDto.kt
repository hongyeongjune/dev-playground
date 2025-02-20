package com.example.grpc.service.member.dto

import com.example.grpc.entity.member.MemberEntity
import com.example.proto.member.CreateMemberRequest
import java.time.LocalDateTime

object MemberDto {
    data class CreateMemberRequest(
        val name: String,
        val createdBy: Int,
    ) {
        companion object {
            fun of(createMemberRequest: com.example.proto.member.CreateMemberRequest): CreateMemberRequest {
                return with(createMemberRequest) {
                    CreateMemberRequest(
                        name = name,
                        createdBy = createdBy,
                    )
                }
            }
        }
    }

    data class MemberResponse(
        val id: Int,
        val name: String,
        val createdBy: Int,
        val modifiedBy: Int?,
        val createdDate: LocalDateTime,
        val modifiedDate: LocalDateTime,
    ) {
        companion object {
            fun of(memberEntity: MemberEntity): MemberResponse {
                return with(memberEntity) {
                    MemberResponse(
                        id = id!!,
                        name = name,
                        createdBy = createdBy,
                        modifiedBy = modifiedBy,
                        createdDate = createdDate,
                        modifiedDate = modifiedDate,
                    )
                }
            }
        }
    }
}