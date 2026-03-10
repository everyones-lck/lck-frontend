//
// Created by 김기찬 on 2026. 3. 11..
//

import Foundation
import PhotosUI
import UIKit

private let openImagePickerNotification = Notification.Name("EverylolOpenImagePicker")
private let imagePickerResultNotification = Notification.Name("EverylolImagePickerResult")
private let keyRequestId = "requestId"
private let keyImagePath = "imagePath"

final class PhotoPickerBridge: NSObject {
    static let shared = PhotoPickerBridge()

    private var currentRequestId: String?
    private var pickerWindow: UIWindow?

    func install() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(openPicker(_:)),
            name: openImagePickerNotification,
            object: nil
        )
    }

    @objc
    private func openPicker(_ notification: Notification) {
        guard let requestId = notification.userInfo?[keyRequestId] as? String else {
            return
        }
        currentRequestId = requestId

        DispatchQueue.main.async {
            guard let scene = UIApplication.shared.connectedScenes
                .compactMap({ $0 as? UIWindowScene })
                .first(where: { $0.activationState == .foregroundActive }) else {
                self.postResult(path: nil)
                return
            }

            let hostVC = UIViewController()
            hostVC.view.backgroundColor = .clear

            let window = UIWindow(windowScene: scene)
            window.rootViewController = hostVC
            window.windowLevel = .alert + 1
            window.isHidden = false
            window.makeKeyAndVisible()

            self.pickerWindow = window

            var config = PHPickerConfiguration()
            config.selectionLimit = 1
            config.filter = .images

            let picker = PHPickerViewController(configuration: config)
            picker.delegate = self
            hostVC.present(picker, animated: true)
        }
    }

    private func cleanupPickerWindow() {
        pickerWindow?.isHidden = true
        pickerWindow = nil
    }

    private func postResult(path: String?) {
        guard let requestId = currentRequestId else {
            cleanupPickerWindow()
            return
        }

        NotificationCenter.default.post(
            name: imagePickerResultNotification,
            object: nil,
            userInfo: [
                keyRequestId: requestId,
                keyImagePath: path as Any
            ]
        )

        currentRequestId = nil
        cleanupPickerWindow()
    }
}

extension PhotoPickerBridge: PHPickerViewControllerDelegate {
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        picker.dismiss(animated: true) {
            guard let result = results.first else {
                self.postResult(path: nil)
                return
            }

            let provider = result.itemProvider
            let typeIdentifier = "public.image"

            provider.loadFileRepresentation(forTypeIdentifier: typeIdentifier) { [weak self] url, error in
                guard let self else { return }

                guard let url else {
                    DispatchQueue.main.async {
                        self.postResult(path: nil)
                    }
                    return
                }

                let tempDir = FileManager.default.temporaryDirectory
                let fileName = UUID().uuidString + "_" + url.lastPathComponent
                let localURL = tempDir.appendingPathComponent(fileName)

                do {
                    if FileManager.default.fileExists(atPath: localURL.path) {
                        try FileManager.default.removeItem(at: localURL)
                    }
                    try FileManager.default.copyItem(at: url, to: localURL)

                    DispatchQueue.main.async {
                        self.postResult(path: localURL.path)
                    }
                } catch {
                    DispatchQueue.main.async {
                        self.postResult(path: nil)
                    }
                }
            }
        }
    }
}