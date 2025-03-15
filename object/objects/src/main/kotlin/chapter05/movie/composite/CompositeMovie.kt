package chapter05.movie.composite

import chapter05.Money
import chapter05.Screening
import java.time.Duration

class CompositeMovie(
    private val title: String,
    private val runningTime: Duration,
    private val fee: Money,
    private val discountPolicy: DiscountPolicy,
) {
    fun calculateMovieFee(screening: Screening): Money {
        return fee.minus(discountPolicy.calculateDiscountAmount(screening))
    }
}