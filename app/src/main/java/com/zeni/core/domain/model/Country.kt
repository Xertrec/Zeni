package com.zeni.core.domain.model

enum class Country(val phonePrefix: String, val flagUnicode: String) {
    ARGENTINA("+54", "\uD83C\uDDE6\uD83C\uDDF7"),
    AUSTRALIA("+61", "\uD83C\uDDE6\uD83C\uDDFA"),
    BRAZIL("+55", "\uD83C\uDDE7\uD83C\uDDF7"),
    CANADA("+1", "\uD83C\uDDE8\uD83C\uDDE6"),
    CHINA("+86", "\uD83C\uDDE8\uD83C\uDDF3"),
    FRANCE("+33", "\uD83C\uDDEB\uD83C\uDDF7"),
    GERMANY("+49", "\uD83C\uDDE9\uD83C\uDDEA"),
    INDIA("+91", "\uD83C\uDDEE\uD83C\uDDF3"),
    INDONESIA("+62", "\uD83C\uDDEE\uD83C\uDDE9"),
    ITALY("+39", "\uD83C\uDDEE\uD83C\uDDF9"),
    JAPAN("+81", "\uD83C\uDDEF\uD83C\uDDF5"),
    MEXICO("+52", "\uD83C\uDDF2\uD83C\uDDFD"),
    RUSSIA("+7", "\uD83C\uDDF7\uD83C\uDDFA"),
    SAUDI_ARABIA("+966", "\uD83C\uDDF8\uD83C\uDDE6"),
    SOUTH_AFRICA("+27", "\uD83C\uDDFF\uD83C\uDDE6"),
    SOUTH_KOREA("+82", "\uD83C\uDDF0\uD83C\uDDF7"),
    SPAIN("+34", "\uD83C\uDDEA\uD83C\uDDF8"),
    TURKEY("+90", "\uD83C\uDDF9\uD83C\uDDF7"),
    UNITED_KINGDOM("+44", "\uD83C\uDDEC\uD83C\uDDE7"),
    UNITED_STATES("+1", "\uD83C\uDDFA\uD83C\uDDF8");
}
