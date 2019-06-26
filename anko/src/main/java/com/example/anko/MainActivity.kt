package com.example.anko

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity(),AnkoLogger {
        private val log = AnkoLogger(this.javaClass)
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                //这里使用anko的logging打印日志
                //Anko Commons – Logging
                //info{"zheshi"}
                //debug("fjjjj ")
                //info("")

                //log.error("")
        }

        /**
         * 这里使用anko 的intents相关的扩展函数
         */
        fun nextActivity(view: View){
//                val intent = Intent(this, SomeOtherActivity::class.java)
//                intent.putExtra("id", 5)
//                intent.setFlag(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                startActivity(intent)

//                startActivity(intentFor<SomeOtherActivity>("id" to 5).singleTop())


//                startActivity<SomeOtherActivity>("id" to 5)

                startActivity<SomeOtherActivity>(
                        "id" to 5,
                        "city" to "Denpasar"
                )

        }

        fun showToast(){
                toast("Hi there!")
                toast(R.string.app_name)
                longToast("Wow, such duration")
        }

        fun alerts(){
                /*view.snackbar("Hi there!")
                view.snackbar(R.string.message)
                view.longSnackbar("Wow, such duration")
                view.snackbar("Action, reaction", "Click me!") { doStuff() }  */

                alert("Hi, I'm Roy", "Have you tried turning it off and on again?") {
                        yesButton { toast("Oh…") }
                        noButton {}
                }.show()

                //alert(Appcompat, "Some text message").show()

                val countries = listOf("Russia", "USA", "Japan", "Australia")
                selector("Where are you from?", countries, { dialogInterface, i ->
                        toast("So you're living in ${countries[i]}, right?")
                })
        }


}
