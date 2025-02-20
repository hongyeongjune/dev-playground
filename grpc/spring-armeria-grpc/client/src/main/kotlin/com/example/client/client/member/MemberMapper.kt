package com.example.client.client.member

import com.example.client.controller.member.dto.MemberDto
import com.example.proto.member.CreateMemberRequest
import com.example.proto.member.MemberIdRequest
import com.example.proto.member.createMemberRequest
import com.example.proto.member.memberIdRequest

object MemberMapper {
    fun ofCreateMemberRequest(request: MemberDto.CreateMemberRequest): CreateMemberRequest {
        return createMemberRequest {
            name = request.name
            createdBy = request.createdBy
        }
    }

    fun ofMemberIdRequest(request: Int): MemberIdRequest {
        return memberIdRequest {
            id = request
        }
    }
}