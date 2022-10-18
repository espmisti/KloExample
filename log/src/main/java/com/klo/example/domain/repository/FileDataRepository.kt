package com.klo.example.domain.repository

interface FileDataRepository {
    // Генерируем название файла
    suspend fun getFileName(date: String) : String
    // Создаем файл
    suspend fun createFile(fileName: String) : Boolean
}