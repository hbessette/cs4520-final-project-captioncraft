package com.example.captioncraft.ui.screens.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.captioncraft.R
import com.example.captioncraft.data.local.entity.CaptionEntity
import com.example.captioncraft.domain.model.Caption
import com.example.captioncraft.domain.model.Post

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val posts by viewModel.feedPosts.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.syncFeedPosts()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts) { post ->
            PostCard(
                post = post,
                onAddCaption = { text -> viewModel.addCaption(post.id, text) },
                onLikeCaption = { captionId -> viewModel.toggleLike(captionId.toInt()) }
            )
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    onAddCaption: (String) -> Unit,
    onLikeCaption: (String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    var showAddCaption by remember { mutableStateOf(false) }
    var captionText by remember { mutableStateOf("") }
    val captions by viewModel.captions.collectAsState()

    LaunchedEffect(post.id) {
        viewModel.loadCaptions(post.id)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Post image
            AsyncImage(
                model = post.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            captions.maxByOrNull { it.likes }?.let { topCaption ->
                Text(
                    text = topCaption.text,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Caption list
            captions.forEach { caption ->
                CaptionItem(
                    caption = caption,
                    onLike = { onLikeCaption(caption.id.toString()) }
                )
            }

            // Add caption button/field
            if (showAddCaption) {
                OutlinedTextField(
                    value = captionText,
                    onValueChange = { captionText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.add_caption)) }
                )
                Button(
                    onClick = {
                        if (captionText.isNotBlank()) {
                            onAddCaption(captionText)
                            captionText = ""
                            showAddCaption = false
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.add_caption))
                }
            } else {
                OutlinedButton(
                    onClick = { showAddCaption = true },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.add_caption))
                }
            }
        }
    }
}

@Composable
fun CaptionItem(
    caption: Caption,
    onLike: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = caption.text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = caption.likes.toString(),
                style = MaterialTheme.typography.labelMedium
            )
            IconButton(onClick = onLike) {
                Icon(Icons.Default.ThumbUp, contentDescription = stringResource(R.string.like))
            }
        }
    }
}