package com.example.grpc.handler.member

import com.example.grpc.annotation.GrpcHandler
import com.example.grpc.service.member.MemberFindService
import com.example.grpc.service.member.MemberSaveService
import com.example.grpc.service.member.dto.MemberDto
import com.example.proto.member.CreateMemberRequest
import com.example.proto.member.MemberHandlerGrpcKt
import com.example.proto.member.MemberIdRequest
import com.example.proto.member.MemberResponse

@GrpcHandler
class MemberHandler(
    private val memberFindService: MemberFindService,
    private val memberSaveService: MemberSaveService,
) : MemberHandlerGrpcKt.MemberHandlerCoroutineImplBase() {
    override suspend fun getMember(request: MemberIdRequest): MemberResponse {
        return memberFindService.getMember(request.id)
            .let { MemberMapper.fromMemberResponse(it) }
    }

    override suspend fun createMember(request: CreateMemberRequest): MemberResponse {
        return memberSaveService.createMember(MemberDto.CreateMemberRequest.of(request))
            .let { MemberMapper.fromMemberResponse(it) }
    }
}