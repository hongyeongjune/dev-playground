package com.example.grpc.handler.member

import com.example.grpc.service.member.dto.MemberDto
import com.example.grpc.utils.toTimestamp
import com.example.proto.member.MemberResponse
import com.example.proto.member.memberResponse
import com.google.protobuf.Int32Value

object MemberMapper {
    fun fromMemberResponse(response: MemberDto.MemberResponse): MemberResponse {
        return memberResponse {
            id = response.id
            name = response.name
            createdBy = response.createdBy
            response.modifiedBy?.let { modifiedBy = Int32Value.of(it) }
            createdDate = response.createdDate.toTimestamp()
            modifiedDate = response.modifiedDate.toTimestamp()
        }
    }
}