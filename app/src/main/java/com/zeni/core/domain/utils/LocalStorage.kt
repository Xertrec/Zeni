package com.zeni.core.domain.utils

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.zeni.core.domain.utils.extensions.completeFileName
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStorage @Inject constructor(
    @ApplicationContext val appContext: Context
) {
    fun copyImageToLocalStorage(uri: Uri): Uri {
        return try {
            val destinationFile = File(appContext.filesDir, uri.completeFileName)
            FileOutputStream(destinationFile).use { outputStream ->
                appContext.contentResolver.openInputStream(uri)!!.use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            destinationFile.toUri()
        } catch (e: IOException) {
            Uri.EMPTY
        }
    }

    fun getImageFromLocalStorage(imageName: String): Uri {
        val file = File(appContext.filesDir, imageName)
        return if (file.exists()) {
            file.toUri()
        } else {
            Uri.EMPTY
        }
    }
}
