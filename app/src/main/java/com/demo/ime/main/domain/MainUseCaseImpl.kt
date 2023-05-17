package com.demo.ime.main.domain

import com.demo.ime.main.data.repository.MainRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainUseCaseImpl @Inject constructor(private val repository: MainRepository) :
    MainUseCase {
    override suspend fun generatePinBlock(pin: String) = repository.generatePinBlock(pin)
}
