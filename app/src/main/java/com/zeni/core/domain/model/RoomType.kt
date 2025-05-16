package com.zeni.core.domain.model

import com.zeni.R

enum class RoomType(val text: Int) {
    SINGLE(R.string.single_room_text),
    DOUBLE(R.string.double_room_text),
    SUITE(R.string.suite_room_text)
}
