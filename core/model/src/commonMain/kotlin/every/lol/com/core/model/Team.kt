package every.lol.com.core.model

enum class Team(
    val id: Int,
    val teamName: String
) {
    GEN(2,"GEN"),
    T1(1, "T1"),
    NS(7, "NS"),
    DNS(10,"DNS"),
    BRO(8, "BRO"),
    BFX(9, "BFX"),
    DK(3, "DK"),
    DRX(6, "DRX"),
    KT(4, "KT"),
    HLE(5,"HLE"),
    NONE(0, "선택 안함");

    companion object {
        fun fromId(id: Int?): Team =
            entries.find { it.id == id } ?: NONE

        fun fromTeamName(teamName: String?): Team =
            entries.find { it.teamName == teamName } ?: NONE
    }
}