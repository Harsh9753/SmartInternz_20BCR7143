package com.example.week_three_app

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import javax.sql.DataSource

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTubeTheme {
                var source by remember {
                    mutableStateOf(
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                    )
                }

                Column {
                    VideoPlayer(source)
                    Button(onClick = {
                        // Elephant Dream by Blender Foundation
                        source = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                    }) {
                        Text("Another Video")
                    }
                }
            }
        }
    }
}
@Composable
fun VideosScreen(viewModel: VideosViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val exoPlayer = remember(context) { ExoPlayer.Builder(context).build() }
    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val playingItemIndex by viewModel.currentlyPlayingIndex.collectAsStateWithLifecycle()

    LazyColumn {
        itemsIndexed(items = videos, key = { _, video -> video.id }) { index, video ->
            VideoCard(
                videoItem = video,
                exoPlayer = exoPlayer,
                isPlaying = index == playingItemIndex,
                onClick = {
                    viewModel.onPlayVideoClick(exoPlayer.currentPosition, index)
                }
            )
        }
    }
}