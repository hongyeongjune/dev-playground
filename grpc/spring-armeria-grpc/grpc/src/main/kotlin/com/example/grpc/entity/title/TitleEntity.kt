package com.example.grpc.entity.title

import com.example.grpc.service.title.dto.TitleDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("title")
class TitleEntity(
    @Id
    val id: Int? = null,
    val subject: String,
    val description: String? = null,
    val titleGroupId: Int,
    val createdBy: Int,
    val modifiedBy: Int? = null,
    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @LastModifiedDate
    val modifiedDate: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun of(request: TitleDto.CreateTitleRequest): TitleEntity {
            return with(request) {
                TitleEntity(
                    subject = subject,
                    description = description,
                    titleGroupId = titleGroupId,
                    createdBy = createdBy,
                )
            }
        }
    }
}