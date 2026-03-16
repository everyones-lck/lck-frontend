import SwiftUI
import ComposeApp
import KakaoSDKCommon
import KakaoSDKAuth

@main
struct iOSApp: App {
    init() {
        KoinBridge.shared.start()
        PhotoPickerBridge.shared.install()
        guard let appKey = Bundle.main.object(forInfoDictionaryKey: "KakaoAppKey") as? String,
              !appKey.isEmpty else {
            fatalError("KakaoAppKey not found in Info.plist")
        }

        KakaoSDK.initSDK(appKey: appKey)
        IOSKakaoLoginBridgeProvider.shared.bridge = IOSKakaoLoginBridgeImpl()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    if AuthApi.isKakaoTalkLoginUrl(url) {
                        _ = AuthController.handleOpenUrl(url: url)
                    }
                }
        }
    }
}