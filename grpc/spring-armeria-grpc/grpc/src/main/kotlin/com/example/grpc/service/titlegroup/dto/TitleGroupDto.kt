package com.example.grpc.service.titlegroup.dto

import com.example.grpc.entity.titlegroup.TitleGroupEntity
import com.example.proto.titlegroup.CreateTitleGroupRequest
import java.time.LocalDateTime

object TitleGroupDto {
    data class CreateTitleGroupRequest(
        val subject: String,
        val createdBy: Int,
    ) {
        companion object {
            fun of(createTitleGroupRequest: com.example.proto.titlegroup.CreateTitleGroupRequest): CreateTitleGroupRequest {
                return with(createTitleGroupRequest) {
                    CreateTitleGroupRequest(
                        subject = subject,
                        createdBy = createdBy,
                    )
                }
            }
        }
    }

    data class TitleGroupResponse(
        val id: Int,
        val subject: String,
        val createdBy: Int,
        val modifiedBy: Int? = null,
        val createdDate: LocalDateTime,
        val modifiedDate: LocalDateTime,
    ) {
        companion object {
            fun of(titleGroupEntity: TitleGroupEntity): TitleGroupResponse {
                return with(titleGroupEntity) {
                    TitleGroupResponse(
                        id = id!!,
                        subject = subject,
                        createdBy = createdBy,
                        modifiedBy = modifiedBy,
                        createdDate = createdDate,
                        modifiedDate = modifiedDate,
                    )
                }
            }
        }
    }
}