package com.zeni.hotel.domain.model

import com.zeni.R

enum class City(val cityName: String, val translatableText: Int) {
    BARCELONA("barcelona", R.string.city_barcelona),
    PARIS("paris", R.string.city_paris),
    LONDRES("londres", R.string.city_londres),
}