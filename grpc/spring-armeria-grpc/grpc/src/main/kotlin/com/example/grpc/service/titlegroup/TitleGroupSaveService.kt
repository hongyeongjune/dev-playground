package com.example.grpc.service.titlegroup

import com.example.grpc.entity.titlegroup.TitleGroupEntity
import com.example.grpc.repository.titlegroup.TitleGroupRepository
import com.example.grpc.service.titlegroup.dto.TitleGroupDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TitleGroupSaveService(
    private val titleGroupRepository: TitleGroupRepository,
) {
    @Transactional
    suspend fun createTitleGroup(request: TitleGroupDto.CreateTitleGroupRequest): TitleGroupDto.TitleGroupResponse {
        val titleGroupEntity = titleGroupRepository.save(TitleGroupEntity.of(request))
        return TitleGroupDto.TitleGroupResponse.of(titleGroupEntity)
    }
}