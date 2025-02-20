package com.example.grpc.service.title

import com.example.grpc.entity.title.TitleEntity
import com.example.grpc.repository.title.TitleRepository
import com.example.grpc.service.title.dto.TitleDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TitleSaveService(
    private val titleRepository: TitleRepository,
) {
    @Transactional
    suspend fun createTitle(request: TitleDto.CreateTitleRequest): TitleDto.TitleResponse {
        val titleEntity = titleRepository.save(TitleEntity.of(request))
        return TitleDto.TitleResponse.of(titleEntity)
    }
}