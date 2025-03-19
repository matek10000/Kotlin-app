package pl.wsei.pam.lab03

import android.os.Bundle
import android.util.Log
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import java.util.Timer
import kotlin.concurrent.schedule

class Lab03Activity : AppCompatActivity() {

    private lateinit var mBoardModel: MemoryBoardView
    private lateinit var gridLayout: GridLayout
    private var isProcessingMove = false  // Blokowanie szybkiego klikania kart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        gridLayout = findViewById(R.id.mBoard)
        val rows = intent.getIntExtra("rows", -1)
        val cols = intent.getIntExtra("columns", -1)

        if (savedInstanceState != null) {
            val gameState = savedInstanceState.getIntArray("game_state")
            val tileIcons = savedInstanceState.getIntArray("tile_icons")

            if (gameState != null && tileIcons != null) {
                mBoardModel = MemoryBoardView(gridLayout, cols, rows, this, tileIcons)
                mBoardModel.setState(gameState)
            }
        } else {
            mBoardModel = MemoryBoardView(gridLayout, cols, rows, this)
        }

    // Obsługa zdarzeń gry
        mBoardModel.setOnGameChangeListener { e ->
            when (e.state) {
                GameStates.Matching -> {
                    runOnUiThread {
                        e.tiles.forEach { it.revealed = true }
                    }
                }
                GameStates.Match -> {
                    runOnUiThread {
                        e.tiles.forEach { it.revealed = true }
                    }
                }
                GameStates.NoMatch -> {
                    isProcessingMove = true

                    runOnUiThread {
                        e.tiles.forEach { it.revealed = true }
                    }

                    Timer().schedule(2000) {
                        runOnUiThread {
                            e.tiles.forEach { it.revealed = false }
                            isProcessingMove = false
                        }
                    }
                }
                GameStates.Finished -> {
                    runOnUiThread {
                        Toast.makeText(this@Lab03Activity, "🎉 Gra zakończona! 🎉", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val gameState = mBoardModel.getState()
        val tileIcons = mBoardModel.getIcons()  // Nowa metoda do pobrania ikon kart

        outState.putIntArray("game_state", gameState) // Zapisujemy stan odkrytych kart
        outState.putIntArray("tile_icons", tileIcons) // Zapisujemy oryginalne ikony kart
    }

}
