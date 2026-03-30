package every.lol.com.core.model

enum class Team(
    val id: Int,
    val teamName: String
) {
    HLE(1,"HLE"),
    Gen(2, "GEN"),
    T1(3, "T1"),
    DNS(4,"DNS"),
    BFX(5, "BFX"),
    NS(6, "NS"),
    DK(7, "DK"),
    DRX(8, "DRX"),
    BRO(9, "BRO"),
    KT(10,"KT"),
    NONE(0, "선택 안함");

    companion object {
        fun fromId(id: Int?): Team =
            entries.find { it.id == id } ?: NONE

        fun fromTeamName(teamName: String?): Team =
            entries.find { it.teamName == teamName } ?: NONE
    }
}