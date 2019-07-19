package com.example.anko

import android.os.Bundle
import android.provider.Contacts
import android.provider.Contacts.Intents.UI
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.doAsync

/**
 * 这个activity学习 anko sqlite
 */
class SomeOtherActivity : AppCompatActivity() ,AnkoLogger{
        private var id: Int = 0
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_some_other)
        }


        private fun loadAsync() {
//                doAsync(UI) {
//                        val result = bg {
//                                database.use {
//                                        // `this` is a SQLiteDatabase instance
//                                }
//                        }
//                        //loadComplete(result)
//                }
        }

        fun createTable(view: View) {
                database.use {
                        createTable("User", true,
                                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                                "name" to TEXT,
                                "mail" to TEXT)
                }
        }

        fun insertData(view: View) {
                /* var db = database.writableDatabase
                 val values = ContentValues()
                 values.put("id", 5)
                 values.put("name", "John Smith")
                 values.put("email", "user@domain.org")
                 db.insert("User", null, values)*/
                debug("插入数据$id")
                id++
                database.use {
                        insert("User",
                                "id" to 41,
                                "name" to "John",
                                "mail" to "user@domain.org"
                        )
                }
        }

        fun queryData(view: View) {
                debug("查询数据")
                val parser = rowParser { id: Int, name: String, email: String ->
                        Triple(id, name, email)
                }
                database.use {
                        /*val list = select("User", "name")
                                .whereArgs("(id > {userId})",
                                        "userId" to 1)
                                .exec {
                                        parseList(parser)

                                }*/

                        var list = select("User").exec {
                                parseList(classParser<User>())

                        }




                        println("数据是："+list)


                }
        }

        fun updateData(view: View){
                database.use {
                        /*update("User", "name" to "Alice")
                                .where("id = {userId}", "userId" to 42)
                                .exec()*/

                        update("User", "name" to "Alice")
                                .whereSimple("id = ?", 1.toString())
                                .exec()
                }
        }

        fun delData(view: View){
                database.use {
                        val numRowsDeleted = delete("User", "id = {userID}", "userID" to 41)
                        println("删除数据"+numRowsDeleted)
                }
        }
}
