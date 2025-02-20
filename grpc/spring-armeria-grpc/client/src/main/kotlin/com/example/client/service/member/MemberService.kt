package com.example.client.service.member

import com.example.client.client.member.MemberClient
import com.example.client.controller.member.dto.MemberDto
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberClient: MemberClient,
) {
    suspend fun createMember(request: MemberDto.CreateMemberRequest): MemberDto.MemberResponse {
        return memberClient.createMember(request)
    }

    suspend fun getMember(id: Int): MemberDto.MemberResponse {
        return memberClient.getMember(id)
    }
}