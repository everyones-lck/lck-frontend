import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinBridge.shared.start()
        PhotoPickerBridge.shared.install()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}