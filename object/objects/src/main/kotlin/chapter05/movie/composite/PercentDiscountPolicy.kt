package chapter05.movie.composite

import chapter05.Money
import chapter05.Screening
import chapter05.discount.DiscountCondition

class PercentDiscountPolicy(
    private val fee: Money,
    private val percent: Double,
    private val discountConditions: List<DiscountCondition>
) : DiscountPolicy {
    override fun calculateDiscountAmount(screening: Screening): Money {
        return if (discountConditions.any { it.isSatisfiedBy(screening) }) {
            fee.times(percent)
        } else {
            Money.ZERO
        }
    }
}