package com.example.grpc.handler.titlegroup

import com.example.grpc.annotation.GrpcHandler
import com.example.grpc.service.titlegroup.TitleGroupFindService
import com.example.grpc.service.titlegroup.TitleGroupSaveService
import com.example.grpc.service.titlegroup.dto.TitleGroupDto
import com.example.proto.title.CreateTitleRequest
import com.example.proto.titlegroup.CreateTitleGroupRequest
import com.example.proto.titlegroup.TitleGroupHandlerGrpcKt
import com.example.proto.titlegroup.TitleGroupIdRequest
import com.example.proto.titlegroup.TitleGroupListResponse
import com.example.proto.titlegroup.TitleGroupResponse
import com.google.protobuf.Empty

@GrpcHandler
class TitleGroupHandler(
    private val titleGroupFindService: TitleGroupFindService,
    private val titleGroupSaveService: TitleGroupSaveService,
) : TitleGroupHandlerGrpcKt.TitleGroupHandlerCoroutineImplBase() {
    override suspend fun getTitleGroup(request: TitleGroupIdRequest): TitleGroupResponse {
        return titleGroupFindService.getTitleGroup(request.id)
            .let { TitleGroupMapper.fromTitleGroupResponse(it) }
    }

    override suspend fun getTitleGroups(request: Empty): TitleGroupListResponse {
        return titleGroupFindService.getTitleGroups()
            .let { TitleGroupMapper.fromTitleGroupListResponse(it) }
    }

    override suspend fun createTitleGroup(request: CreateTitleGroupRequest): TitleGroupResponse {
        return titleGroupSaveService.createTitleGroup(TitleGroupDto.CreateTitleGroupRequest.of(request))
            .let { TitleGroupMapper.fromTitleGroupResponse(it) }
    }
}