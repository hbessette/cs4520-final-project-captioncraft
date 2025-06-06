package com.example.captioncraft.data.local.dao
import androidx.room.*
import com.example.captioncraft.data.local.entity.CaptionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Dao
interface CaptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaption(caption: CaptionEntity)

    @Query("SELECT * FROM Caption WHERE id = :id")
    fun getCaptionById(id: Int): Flow<CaptionEntity?>

    @Query("SELECT * FROM Caption WHERE postId = :postId ORDER BY createdAt ASC")
    fun getCaptionsByPost(postId: Int): Flow<List<CaptionEntity>>

    @Query("SELECT * FROM Caption WHERE userId = :userId ORDER BY createdAt DESC")
    fun getCaptionsByUser(userId: Int): Flow<List<CaptionEntity>>

    @Update
    suspend fun updateCaption(caption: CaptionEntity)

    @Delete
    suspend fun deleteCaption(caption: CaptionEntity)

    @Query("SELECT * FROM Caption WHERE postId = :postId ORDER BY createdAt ASC")
    suspend fun getCaptionsByPostOnce(postId: Int): List<CaptionEntity>

    @Query("DELETE FROM Caption WHERE postId = :postId")
    suspend fun deleteCaptionsByPost(postId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaptions(captions: List<CaptionEntity>)

}
