package com.example.grpc.repository.member

import com.example.grpc.entity.member.MemberEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : CoroutineCrudRepository<MemberEntity, Int>