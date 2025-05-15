package com.zeni.core.data.remote.dto.responses

data class ResponseBody(
    val loc: Array<Any>,
    val message: String,
    val type: String?
)