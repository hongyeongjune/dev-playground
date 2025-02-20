package com.example.grpc.handler.title

import com.example.grpc.annotation.GrpcHandler
import com.example.grpc.service.title.TitleFindService
import com.example.grpc.service.title.TitleSaveService
import com.example.grpc.service.title.dto.TitleDto
import com.example.proto.title.CreateTitleRequest
import com.example.proto.title.TitleGroupIdRequest
import com.example.proto.title.TitleHandlerGrpcKt
import com.example.proto.title.TitleIdRequest
import com.example.proto.title.TitleListResponse
import com.example.proto.title.TitleResponse

@GrpcHandler
class TitleHandler(
    private val titleFindService: TitleFindService,
    private val titleSaveService: TitleSaveService,
) : TitleHandlerGrpcKt.TitleHandlerCoroutineImplBase() {
    override suspend fun createTitle(request: CreateTitleRequest): TitleResponse {
        return titleSaveService.createTitle(TitleDto.CreateTitleRequest.of(request))
            .let { TitleMapper.fromTitleResponse(it) }
    }

    override suspend fun getTitle(request: TitleIdRequest): TitleResponse {
        return titleFindService.getTitle(request.id)
            .let { TitleMapper.fromTitleResponse(it) }
    }

    override suspend fun getTitleByTitleGroupId(request: TitleGroupIdRequest): TitleListResponse {
        return titleFindService.getTitlesByTitleGroupId(request.id)
            .let { TitleMapper.fromTitleListResponse(it) }
    }
}