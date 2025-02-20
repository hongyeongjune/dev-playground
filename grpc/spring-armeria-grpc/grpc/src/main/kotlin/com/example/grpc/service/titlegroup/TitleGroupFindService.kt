package com.example.grpc.service.titlegroup

import com.example.grpc.exception.ResourceNotFoundException
import com.example.grpc.repository.titlegroup.TitleGroupRepository
import com.example.grpc.service.titlegroup.dto.TitleGroupDto
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TitleGroupFindService(
    private val titleGroupRepository: TitleGroupRepository,
) {
    @Transactional(readOnly = true)
    suspend fun getTitleGroup(id: Int): TitleGroupDto.TitleGroupResponse {
        val titleGroupEntity = titleGroupRepository.findById(id) ?: throw ResourceNotFoundException("titlegroup")
        return TitleGroupDto.TitleGroupResponse.of(titleGroupEntity)
    }

    @Transactional(readOnly = true)
    suspend fun getTitleGroups(): List<TitleGroupDto.TitleGroupResponse> {
        val titleGroupEntityList = titleGroupRepository.findAll().toList()
        return titleGroupEntityList.map { TitleGroupDto.TitleGroupResponse.of(it) }
    }
}