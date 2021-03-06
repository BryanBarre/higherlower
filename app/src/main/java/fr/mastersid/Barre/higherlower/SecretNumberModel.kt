package fr.mastersid.Barre.higherlower

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.math.absoluteValue

/**
 *Created by Bryan BARRE on 18/02/2021.
 */
class SecretNumberModel(state : SavedStateHandle, private val turn:Int,private val max:Int):ViewModel () {

    val secretNumber = MutableLiveData(NO_SECRET_NUMBER)
    val nbTurn=MutableLiveData(NO_TURN)
    val checkResult = MutableLiveData(CheckResult.NO_GUESS)


    enum class CheckResult {
        LOWER, GREATER, EQUAL,NO_GUESS
    }


    fun chooseSecretNumber() {
        secretNumber.value=(1..max).random()
        nbTurn.value=turn

    }


    fun setNoGuess(){
        checkResult.value = CheckResult.NO_GUESS
    }

    fun check(number:Int) {
        when {
            secretNumber.value == NO_SECRET_NUMBER -> {
                checkResult.value = CheckResult.NO_GUESS
            }
            number < secretNumber.value.toString().toInt() -> {
                checkResult.value = CheckResult.GREATER
            }
            number > secretNumber.value.toString().toInt() -> {
                checkResult.value = CheckResult.LOWER
            }
            else -> {
                checkResult.value = CheckResult.EQUAL
            }
        }

        when {
            nbTurn.value!! > 0 -> {
                nbTurn.value = nbTurn.value?.minus(1)
            }
            nbTurn.value == 0 -> {
                nbTurn.value = 0
            }
        }
    }



    companion  object {
        const  val  NO_SECRET_NUMBER = -1
        const  val  NO_TURN = -1
    }
}