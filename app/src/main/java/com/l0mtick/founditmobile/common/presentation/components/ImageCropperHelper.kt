package com.l0mtick.founditmobile.common.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

/**
 * Компонент для выбора и редактирования изображений с заданной рамкой
 * @param onImageSelected функция обратного вызова, которая будет вызвана при выборе изображения
 * @param aspectRatioX соотношение сторон по X (ширина)
 * @param aspectRatioY соотношение сторон по Y (высота)
 */
@Composable
fun rememberImageCropperLauncher(
    onImageSelected: (Uri) -> Unit,
    aspectRatioX: Int = 1,
    aspectRatioY: Int = 1
) = rememberLauncherForActivityResult(CropImageContract()) { result ->
    if (result.isSuccessful) {
        result.uriContent?.let { uri ->
            onImageSelected(uri)
        }
    }
}

/**
 * Создает опции для редактора изображений с заданной рамкой
 * @param aspectRatioX соотношение сторон по X (ширина)
 * @param aspectRatioY соотношение сторон по Y (высота)
 * @param includeCamera включить выбор из камеры
 * @param includeGallery включить выбор из галереи
 * @return опции для редактора изображений
 */
fun createCropImageOptions(
    aspectRatioX: Int = 1,
    aspectRatioY: Int = 1,
    includeCamera: Boolean = true,
    includeGallery: Boolean = true,
    circleShape: Boolean = false
): CropImageContractOptions {
    return CropImageContractOptions(
        uri = null,
        cropImageOptions = CropImageOptions(
            guidelines = CropImageView.Guidelines.ON,
            fixAspectRatio = true,
            aspectRatioX = aspectRatioX,
            aspectRatioY = aspectRatioY,
            cropShape = if (circleShape) CropImageView.CropShape.OVAL else CropImageView.CropShape.RECTANGLE,
            imageSourceIncludeCamera = includeCamera,
            imageSourceIncludeGallery = includeGallery
        )
    )
}

/**
 * Создает опции для редактора изображений с заданной рамкой для существующего изображения
 * @param uri URI существующего изображения
 * @param aspectRatioX соотношение сторон по X (ширина)
 * @param aspectRatioY соотношение сторон по Y (высота)
 * @return опции для редактора изображений
 */
fun createCropImageOptionsForExistingImage(
    uri: Uri,
    aspectRatioX: Int = 1,
    aspectRatioY: Int = 1,
    circleShape: Boolean = false
): CropImageContractOptions {
    return CropImageContractOptions(
        uri = uri,
        cropImageOptions = CropImageOptions(
            guidelines = CropImageView.Guidelines.ON,
            fixAspectRatio = true,
            aspectRatioX = aspectRatioX,
            aspectRatioY = aspectRatioY,
            cropShape = if (circleShape) CropImageView.CropShape.OVAL else CropImageView.CropShape.RECTANGLE
        )
    )
}