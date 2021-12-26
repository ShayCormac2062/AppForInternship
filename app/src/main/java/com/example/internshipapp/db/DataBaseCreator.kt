package com.example.internshipapp.db

import android.app.Activity
import androidx.room.Room

class DataBaseCreator {

    fun createDBInstance(activity: Activity): AreaDatabase { // TaskDao AchievementDao
        return Room.databaseBuilder(activity, AreaDatabase::class.java, "dao")
            .allowMainThreadQueries()
            .build()
    }

    fun initDB(activity: Activity): AreaDatabase {
        val db = createDBInstance(activity)
        return db
    }
}