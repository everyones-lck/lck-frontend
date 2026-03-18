//
// Created by 김기찬 on 2026. 3. 16..
//

import Foundation
import KakaoSDKUser

final class KakaoLoginManager {
    static let shared = KakaoLoginManager()

    func login(completion: @escaping (Result<String, Error>) -> Void) {
        let finishWithUserId: (Error?) -> Void = { error in
            if let error = error {
                completion(.failure(error))
            } else {
                UserApi.shared.me { user, meError in
                    if let meError = meError {
                        completion(.failure(meError))
                    } else if let user = user, let id = user.id {
                        completion(.success(String(id)))
                    } else {
                        completion(.failure(NSError(domain: "KakaoLogin", code: -1)))
                    }
                }
            }
        }

        if UserApi.isKakaoTalkLoginAvailable() {
            UserApi.shared.loginWithKakaoTalk { _, error in
                if let error = error {
                    finishWithUserId(error)
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