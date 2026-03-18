package every.lol.com.core.network.datasource

interface IOSKakaoLoginCallback {
    fun onSuccess(userId: String)
    fun onFailure(message: String)
}

interface IOSKakaoLoginBridge {
    fun loginWithKakao(callback: IOSKakaoLoginCallback)
}

object IOSKakaoLoginBridgeProvider {
    var bridge: IOSKakaoLoginBridge? = null
}