package fr97.movieinfo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenreEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
)