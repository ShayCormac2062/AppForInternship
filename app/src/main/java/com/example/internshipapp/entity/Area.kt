package com.example.internshipapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "regions")
data class Area(

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    var id: Int,

    @ColumnInfo(name = "parent_id")
    @SerializedName("parent_id")
    @Expose
    var parentId: Int?,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    var name: String,

    @ColumnInfo(name = "subregions")
    @SerializedName("areas")
    @Expose
    var subregions: List<Area>?
)