package com.example.captioncraft.ui.screens.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.captioncraft.R
import com.example.captioncraft.data.models.Post
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.captioncraft.ui.Screen

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val userPosts by viewModel.userPosts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentUser?.username ?: "Username",
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                IconButton(onClick = { viewModel.navigateToSettings(navController) }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
                IconButton(onClick = { viewModel.logout(navController) }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                currentUser?.avatarUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = currentUser?.username ?: "Username",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    ProfileStat("Posts", userPosts.size)
                    ProfileStat("Followers", currentUser?.followers?.size ?: 0)
                    ProfileStat("Following", currentUser?.following?.size ?: 0)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Navigate to Edit Profile */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profile")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "My Posts",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(userPosts) { post ->
                PostThumbnail(post)
            }
        }
    }
}


@Composable
fun ProfileStat(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), style = MaterialTheme.typography.bodyLarge)
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun PostThumbnail(post: Post) {
    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        AsyncImage(
            model = post.imageUrl,
            contentDescription = "Post Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
