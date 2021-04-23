package com.example.calculator_demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity()
{
    /*
        lateinit : Late Initialization or lateinit is used when the developer is certain that the variable will not be null when accessing it. This eliminates the null checks (i.e. ?= null )

        by lazy : By using the lazy delegate (or, by lazy) we're defining a function that will be called to assign the value to the property. The function will be called the first time when
                  that property is accessed, then the value will be cached, so that the function will not be called again. To be noted, that the lazy delegate can only be used with "val" type
                  variables.

        LazyThreadSafetyMode.NONE : No locks are used to sync an access to the Lazy instance value; if the instance is accessed from multiple threads, it's behavior will be undefined.
    */
    private lateinit var resultEditText : EditText
    private lateinit var newNumEditText : EditText
    private val displayOperands by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operators) }

    //Variables to hold the operands and the type of calculation
    private var operand1 : Double ?= null //Num1 for calculation
    private var operand2 : Double = 0.0 //Num2 for calculation
    private var pendingOperation = "=" //

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setTitle("Basic Calculator")

        //Typecasting the EditTexts
        resultEditText = findViewById(R.id.showResultEditText)
        newNumEditText = findViewById(R.id.newNumberEditText)

        //Data input buttons
        val button0 : Button = findViewById(R.id.button0)
        val button1 : Button = findViewById(R.id.button1)
        val button2 : Button = findViewById(R.id.button2)
        val button3 : Button = findViewById(R.id.button3)
        val button4 : Button = findViewById(R.id.button4)
        val button5 : Button = findViewById(R.id.button5)
        val button6 : Button = findViewById(R.id.button6)
        val button7 : Button = findViewById(R.id.button7)
        val button8 : Button = findViewById(R.id.button8)
        val button9 : Button = findViewById(R.id.button9)
        val buttonDot : Button = findViewById(R.id.buttonDot)

        //Operation buttons
        val buttonPlus : Button = findViewById(R.id.buttonPlus)
        val buttonMinus : Button = findViewById(R.id.buttonMinus)
        val buttonMultiply : Button = findViewById(R.id.buttonMultiply)
        val buttonDivide : Button = findViewById(R.id.buttonDivide)
        val buttonModulo : Button = findViewById(R.id.buttonModulo)
        val buttonDelete : Button = findViewById(R.id.buttonDelete)
        val buttonAllClear : Button = findViewById(R.id.buttonAllClear)
        val buttonEquals : Button = findViewById(R.id.buttonEquals)
        val buttonExit : Button = findViewById(R.id.buttonExit)

        //Configuring the onClickListener: This single onclicklistener will do operations for multiple buttons.
        val listener = View.OnClickListener { v->
            val data = v as Button //v is an object of View class
            newNumEditText.append(data.text) //This will add a new text value at the end of the previous string
        }

        //Assigning the above onclickListener object to the buttons for operation with the edittexts. These will enter the numbers into the editTexts automatically based on the buttons that we selected.
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)


        //The traditional onclicklistener to exit the app
        buttonExit.setOnClickListener {
            finishAffinity()
        }

        //To clear the editTexts
        buttonAllClear.setOnClickListener {
            newNumEditText.text.clear()
            resultEditText.text.clear()
            operand1 = null
            operand2 = 0.0
            displayOperands.setText("  ")
        }

        //Customizing the onClickListener for the operations
        /*
            Logic: Operands from the buttons & the value in the newNumEditText are entered as parameters into the user defined function perfomOperation() where the arithmatic operations will be performed.
                   "pendingOperation" represents the sign of operands i.e. + for addition, - for subtraction, * for multiplication, / for division, % for percentage & = for equals.

                    The operands are selected by the "op" at line #104 where the button will be responsible to provide the operands whenever the user clicks on the operands button(s).
        */
        val operationListener = View.OnClickListener {v->
            val op = (v as Button).text.toString() //It's picking up the text written on the button
            val value = newNumEditText.text.toString()

            if (value.isNotEmpty())
            {
                performOperation(value, op) //User defined function
            }

            pendingOperation = op
            displayOperands.text = pendingOperation //Displays the operand that is in use
        }

        //Assigning the operand buttons with the new onclicklistener above for performing operations. These buttons will automatically putting the symbols of the operands when clicked.
        buttonEquals.setOnClickListener(operationListener)
        buttonDivide.setOnClickListener(operationListener)
        buttonMultiply.setOnClickListener(operationListener)
        buttonMinus.setOnClickListener(operationListener)
        buttonPlus.setOnClickListener(operationListener)
        buttonModulo.setOnClickListener(operationListener)
    }

    //Implementing the user defined function
    private fun performOperation(value : String, operation : String)
    {
        if(operand1 == null)
        {
            operand1 = value.toDouble()
        }
        else
        {
            operand2 = value.toDouble()

            /* For the equal button */
            if(pendingOperation == "=") //If equals button is pressed. In simple words if the picked up text from button is "=".
            {
                pendingOperation = operation //operation variable holds the string value "=" if equals button is pressed
            }

            /* For the rest of the operations. Arithmatic operations will be stored inside the variable "operand1" */
            when(pendingOperation)
            {
                "=" -> operand1 = operand2
                "/" -> if (operand2 == 0.0)
                       {
                           operand1 = Double.NaN //Handle attempt to divide by zero
                       }
                       else
                       {
                           operand1 = operand1!! / operand2 //operand1!! means that the value stored under operand1 can't be null
                       }
                "*" -> operand1 = operand1!! * operand2
                "-" -> operand1 = operand1!! - operand2
                "+" -> operand1 = operand1!! + operand2
                "%" -> operand1 = operand1!! * (operand2/100)
            }
        }

        //Showing result
        resultEditText.setText(operand1.toString())
        newNumEditText.setText("")
        displayOperands.text = operation
    }
}