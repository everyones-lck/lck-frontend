package every.lol.com.core.model

enum class Team(
    val id: Int,
    val teamName: String
) {
    HLE(1,"Hanwha"),
    Gen(2, "Gen.G"),
    T1(3, "T1"),
    DNS(4,"DN"),
    BFX(5, "BNK"),
    NS(6, "RED"),
    DK(7, "DK"),
    DRX(8, "DRX"),
    BRO(9, "Brion"),
    KT(10,"Kt"),
    NONE(0, "선택 안함");

    companion object {
        fun fromId(id: Int?): Team =
            entries.find { it.id == id } ?: NONE
    }
}