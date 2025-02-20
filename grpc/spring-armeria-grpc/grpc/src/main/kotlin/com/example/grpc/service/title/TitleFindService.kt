package com.example.grpc.service.title

import com.example.grpc.exception.ResourceNotFoundException
import com.example.grpc.repository.title.TitleRepository
import com.example.grpc.service.title.dto.TitleDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TitleFindService(
    private val titleRepository: TitleRepository,
) {
    @Transactional(readOnly = true)
    suspend fun getTitle(id: Int): TitleDto.TitleResponse {
        val titleEntity = titleRepository.findById(id) ?: throw ResourceNotFoundException.of("title")
        return TitleDto.TitleResponse.of(titleEntity)
    }

    @Transactional(readOnly = true)
    suspend fun getTitlesByTitleGroupId(titleGroupId: Int): List<TitleDto.TitleResponse> {
        val titleEntityList = titleRepository.findByTitleGroupId(titleGroupId) ?: throw ResourceNotFoundException.of("title")
        return titleEntityList.map { TitleDto.TitleResponse.of(it) }
    }
}