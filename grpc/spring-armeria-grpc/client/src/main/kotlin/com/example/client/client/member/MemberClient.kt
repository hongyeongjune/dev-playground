package com.example.client.client.member

import com.example.client.controller.member.dto.MemberDto
import com.example.proto.member.MemberHandlerGrpcKt
import org.springframework.stereotype.Component

@Component
class MemberClient(
    private val memberServiceStub: MemberHandlerGrpcKt.MemberHandlerCoroutineStub,
) {
    suspend fun createMember(request: MemberDto.CreateMemberRequest): MemberDto.MemberResponse {
        return memberServiceStub.createMember(MemberMapper.ofCreateMemberRequest(request))
            .let { MemberDto.MemberResponse.of(it) }
    }

    suspend fun getMember(request: Int): MemberDto.MemberResponse {
        return memberServiceStub.getMember(MemberMapper.ofMemberIdRequest(request))
            .let { MemberDto.MemberResponse.of(it) }
    }
}