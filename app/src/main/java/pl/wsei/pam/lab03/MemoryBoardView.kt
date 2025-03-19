package pl.wsei.pam.lab03

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import java.util.*
import pl.wsei.pam.lab01.R

class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int,
    private val activity: Activity,
    private val savedIcons: IntArray? = null  // Nowy argument - zapisane ikony
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val deckResource: Int = R.drawable.deck
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)
    private val matchedPair: Stack<Tile> = Stack()
    private var isProcessingMove = false

    private val icons: List<Int> = listOf(
        R.drawable.rocket,
        R.drawable.baseline_star_24,
        R.drawable.baseline_android_24,
        R.drawable.baseline_favorite_24,
        R.drawable.baseline_assistant_direction_24,
        R.drawable.baseline_airplanemode_active_24,
        R.drawable.baseline_attach_money_24,
        R.drawable.baseline_camera_24,
        R.drawable.baseline_coffee_maker_24,
        R.drawable.baseline_content_cut_24
    )

    init {
        Log.d("MemoryBoardView", "Tworzenie planszy: cols=$cols, rows=$rows")

        val totalPairs = cols * rows / 2
        val shuffledIcons: MutableList<Int> = if (savedIcons == null) {
            // Losujemy nowe ikony, jeśli nie ma zapisanych ikon
            val tempIcons = icons.toMutableList()
            while (tempIcons.size < totalPairs) {
                tempIcons.addAll(icons)
            }
            tempIcons.subList(0, totalPairs).toMutableList().apply {
                addAll(this)  // Podwajamy listę ikon
                shuffle()
            }
        } else {
            savedIcons.toMutableList()  // Używamy zapisanych ikon
        }

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val button = ImageButton(gridLayout.context).apply {
                    tag = "${row}x${col}"
                }

                val resourceImage = shuffledIcons.removeAt(0)

                addTile(button, resourceImage)
                gridLayout.addView(button, createLayoutParams(row, col))
            }
        }
    }

    private fun createLayoutParams(row: Int, col: Int): GridLayout.LayoutParams {
        return GridLayout.LayoutParams().apply {
            width = 0
            height = 0
            setGravity(android.view.Gravity.CENTER)
            columnSpec = GridLayout.spec(col, 1, 1f)
            rowSpec = GridLayout.spec(row, 1, 1f)
        }
    }

    fun getState(): IntArray {
        return tiles.values.map { if (it.revealed) it.tileResource else -1 }.toIntArray()
    }

    fun getIcons(): IntArray {
        return tiles.values.map { it.tileResource }.toIntArray()
    }

    fun setState(state: IntArray) {
        val tileList = tiles.values.toList()
        for (i in state.indices) {
            tileList[i].revealed = state[i] != -1
        }
    }

    private fun onClickTile(v: View) {
        if (isProcessingMove) return

        val tile = tiles[v.tag] ?: return
        if (tile.revealed) return

        tile.revealed = true
        matchedPair.push(tile)

        val matchResult = logic.process { tile.tileResource }

        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))

        if (matchResult == GameStates.NoMatch) {
            isProcessingMove = true
            activity.runOnUiThread {
                matchedPair.forEach { it.revealed = true }
            }
            v.postDelayed({
                activity.runOnUiThread {
                    matchedPair.forEach { it.revealed = false }
                    matchedPair.clear()
                    isProcessingMove = false
                }
            }, 2000)
        } else if (matchResult == GameStates.Match || matchResult == GameStates.Finished) {
            matchedPair.forEach { it.removeOnClickListener() }
            matchedPair.clear()
        }
    }

    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { _ -> }

    fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    private fun addTile(button: ImageButton, resourceImage: Int) {
        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }
}
