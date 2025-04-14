package com.example.captioncraft.data.repository

import com.example.captioncraft.data.models.Caption
import com.example.captioncraft.data.models.Post
import com.example.captioncraft.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    fun login(username: String) {
        val user = User(
            id = UUID.randomUUID().toString(),
            username = username
        )
        _currentUser.value = user
        _users.update { currentUsers ->
            if (currentUsers.none { it.id == user.id }) {
                currentUsers + user
            } else {
                currentUsers
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun createPost(imageUrl: String) {
        val currentUser = _currentUser.value ?: return
        val post = Post(
            id = UUID.randomUUID().toString(),
            imageUrl = imageUrl,
            userId = currentUser.id
        )
        _posts.update { it + post }
    }

    fun addCaption(postId: String, text: String) {
        val currentUser = _currentUser.value ?: return
        val caption = Caption(
            id = UUID.randomUUID().toString(),
            text = text,
            userId = currentUser.id
        )
        _posts.update { posts ->
            posts.map { post ->
                if (post.id == postId) {
                    post.copy(captions = post.captions + caption)
                } else {
                    post
                }
            }
        }
    }

    fun toggleCaptionLike(postId: String, captionId: String) {
        val currentUser = _currentUser.value ?: return
        _posts.update { posts ->
            posts.map { post ->
                if (post.id == postId) {
                    post.copy(
                        captions = post.captions.map { caption ->
                            if (caption.id == captionId) {
                                if (caption.likes.contains(currentUser.id)) {
                                    caption.copy(likes = caption.likes - currentUser.id)
                                } else {
                                    caption.copy(likes = caption.likes + currentUser.id)
                                }
                            } else {
                                caption
                            }
                        }
                    )
                } else {
                    post
                }
            }
        }
    }

    fun searchUsers(query: String): List<User> {
        return _users.value.filter { 
            it.username.contains(query, ignoreCase = true) 
        }
    }

    fun toggleFollow(userId: String) {
        val currentUser = _currentUser.value ?: return
        _users.update { users ->
            users.map { user ->
                when (user.id) {
                    currentUser.id -> {
                        if (user.following.contains(userId)) {
                            user.copy(following = user.following - userId)
                        } else {
                            user.copy(following = user.following + userId)
                        }
                    }
                    userId -> {
                        if (user.followers.contains(currentUser.id)) {
                            user.copy(followers = user.followers - currentUser.id)
                        } else {
                            user.copy(followers = user.followers + currentUser.id)
                        }
                    }
                    else -> user
                }
            }
        }
        _currentUser.update { it?.let { currentUser ->
            if (currentUser.following.contains(userId)) {
                currentUser.copy(following = currentUser.following - userId)
            } else {
                currentUser.copy(following = currentUser.following + userId)
            }
        }}
    }

    fun getUserPosts(userId: String): List<Post> {
        return _posts.value.filter { it.userId == userId }
    }
} 