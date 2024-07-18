package com.example.mobilebanking.utils

import android.graphics.Bitmap
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class QRCodeUtil {
    companion object {
        private const val QR_CODE_SIZE = 500

        @JvmStatic
        @Throws(WriterException::class)
        fun generateQRCode(content: String): Bitmap? {
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, com.google.zxing.BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE)
            val bitmap = Bitmap.createBitmap(QR_CODE_SIZE, QR_CODE_SIZE, Bitmap.Config.RGB_565)
            for (x in 0 until QR_CODE_SIZE) {
                for (y in 0 until QR_CODE_SIZE) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
                }
            }
            return bitmap
        }
    }
}