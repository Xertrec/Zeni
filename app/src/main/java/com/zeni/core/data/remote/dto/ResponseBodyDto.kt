package com.zeni.core.data.remote.dto

data class ResponseBodyDto(
    val loc: Array<Any>,
    val message: String,
    val type: String?
)