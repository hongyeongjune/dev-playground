package com.example.grpc.handler.title

import com.example.grpc.service.title.dto.TitleDto
import com.example.grpc.utils.toTimestamp
import com.example.proto.title.TitleListResponse
import com.example.proto.title.TitleResponse
import com.example.proto.title.titleListResponse
import com.example.proto.title.titleResponse
import com.google.protobuf.Int32Value
import com.google.protobuf.StringValue

object TitleMapper {
    fun fromTitleResponse(response: TitleDto.TitleResponse): TitleResponse {
        return titleResponse {
            id = response.id
            titleGroupId = response.titleGroupId
            subject = response.subject
            response.description?.let { description = StringValue.of(it) }
            createdBy = response.createdBy
            response.modifiedBy?.let { modifiedBy = Int32Value.of(it) }
            createdDate = response.createdDate.toTimestamp()
            modifiedDate = response.modifiedDate.toTimestamp()
        }
    }

    fun fromTitleListResponse(responseList: List<TitleDto.TitleResponse>): TitleListResponse {
        return titleListResponse {
            title.addAll(responseList.map { fromTitleResponse(it) })
        }
    }
}