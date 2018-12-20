package com.canbot.stdlibkt

import org.junit.Test
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Created by tao.liu on 2018/11/23.
 * description this is description
 */
class UnitTest01 {
        @Test
        fun fun1() {

        }

        fun foo(string: String?) {
               /* if (string.isNotNull()) {
                        string.length
                }*/
        }


        //fun String?.isNotNull() = this != null

       /* @ExperimentalContracts
        fun String?.isNotNull(): Boolean {
                contract {
                        returns(true) implies(this@isNotNull != null)
                }
                return this != null
        }

        @ExperimentalContracts
        fun String?.isNullOrEmpty(): Boolean {
                contract { returns(false) implies (this@isNullOrEmpty != null) }
                return this == null || isEmpty()
        }*/
}