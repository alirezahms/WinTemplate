package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VpnDao {
    // Selected Apps queries
    @Query("SELECT * FROM selected_apps ORDER BY appName ASC")
    fun getSelectedApps(): Flow<List<SelectedApp>>

    @Query("SELECT packageName FROM selected_apps")
    suspend fun getSelectedPackageNames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedApp(app: SelectedApp)

    @Delete
    suspend fun deleteSelectedApp(app: SelectedApp)

    @Query("DELETE FROM selected_apps")
    suspend fun clearSelectedApps()

    // App Settings queries
    @Query("SELECT value FROM app_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettingValue(key: String): String?

    @Query("SELECT value FROM app_settings WHERE `key` = :key LIMIT 1")
    fun getSettingValueFlow(key: String): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: AppSetting)
}
