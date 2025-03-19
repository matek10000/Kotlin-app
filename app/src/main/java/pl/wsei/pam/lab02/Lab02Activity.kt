package pl.wsei.pam.lab02

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab03.Lab03Activity

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab02)

        // Pobranie przycisków i przypisanie wspólnej funkcji
        findViewById<Button>(R.id.main_6_6_board).setOnClickListener { handleBoardSelection(it) }
        findViewById<Button>(R.id.main_4_4_board).setOnClickListener { handleBoardSelection(it) }
        findViewById<Button>(R.id.main_4_3_board).setOnClickListener { handleBoardSelection(it) }
        findViewById<Button>(R.id.main_3_2_board).setOnClickListener { handleBoardSelection(it) }
    }

    private fun handleBoardSelection(view: View) {
        val tag: String? = view.tag as String?
        Log.d("Lab02Activity", "Kliknięto przycisk, tag: $tag") // Debug

        val tokens = tag?.split(" ")
        val rows = tokens?.getOrNull(0)?.toIntOrNull()
        val columns = tokens?.getOrNull(1)?.toIntOrNull()

        if (rows != null && columns != null) {
            Log.d("Lab02Activity", "Przekazuję: rows=$rows, columns=$columns") // Debug
            val intent = Intent(this, Lab03Activity::class.java)
            intent.putExtra("rows", rows)
            intent.putExtra("columns", columns)
            startActivity(intent)
        } else {
            Log.e("Lab02Activity", "Błąd: Nie udało się odczytać tagu!")
            Toast.makeText(this, "Błąd w odczycie danych", Toast.LENGTH_SHORT).show()
        }
    }
}
