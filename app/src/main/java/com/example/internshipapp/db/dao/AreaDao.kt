package com.example.internshipapp.db.dao

import androidx.room.*
import com.example.internshipapp.entity.Area
import com.google.gson.Gson
import kotlin.collections.ArrayList

@Dao
interface AreaDao {

    class ArrayConvertors {

        @TypeConverter
        fun fromList(areas: List<Area>?): String = Gson().toJson(areas)

        @TypeConverter
        fun toList(value: String?) = Gson().fromJson(value, Array<Area>::class.java).toList()

    }

    @Query("SELECT * FROM 'regions'")
    fun getAll(): List<Area>

    @Query("SELECT * FROM 'regions' WHERE parent_id IS NULL")
    fun getAllParents(): List<Area>

    @Query("SELECT * FROM 'regions' WHERE name=:name")
    fun getByName(name: String?): Area

    @Insert
    fun add(area: Area)

    @Query("UPDATE regions SET parent_id=:parentId WHERE (id=:id)")
    fun updateParentId(id: Int, parentId: Int?)

    @Query("UPDATE regions SET name=:name WHERE (id=:id)")
    fun updateName(id: Int, name: String)

    @Query("SELECT * from 'regions' WHERE id=:id")
    fun getArea(id: Int): Area

    @Query("UPDATE regions SET 'subregions'=:subregions WHERE (id=:id)")
    fun updateAreas(id: Int, subregions: List<Area>)

    @Query("SELECT * FROM 'regions' WHERE parent_id=:id")
    fun getAllSubregions(id: Int): List<Area>

    @Query("DELETE FROM regions WHERE (id=:id)")
    fun deleteArea(id: Int)

    @Query("DELETE FROM regions")
    fun deleteAll()
}