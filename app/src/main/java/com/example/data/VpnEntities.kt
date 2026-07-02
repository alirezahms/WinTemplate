package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_apps")
data class SelectedApp(
    @PrimaryKey val packageName: String,
    val appName: String
)

@Entity(tableName = "app_settings")
data class AppSetting(
    @PrimaryKey val key: String,
    val value: String
)
