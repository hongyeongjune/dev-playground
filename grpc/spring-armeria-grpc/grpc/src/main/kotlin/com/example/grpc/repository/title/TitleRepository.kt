package com.example.grpc.repository.title

import com.example.grpc.entity.title.TitleEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TitleRepository : CoroutineCrudRepository<TitleEntity, Int> {
    suspend fun findByTitleGroupId(titleGroupId: Int): List<TitleEntity>
}