package com.example.grpc.handler.titlegroup

import com.example.grpc.service.titlegroup.dto.TitleGroupDto
import com.example.grpc.utils.toTimestamp
import com.example.proto.titlegroup.TitleGroupListResponse
import com.example.proto.titlegroup.TitleGroupResponse
import com.example.proto.titlegroup.titleGroupListResponse
import com.example.proto.titlegroup.titleGroupResponse
import com.google.protobuf.Int32Value

object TitleGroupMapper {
    fun fromTitleGroupResponse(response: TitleGroupDto.TitleGroupResponse): TitleGroupResponse {
        return titleGroupResponse {
            id = response.id
            subject = response.subject
            createdBy = response.createdBy
            response.modifiedBy?.let { modifiedBy = Int32Value.of(it) }
            createdDate = response.createdDate.toTimestamp()
            modifiedDate = response.modifiedDate.toTimestamp()
        }
    }

    fun fromTitleGroupListResponse(responseList: List<TitleGroupDto.TitleGroupResponse>): TitleGroupListResponse {
        return titleGroupListResponse {
            titleGroup.addAll(responseList.map { fromTitleGroupResponse(it) })
        }
    }
}