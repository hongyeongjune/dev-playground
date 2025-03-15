package chapter05.movie.inheritance

import chapter05.Money
import chapter05.Screening
import chapter05.discount.DiscountCondition
import java.time.Duration

abstract class Movie(
    private val title: String,
    private val runningTime: Duration,
    val fee: Money,
    private val discountConditions: List<DiscountCondition>,
) {
    fun calculateMovieFee(screening: Screening): Money {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount())
        }
        return fee
    }

    private fun isDiscountable(screening: Screening): Boolean {
        return this.discountConditions.any { it.isSatisfiedBy(screening) }
    }

    abstract fun calculateDiscountAmount(): Money
}