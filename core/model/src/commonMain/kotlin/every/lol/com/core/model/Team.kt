package every.lol.com.core.model

enum class Team(
    val id: Int,
    val teamName: String
) {
    GEN(1,"GEN"),
    T1(2, "T1"),
    NS(3, "NS"),
    DNS(4,"DNS"),
    BRO(5, "BRO"),
    BFX(6, "BFX"),
    DK(7, "DK"),
    KRX(8, "KRX"),
    KT(9, "KT"),
    HLE(10,"HLE"),
    NONE(0, "선택 안함");

    companion object {
        fun fromId(id: Int?): Team =
            entries.find { it.id == id } ?: NONE

        fun fromTeamName(teamName: String?): Team =
            entries.find { it.teamName == teamName } ?: NONE
    }
}