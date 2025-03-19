package pl.wsei.pam.lab03

import android.widget.ImageButton

data class Tile(val button: ImageButton, val tileResource: Int, val deckResource: Int) {
    init {
        button.setImageResource(deckResource) // Domyślnie karta jest zakryta
    }

    private var _revealed: Boolean = false
    var revealed: Boolean
        get() = _revealed
        set(value) {
            _revealed = value
            button.setImageResource(if (value) tileResource else deckResource) // Odkryj/Zakryj kartę
        }

    fun removeOnClickListener() {
        button.setOnClickListener(null) // Usuwa obsługę kliknięć (np. po dopasowaniu pary)
    }
}
