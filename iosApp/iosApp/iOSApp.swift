import SwiftUI

@main
struct iOSApp: App {
    init() {
        PhotoPickerBridge.shared.install()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}