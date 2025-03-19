package pl.wsei.pam.lab03

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import pl.wsei.pam.lab01.R
import java.util.*

class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int,
    private val activity: Activity
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
        R.drawable.baseline_content_cut_24,
        R.drawable.baseline_hail_24,
        R.drawable.baseline_hive_24,
        R.drawable.baseline_icecream_24,
        R.drawable.baseline_import_contacts_24,
        R.drawable.baseline_house_24,
        R.drawable.baseline_ice_skating_24,
        R.drawable.baseline_image_24,
        R.drawable.baseline_insert_comment_24
    )

    init {
        Log.d("MemoryBoardView", "Tworzenie planszy: cols=$cols, rows=$rows")

        val totalPairs = cols * rows / 2
        val shuffledIcons = icons.shuffled().take(totalPairs).flatMap { listOf(it, it) }.shuffled()

        var index = 0
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val button = ImageButton(gridLayout.context).apply {
                    tag = "${row}x${col}"
                }

                val resourceImage = shuffledIcons[index++]

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

    private fun onClickTile(v: View) {
        if (isProcessingMove) return

        val tile = tiles[v.tag] ?: return
        if (tile.revealed) return

        tile.revealed = true
        matchedPair.push(tile)

        val matchResult = logic.process { tile.tileResource }
        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))

        when (matchResult) {
            GameStates.NoMatch -> {
                isProcessingMove = true
                activity.runOnUiThread {
                    animateWrongPair(matchedPair.toList()) // Przekazujemy listę `Tile`, a nie `button`
                }
                v.postDelayed({
                    activity.runOnUiThread {
                        matchedPair.forEach { it.revealed = false }
                        matchedPair.clear()
                        isProcessingMove = false
                    }
                }, 1000)
            }
            GameStates.Match, GameStates.Finished -> {
                activity.runOnUiThread {
                    matchedPair.forEach { animateMatchedPair(it.button) }
                }
                matchedPair.forEach { it.removeOnClickListener() }
                matchedPair.clear()
            }
            else -> {}
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

    /** 🔹 Animacja trafionej pary (obrót + powiększenie + zanikanie) */
    fun animateMatchedPair(button: ImageButton) {
        val set = AnimatorSet()
        val rotation = ObjectAnimator.ofFloat(button, "rotation", 0f, 1080f)
        val scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.5f, 0f)
        val scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.5f, 0f)
        val fadeOut = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)

        set.playTogether(rotation, scaleX, scaleY, fadeOut)
        set.interpolator = android.view.animation.DecelerateInterpolator()
        set.duration = 1000
        set.start()
    }

    /** 🔹 Animacja nietrafionej pary (ruch przeczący - delikatne obracanie) */
    fun animateWrongPair(tiles: List<Tile>) {
        tiles.forEach { tile ->
            val shake = ObjectAnimator.ofFloat(tile.button, "translationX", -10f, 10f, -10f, 10f, 0f)
            shake.duration = 500
            shake.start()
        }
    }


    fun getState(): IntArray = tiles.values.map { if (it.revealed) it.tileResource else -1 }.toIntArray()
    fun getIcons(): IntArray = tiles.values.map { it.tileResource }.toIntArray()
    fun setState(state: IntArray) {
        tiles.values.toList().forEachIndexed { index, tile -> tile.revealed = state[index] != -1 }
    }
}
