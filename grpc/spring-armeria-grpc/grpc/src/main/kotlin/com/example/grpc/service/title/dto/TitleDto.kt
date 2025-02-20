package com.example.grpc.service.title.dto

import com.example.grpc.entity.title.TitleEntity
import com.example.proto.title.CreateTitleRequest
import com.example.proto.title.descriptionOrNull
import java.time.LocalDateTime

object TitleDto {
    data class CreateTitleRequest(
        val subject: String,
        val description: String? = null,
        val titleGroupId: Int,
        val createdBy: Int,
    ) {
        companion object {
            fun of(createTitleRequest: com.example.proto.title.CreateTitleRequest): CreateTitleRequest {
                return with(createTitleRequest) {
                    CreateTitleRequest(
                        subject = subject,
                        description = descriptionOrNull?.value,
                        titleGroupId = titleGroupId,
                        createdBy = createdBy
                    )
                }
            }
        }
    }

    data class TitleResponse(
        val id: Int,
        val subject: String,
        val description: String? = null,
        val titleGroupId: Int,
        val createdBy: Int,
        val modifiedBy: Int? = null,
        val createdDate: LocalDateTime,
        val modifiedDate: LocalDateTime,
    ) {
        companion object {
            fun of(titleEntity: TitleEntity): TitleResponse {
                return with(titleEntity) {
                    TitleResponse(
                        id = id!!,
                        subject = subject,
                        description = description,
                        titleGroupId = titleGroupId,
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