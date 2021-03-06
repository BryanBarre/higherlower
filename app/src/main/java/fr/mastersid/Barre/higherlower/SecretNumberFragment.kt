package fr.mastersid.Barre.higherlower;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import fr.mastersid.Barre.higherlower.databinding.FragmentSecretnumberBinding

/**
 * Created by Bryan BARRE on 08/03/2021.
 */
class SecretNumberFragment: Fragment() {
    private lateinit var _binding : FragmentSecretnumberBinding
    override fun onCreateView (
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ): View? {
        _binding = FragmentSecretnumberBinding.inflate(inflater)
        return _binding.root
    }


    override fun onViewCreated ( view : View , savedInstanceState : Bundle ?) {
        //_binding.idGuessNumber.setTransformationMethod (null);//utilisé avec inputType password pour avoir uniquement des chiffres
        super.onViewCreated (view , savedInstanceState )
        val args : SecretNumberFragmentArgs by navArgs()
        val  secretNumberModel: SecretNumberModel by viewModels(
                factoryProducer = { SecretNumberfactory (this , args.turns ,args.max) })

        _binding.idButtonChooseNumber.setOnClickListener {
            secretNumberModel.chooseSecretNumber()
            secretNumberModel.setNoGuess()
            _binding.idTextView2.text =""
            _binding.idTextView4.text =""

        }
        _binding.idButtonCheck.setOnClickListener {
            if (secretNumberModel.secretNumber.value.toString()=="-1") {
                Toast.makeText(this.context, "First  press  \"Choose  secretnumber\"  button", Toast.LENGTH_SHORT).show()
            }
            else {
                try {
                    secretNumberModel.check(_binding.idGuessNumber.text.toString().toInt())
                } catch (error: NumberFormatException) {
                    Toast.makeText(this.context, "Incorrect number", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /////////////////////////////////observer/////////////////////////////////
        secretNumberModel.secretNumber.observe(viewLifecycleOwner) {///////////////////reproduire cette observer pour le textwiev 2
            value ->
            if (value == SecretNumberModel.NO_SECRET_NUMBER) {
                _binding.idTextView1.text =""
                _binding.idTextView2.text =""
                _binding.idTextView3.text=""
                _binding.idTextView4.text =""
            } else {
                _binding.idButtonChooseNumber.isEnabled=false
                _binding.idButtonCheck.isEnabled=true
                _binding.idTextView1.text = "Secret number chosen"
            }
        }

        /////////////////////////////////observer/////////////////////////////////
        secretNumberModel.checkResult.observe(viewLifecycleOwner){
            value->
            if (value==SecretNumberModel.CheckResult.EQUAL) {
                if (secretNumberModel.secretNumber.value!=SecretNumberModel.NO_SECRET_NUMBER) {
                    _binding.idTextView2.text = "Well done"
                    _binding.idButtonCheck.isEnabled = false
                    _binding.idButtonChooseNumber.isEnabled = true

                }
            }
            if (value==SecretNumberModel.CheckResult.GREATER) {
                _binding.idTextView2.text = "The secret number is greater"
            }
            if (value==SecretNumberModel.CheckResult.LOWER) {
                _binding.idTextView2.text = "The secret number is lower"
            }

        }

        /////////////////////////////////observer/////////////////////////////////
        secretNumberModel.nbTurn.observe(viewLifecycleOwner){
        if (secretNumberModel.nbTurn.value.toString()=="0") {
            if (secretNumberModel.checkResult.value.toString()!="EQUAL") {
                    _binding.idTextView4.text = "You've lost! The secret number was " + secretNumberModel.secretNumber.value.toString()
                    _binding.idButtonCheck.isEnabled=false
                _binding.idButtonChooseNumber.isEnabled=true
                }
            }
            if (secretNumberModel.secretNumber.value!=SecretNumberModel.NO_SECRET_NUMBER)
            _binding.idTextView3.text ="Remaining turn: "+ secretNumberModel.nbTurn.value.toString()
        }
    }
}