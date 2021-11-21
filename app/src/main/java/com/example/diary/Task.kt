package com.example.diary

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Task(

    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    var date_start: String = "",
    var date_finish: String = "",
    var name: String = "",
    var description: String = "",
): RealmObject()