package com.example.captioncraft.data.repository

import com.example.captioncraft.data.models.Post
import com.example.captioncraft.data.models.User
import com.example.captioncraft.data.models.Caption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostRepository {
    private val dummyUsers = listOf(
        User(
            id = "1",
            username = "john_doe",
            avatarUrl = "https://i.pravatar.cc/150?img=1",
            followers = listOf("2", "3"),
            following = listOf("2", "3")
        ),
        User(
            id = "2",
            username = "jane_smith",
            avatarUrl = "https://i.pravatar.cc/150?img=2",
            followers = listOf("1", "3"),
            following = listOf("1", "3")
        ),
        User(
            id = "3",
            username = "mike_wilson",
            avatarUrl = "https://i.pravatar.cc/150?img=3",
            followers = listOf("1", "2"),
            following = listOf("1", "2")
        )
    )

    private val dummyPosts = listOf(
        Post(
            id = "1",
            userId = "1",
            imageUrl = "https://picsum.photos/800/800?random=1",
            captions = listOf(
                Caption(
                    id = "c1",
                    text = "Beautiful sunset at the beach 🌅 #photography #nature",
                    userId = "1",
                    likes = setOf("2", "3")
                )
            )
        ),
        Post(
            id = "2",
            userId = "2",
            imageUrl = "https://picsum.photos/800/800?random=2",
            captions = listOf(
                Caption(
                    id = "c2",
                    text = "New digital art piece 🎨 #art #digitalart",
                    userId = "2",
                    likes = setOf("1", "3")
                )
            )
        ),
        Post(
            id = "3",
            userId = "3",
            imageUrl = "https://picsum.photos/800/800?random=3",
            captions = listOf(
                Caption(
                    id = "c3",
                    text = "Exploring the mountains 🏔️ #travel #adventure",
                    userId = "3",
                    likes = setOf("1", "2")
                )
            )
        ),
        Post(
            id = "4",
            userId = "1",
            imageUrl = "https://picsum.photos/800/800?random=4",
            captions = listOf(
                Caption(
                    id = "c4",
                    text = "City lights 🌆 #urban #photography",
                    userId = "1",
                    likes = setOf("2", "3")
                )
            )
        ),
        Post(
            id = "5",
            userId = "2",
            imageUrl = "https://picsum.photos/800/800?random=5",
            captions = listOf(
                Caption(
                    id = "c5",
                    text = "Abstract digital creation 🎨 #abstract #art",
                    userId = "2",
                    likes = setOf("1", "3")
                )
            )
        )
    )

    private val _posts = MutableStateFlow(dummyPosts)
    private val _users = MutableStateFlow(dummyUsers)

    fun getFeedPosts(): Flow<List<Post>> = _posts.asStateFlow()

    fun getUserPosts(userId: String): Flow<List<Post>> = flow {
        emit(_posts.value.filter { it.userId == userId })
    }

    fun searchUsers(query: String): Flow<List<User>> = flow {
        emit(_users.value.filter { 
            it.username.contains(query, ignoreCase = true)
        })
    }

    fun getCurrentUser(): Flow<User?> = flow {
        emit(_users.value[0]) // For testing, always return the first user
    }

    fun addCaption(postId: String, text: String) {
        val currentPosts = _posts.value.toMutableList()
        val postIndex = currentPosts.indexOfFirst { it.id == postId }
        
        if (postIndex != -1) {
            val post = currentPosts[postIndex]
            val newCaption = Caption(
                id = "c${System.currentTimeMillis()}",
                text = text,
                userId = "1", // For testing, always use the first user
                likes = emptySet()
            )
            currentPosts[postIndex] = post.copy(
                captions = post.captions + newCaption
            )
            _posts.value = currentPosts
        }
    }

    fun toggleCaptionLike(postId: String, captionId: String) {
        val currentPosts = _posts.value.toMutableList()
        val postIndex = currentPosts.indexOfFirst { it.id == postId }
        
        if (postIndex != -1) {
            val post = currentPosts[postIndex]
            val captionIndex = post.captions.indexOfFirst { it.id == captionId }
            
            if (captionIndex != -1) {
                val caption = post.captions[captionIndex]
                val currentUserId = "1" // For testing, always use the first user
                val newLikes = if (currentUserId in caption.likes) {
                    caption.likes - currentUserId
                } else {
                    caption.likes + currentUserId
                }
                
                val updatedCaption = caption.copy(likes = newLikes)
                val updatedCaptions = post.captions.toMutableList()
                updatedCaptions[captionIndex] = updatedCaption
                
                currentPosts[postIndex] = post.copy(captions = updatedCaptions)
                _posts.value = currentPosts
            }
        }
    }
} 