package chapter05.movie.inheritance

import chapter05.Money
import chapter05.discount.DiscountCondition
import java.time.Duration

class AmountDiscountMovie(
    title: String,
    runningTime: Duration,
    fee: Money,
    discountConditions: List<DiscountCondition>,
    private val discountAmount: Money,
) : Movie(
    title,
    runningTime,
    fee,
    discountConditions,
) {
    override fun calculateDiscountAmount(): Money {
        return discountAmount
    }
}