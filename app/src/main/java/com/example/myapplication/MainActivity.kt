// MainActivity.kt
package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Inisialisasi ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Variabel untuk menyimpan operand dan operator
    private var operand1: Int? = null
    private var operand2: Int? = null
    private var operator: String? = null

//    Fungsi ini dipanggil saat aktivitas pertama kali dibuat. Bundle dapat digunakan untuk menyimpan dan mengembalikan
//    keadaan sebelumnya jika aplikasi dirotasi atau ditutup dan dibuka kembali.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi binding dengan menggunakan LayoutInflater
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi klik listener untuk tombol angka
        setupNumberButtons()

        // Inisialisasi klik listener untuk tombol operator
        setupOperatorButtons()

        // Inisialisasi klik listener untuk tombol Clear, Equals, dan Backspace
        binding.btnClear.setOnClickListener { clearAll() }
        binding.btnEquals.setOnClickListener { calculateResult() }
        binding.imgArrow.setOnClickListener { backspace() }
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )

        for (button in numberButtons) {
            button.setOnClickListener {
                appendNumber(button.text.toString())
            }
        }
    }

    private fun setupOperatorButtons() {
        binding.btnAdd.setOnClickListener { setOperator("+") }
        binding.btnSubtract.setOnClickListener { setOperator("-") }
        binding.btnMultiply.setOnClickListener { setOperator("*") }
        binding.btnDivide.setOnClickListener { setOperator("/") }
    }

    private fun appendNumber(number: String) {
        binding.textEquation.append(number)
    }
//Memastikan input operator hanya ditambahkan jika sebelumnya ada angka.
//Jika tidak, akan menampilkan pesan kesalahan.
    private fun setOperator(op: String) {
        val equationText = binding.textEquation.text.toString()
        if (equationText.isNotEmpty() && equationText.last().isDigit()) {
            binding.textEquation.append(" $op ")
        } else {
            Toast.makeText(this, "Masukkan angka terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateResult() {
        val equationText = binding.textEquation.text.toString()
//      memisahkan persamaan operand dan operator
        val parts = equationText.split(" ")
//      memastikan yang dimasukkan adalah angka pertama, operator, angka ketiga
        if (parts.size != 3) {
            Toast.makeText(this, "Persamaan tidak lengkap", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            operand1 = parts[0].toInt()
            operator = parts[1]
            operand2 = parts[2].toInt()

            val result = when (operator) {
                "+" -> operand1!! + operand2!!
                "-" -> operand1!! - operand2!!
                "*" -> operand1!! * operand2!!
                "/" -> {
                    if (operand2 == 0) {
                        Toast.makeText(this, "Tidak bisa dibagi dengan nol", Toast.LENGTH_SHORT).show()
                        return
                    } else {
                        operand1!! / operand2!!
                    }
                }
                else -> {
                    Toast.makeText(this, "Operator tidak valid", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            binding.textResult.text = result.toString()
            Toast.makeText(this, "Hasil: $result", Toast.LENGTH_SHORT).show()

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Input tidak valid", Toast.LENGTH_SHORT).show()
        }
    }
//  metode hapus semua content
    private fun clearAll() {
        binding.textEquation.text = ""
        binding.textResult.text = ""
        operand1 = null
        operand2 = null
        operator = null
        Toast.makeText(this, "Dihapus", Toast.LENGTH_SHORT).show()
    }

    private fun backspace() {
        val equationText = binding.textEquation.text.toString()
        if (equationText.isNotEmpty()) {
            // Menghapus karakter terakhir
            val newText = equationText.dropLast(1)
            binding.textEquation.text = newText

            // Jika menghapus spasi sebelum operator, hapus juga operator
            if (newText.endsWith(" ")) {
                binding.textEquation.text = newText.dropLast(1)
            }

            Toast.makeText(this, "Dihapus satu karakter", Toast.LENGTH_SHORT).show()
        }
    }
}
