//
// Created by 김기찬 on 2026. 3. 16..
//

import Foundation
import ComposeApp
import KakaoSDKUser
import KakaoSDKAuth

final class IOSKakaoLoginBridgeImpl: NSObject, IOSKakaoLoginBridge {

    func loginWithKakao(callback: IOSKakaoLoginCallback) {
        let finishWithUserId: (Error?) -> Void = { error in
            if let error = error {
                callback.onFailure(message: error.localizedDescription)
                return
            }

            UserApi.shared.me { user, meError in
                if let meError = meError {
                    callback.onFailure(message: meError.localizedDescription)
                } else if let userId = user?.id {
                    callback.onSuccess(userId: String(userId))
                } else {
                    callback.onFailure(message: "카카오 사용자 정보를 가져오지 못했습니다.")
                }
            }
        }

        if UserApi.isKakaoTalkLoginAvailable() {
            UserApi.shared.loginWithKakaoTalk { _, error in
                if let error = error {
                    UserApi.shared.loginWithKakaoAccount { _, accountError in
                        finishWithUserId(accountError ?? error)
                    }
                } else {
                    finishWithUserId(nil)
                }
            }
        } else {
            UserApi.shared.loginWithKakaoAccount { _, error in
                finishWithUserId(error)
            }
        }
    }
}