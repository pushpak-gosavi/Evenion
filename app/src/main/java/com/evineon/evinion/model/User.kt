package com.evineon.evinion.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.evineon.evinion.Constants.Companion.USER_TABLE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = USER_TABLE)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val firstName:String,
    val lastName: String,
    val introduction:String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val  image:ByteArray
):Parcelable