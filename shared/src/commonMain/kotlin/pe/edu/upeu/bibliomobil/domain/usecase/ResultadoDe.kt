package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.CancellationException

suspend fun <T> resultadoDe(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
