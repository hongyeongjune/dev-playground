package com.example.grpc.service.member

import com.example.grpc.exception.ResourceNotFoundException
import com.example.grpc.repository.member.MemberRepository
import com.example.grpc.service.member.dto.MemberDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberFindService(
    private val memberRepository: MemberRepository,
) {
    @Transactional(readOnly = true)
    suspend fun getMember(id: Int): MemberDto.MemberResponse {
        val memberEntity = memberRepository.findById(id) ?: throw ResourceNotFoundException.of("member")
        return MemberDto.MemberResponse.of(memberEntity)
    }
}