/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author athif
 */

import java.text.DecimalFormat;

public class DialogUtils {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

    // Metode utilitas untuk memformat angka
    public static String formatNumberWithThousandsSeparator(String number) {
        try {
            // Menghapus karakter yang tidak diperlukan, menjaga hanya angka dan titik desimal
            double parsedNumber = Double.parseDouble(number.replaceAll("[^\\d.]", ""));
            return decimalFormat.format(parsedNumber);
        } catch (NumberFormatException e) {
            return number; // Jika format tidak sesuai, kembalikan angka default
        }
    }

}
