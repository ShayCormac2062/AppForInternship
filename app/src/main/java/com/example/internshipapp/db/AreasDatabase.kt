package com.example.internshipapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.internshipapp.db.dao.AreaDao
import com.example.internshipapp.entity.Area

@Database(
    entities = [Area::class],
    version = 2,
)
@TypeConverters(AreaDao.ArrayConvertors::class)
abstract class AreaDatabase : RoomDatabase() {
    abstract fun areaDao(): AreaDao
}