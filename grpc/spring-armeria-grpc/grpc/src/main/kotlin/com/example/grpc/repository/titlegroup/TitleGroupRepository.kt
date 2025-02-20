package com.example.grpc.repository.titlegroup

import com.example.grpc.entity.titlegroup.TitleGroupEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TitleGroupRepository : CoroutineCrudRepository<TitleGroupEntity, Int>