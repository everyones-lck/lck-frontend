package every.lol.com.feature.community.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
@Composable
actual fun VideoPlayerView(url: String, modifier: Modifier) {
    val player = remember {
        val nsUrl = NSURL.URLWithString(url) ?: return@remember AVPlayer()
        AVPlayer(uRL = nsUrl)
    }

    val playerLayer = remember { AVPlayerLayer.playerLayerWithPlayer(player) }
    val viewController = remember { AVPlayerViewController() }

    DisposableEffect(Unit) {
        viewController.player = player
        player.play() // 자동 재생

        onDispose {
            player.pause()
            viewController.player = null
        }
    }

    UIKitView(
        factory = {
            val container = UIView()
            container.addSubview(viewController.view)
            viewController.view.setFrame(container.bounds)
            container
        },
        onResize = { view, rect ->
            // 화면 크기 변경 대응 (Compose 레이아웃에 맞춤)
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.layer.setFrame(rect)
            viewController.view.setFrame(rect)
            CATransaction.commit()
        },
        modifier = modifier
    )
}