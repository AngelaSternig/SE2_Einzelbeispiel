package com.example.einzelbeispiel_12003325_sternigangela

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket
import java.util.*


class MainActivity : AppCompatActivity() {

    var primeNumbers = 0
    var editTextMatrikelnummer: EditText? = null
    var textViewResult: TextView? = null
    var buttonSend: Button? = null

    var textViewCalculate: TextView? = null
    var buttonCalculate: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextMatrikelnummer = findViewById(R.id.Matrikelnumber)
        buttonSend = findViewById(R.id.Senden)
        textViewResult = findViewById(R.id.Ausgabe)
        buttonCalculate = findViewById<View>(R.id.Berechnen) as Button
        buttonSend!!.setOnClickListener(View.OnClickListener { listenerButtonSend() })
    }

    private fun listenerButtonSend() {
        object : Thread() {
            override fun run() {
                val sentence = editTextMatrikelnummer!!.text.toString()
                try {
                    val clientSocket = Socket("se2-isys.aau.at", 53212)
                    val outToServer = DataOutputStream(clientSocket.getOutputStream())
                    val inFromServer =
                        BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                    outToServer.writeBytes(
                        """
                        $sentence
                        
                        """.trimIndent()
                    )
                    val modifiedSentence = inFromServer.readLine()
                    runOnUiThread { textViewResult!!.text = modifiedSentence }
                    clientSocket.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

}