package every.lol.com

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform