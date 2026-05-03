package every.lol.com.feature.community.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.fragment.app.commit

@Composable
actual fun VideoPlayerView(url: String, modifier: Modifier, onReady: () -> Unit) {
    val player = remember {
        val nsUrl = NSURL.URLWithString(url) ?: return@remember AVPlayer()
        AVPlayer(uRL = nsUrl)
    }

    val playerLayer = remember { AVPlayerLayer.playerLayerWithPlayer(player) }
    val viewController = remember { AVPlayerViewController() }

    DisposableEffect(playerItem) {
        val observer = object : NSObject() {
            override fun observeValueForKeyPath(
                keyPath: String?,
                ofObject: Any?,
                change: Map<Any?, *>?,
                context: COpaquePointer?
            ) {
                if (keyPath == "status") {
                    val status = playerItem?.status ?: AVPlayerItemStatusUnknown
                    if (status == AVPlayerItemStatusReadyToPlay) {
                        dispatch_async(dispatch_get_main_queue()) {
                            onReady()
                        }
                    }
                }
            }
        }

        playerItem?.addObserver(
            observer = observer,
            forKeyPath = "status",
            options = NSKeyValueObservingOptionNew,
            context = null
        )

        viewController.player = player
        player.play()

        onDispose {
            player.pause()
            playerItem?.removeObserver(observer, forKeyPath = "status")
            viewController.player = null
        }
    }

    UIKitView(
        factory = {
            val container = UIView()
            container.addSubview(viewController.view)
            container
        },
        onResize = { view, rect ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.layer.setFrame(rect)
            viewController.view.setFrame(rect)
            CATransaction.commit()
        },
        modifier = modifier
    )
}