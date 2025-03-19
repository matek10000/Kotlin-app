package pl.wsei.pam.lab03

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R

class Lab03Activity : AppCompatActivity() {

    private lateinit var mBoardModel: MemoryBoardView
    private lateinit var gridLayout: GridLayout
    private var isProcessingMove = false // Blokowanie szybkiego klikania kart
    private var isSound: Boolean = true  // 🔊 Zmienna do kontroli dźwięków

    // 🎵 MediaPlayer do efektów dźwiękowych
    private lateinit var completionPlayer: MediaPlayer
    private lateinit var negativePlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        gridLayout = findViewById(R.id.mBoard)
        val rows = intent.getIntExtra("rows", 3)
        val cols = intent.getIntExtra("columns", 3)

        if (savedInstanceState != null) {
            val gameState = savedInstanceState.getIntArray("game_state")
            val tileIcons = savedInstanceState.getIntArray("tile_icons")

            if (gameState != null && tileIcons != null) {
                mBoardModel = MemoryBoardView(gridLayout, cols, rows, this)
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
                        if (isSound) completionPlayer.start() // 🔊 Odtwarzanie dźwięku tylko jeśli opcja jest aktywna
                    }
                }
                GameStates.NoMatch -> {
                    isProcessingMove = true
                    runOnUiThread {
                        e.tiles.forEach { it.revealed = true }
                        if (isSound) negativePlayer.start() // 🔇 Dźwięk błędu tylko jeśli opcja jest aktywna
                    }
                    mBoardModel.animateWrongPair(e.tiles)
                    gridLayout.postDelayed({
                        runOnUiThread {
                            e.tiles.forEach { it.revealed = false }
                            isProcessingMove = false
                        }
                    }, 1000)
                }
                GameStates.Finished -> {
                    runOnUiThread {
                        if (isSound) completionPlayer.start() // 🔊 Odtwarzanie dźwięku na zakończenie gry
                        Toast.makeText(this@Lab03Activity, "🎉 Gra zakończona! 🎉", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 🎵 Inicjalizacja odtwarzaczy dźwięków
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        negativePlayer = MediaPlayer.create(applicationContext, R.raw.negative_guitar)
    }

    override fun onPause() {
        super.onPause()
        // 🎵 Zwolnienie zasobów po wyjściu z aktywności
        completionPlayer.release()
        negativePlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("game_state", mBoardModel.getState())
        outState.putIntArray("tile_icons", mBoardModel.getIcons())
    }

    // 🔹 Menu opcji - Dodanie ikony dźwięku 🔊
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.board_activity_menu, menu)

        // 👇 Ustaw ikonę na podstawie aktualnego stanu dźwięku przy otwarciu menu
        val soundItem = menu.findItem(R.id.board_activity_sound)
        soundItem.setIcon(if (isSound) R.drawable.volume_up else R.drawable.volume_down)

        return true
    }


    // 🔹 Obsługa kliknięcia na menu dźwięku 🔊
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.board_activity_sound -> {
                isSound = !isSound // Odwracamy stan dźwięku

                // 👇 Zmieniamy ikonę od razu po kliknięciu
                item.setIcon(if (isSound) R.drawable.volume_up else R.drawable.volume_down)

                val message = if (isSound) "🔊 Dźwięk włączony" else "🔇 Dźwięk wyłączony"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
