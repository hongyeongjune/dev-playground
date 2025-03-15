package chapter05

import chapter05.movie.inheritance.Movie
import java.time.LocalDateTime

class Screening(
    private val movie: Movie,
    val sequence: Int,
    val whenScreened: LocalDateTime,
) {
    fun reserve(customer: Customer, audienceCount: Int) {}

    private fun calculateFee(audienceCount: Int): Money {
        return movie.calculateMovieFee(this).times(audienceCount)
    }
}