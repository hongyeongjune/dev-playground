package com.example.client.controller.member

import com.example.client.controller.member.dto.MemberDto
import com.example.client.service.member.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService,
) {
    @PostMapping("/members")
    suspend fun createMember(@RequestBody request: MemberDto.CreateMemberRequest): MemberDto.MemberResponse {
        return memberService.createMember(request)
    }

    @GetMapping("/members/{memberId}")
    suspend fun getMember(@PathVariable memberId: Int): MemberDto.MemberResponse {
        return memberService.getMember(memberId)
    }
}