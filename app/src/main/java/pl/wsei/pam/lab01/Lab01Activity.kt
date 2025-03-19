package pl.wsei.pam.lab01

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Lab01Activity : AppCompatActivity() {
    lateinit var mLayout: LinearLayout
    lateinit var mTitle: TextView
    var mBoxes: MutableList<CheckBox> = mutableListOf()
    var mButtons: MutableList<Button> = mutableListOf()
    lateinit var mProgress: ProgressBar
    var completedTasks = 0  // Licznik ukończonych zadań

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab01)
        mLayout = findViewById(R.id.main)

        mTitle = TextView(this)
        mTitle.text = "Laboratorium 1"
        mTitle.textSize = 24f
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(20, 20, 20, 20)
        mTitle.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        mTitle.layoutParams = params
        mLayout.addView(mTitle)

        for (i in 1..6) {
            val row = LinearLayout(this)
            row.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            row.orientation = LinearLayout.HORIZONTAL

            val checkBox = CheckBox(this)
            checkBox.text = "Zadanie ${i}"
            checkBox.isEnabled = false
            mBoxes.add(checkBox)

            val button = Button(this).also {
                it.text = "Testuj"
                it.setOnClickListener {
                    if (!checkBox.isChecked) {  // Jeśli zadanie jeszcze nie było zaliczone
                        val passed = when (i) {
                            1 -> task11(4, 6) in 0.666665..0.666667 && task11(7, -6) in -1.1666667..-1.1666665
                            2 -> task12(7U, 6U) == "7 + 6 = 13" && task12(12U, 15U) == "12 + 15 = 27"
                            3 -> task13(0.0, 5.4f) && !task13(7.0, 5.4f) &&
                                    !task13(-6.0, -1.0f) && task13(6.0, 9.1f) &&
                                    !task13(6.0, -1.0f) && task13(1.0, 1.1f)
                            4 -> task14(-2, 5) == "-2 + 5 = 3" && task14(-2, -5) == "-2 - 5 = -7"
                            5 -> task15("DOBRY") == 4 && task15("barDzo dobry") == 5 &&
                                    task15("doStateczny") == 3 && task15("Dopuszczający") == 2 &&
                                    task15("NIEDOSTATECZNY") == 1 && task15("XYZ") == -1
                            6 -> task16(
                                mapOf("A" to 2U, "B" to 4U, "C" to 3U),
                                mapOf("A" to 1U, "B" to 2U)
                            ) == 2U &&
                                    task16(
                                        mapOf("A" to 2U, "B" to 4U, "C" to 3U),
                                        mapOf("F" to 1U, "G" to 2U)
                                    ) == 0U &&
                                    task16(
                                        mapOf("A" to 23U, "B" to 47U, "C" to 30U),
                                        mapOf("A" to 1U, "B" to 2U, "C" to 4U)
                                    ) == 7U
                            else -> false
                        }
                        if (passed) {
                            checkBox.isChecked = true
                            completedTasks++

                            // Dokładne obliczenie postępu
                            mProgress.progress = ((completedTasks / 6.0) * 100).toInt()
                        } else {
                            Toast.makeText(this, "Test nie powiódł się", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            row.addView(checkBox)
            row.addView(button)
            mLayout.addView(row)
        }

        mProgress = ProgressBar(
            this,
            null,
            androidx.appcompat.R.attr.progressBarStyle,
            androidx.appcompat.R.style.Widget_AppCompat_ProgressBar_Horizontal
        ).also {
            it.max = 100
            it.progress = 0
        }
        mLayout.addView(mProgress)
    }

    // Wykonaj dzielenie niecałkowite parametru a przez b
    private fun task11(a: Int, b: Int): Double {
        return a.toDouble() / b.toDouble()
    }

    // Funkcja zwracająca sumę argumentów w postaci łańcucha
    private fun task12(a: UInt, b: UInt): String {
        return "$a + $b = ${a + b}"
    }

    // Sprawdza, czy `a` jest nieujemne i mniejsze od `b`
    fun task13(a: Double, b: Float): Boolean {
        return a >= 0.0 && a < b
    }

    // Funkcja zwracająca sumę lub różnicę w zależności od wartości `b`
    private fun task14(a: Int, b: Int): String {
        return "$a ${if (b < 0) "-" else "+"} ${Math.abs(b)} = ${a + b}"
    }

    // Konwertuje ocenę słowną na liczbę
    fun task15(degree: String): Int {
        return when (degree.lowercase()) {
            "bardzo dobry" -> 5
            "dobry" -> 4
            "dostateczny" -> 3
            "dopuszczający" -> 2
            "niedostateczny" -> 1
            else -> -1
        }
    }

    // Oblicza maksymalną liczbę egzemplarzy możliwych do zbudowania
    fun task16(store: Map<String, UInt>, asset: Map<String, UInt>): UInt {
        return asset.map { (key, value) -> (store[key] ?: 0U) / value }.minOrNull() ?: 0U
    }
}
