package com.example.anko

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper private constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {
    init {
        instance = this
    }

    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: MyDatabaseOpenHelper(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("Customer", true, 
                    "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                    "name" to TEXT,
                    "photo" to BLOB)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("User", true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(this)

data class User(var id:Int,var name:String,var mail:String)

