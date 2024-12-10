package com.example.tvapplicationpaging3.dao

import androidx.room.Entity

@Entity(tableName = "remote_keys")
data class RemoteKey(val label: String, val nextKey: String?)