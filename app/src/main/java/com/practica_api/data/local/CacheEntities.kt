package com.practica_api.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_cache")
data class CharacterCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val job: String?,
    val height: String?,
    val age: String?,
    val bounty: Long?,
    val crewName: String?,
    val isYonko: Boolean?,
    val fruitName: String?,
    val fruitImageUrl: String?,
    val status: String
)

@Entity(tableName = "fruit_cache")
data class FruitCacheEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val romanName: String?,
    val type: String,
    val description: String?,
    val imageUrl: String?
)

@Entity(tableName = "saga_cache")
data class SagaCacheEntity(
    @PrimaryKey val id: Int,
    val number: String,
    val title: String,
    val chapters: String,
    val volumes: String,
    val episodes: String
)
