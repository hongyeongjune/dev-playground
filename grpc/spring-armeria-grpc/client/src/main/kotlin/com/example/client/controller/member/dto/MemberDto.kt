package com.example.client.controller.member.dto

import com.example.client.utils.toLocalDateTime
import com.example.proto.member.modifiedByOrNull
import java.time.LocalDateTime

object MemberDto {
    data class CreateMemberRequest(
        val name: String,
        val createdBy: Int,
    )

    data class MemberResponse(
        val id: Int,
        val name: String,
        val createdBy: Int,
        val modifiedBy: Int?,
        val createdDate: LocalDateTime,
        val modifiedDate: LocalDateTime,
    ) {
        companion object {
            fun of(response: com.example.proto.member.MemberResponse): MemberResponse {
                return MemberResponse(
                    id = response.id,
                    name = response.name,
                    createdBy = response.createdBy,
                    modifiedBy = response.modifiedByOrNull?.value,
                    createdDate = response.createdDate.toLocalDateTime(),
                    modifiedDate = response.modifiedDate.toLocalDateTime(),
                )
            }
        }
    }
}