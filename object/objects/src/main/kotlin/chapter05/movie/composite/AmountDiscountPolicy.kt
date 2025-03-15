package chapter05.movie.composite

import chapter05.Money
import chapter05.Screening
import chapter05.discount.DiscountCondition

class AmountDiscountPolicy(
    private val discountAmount: Money,
    private val discountConditions: List<DiscountCondition>
) : DiscountPolicy {
    override fun calculateDiscountAmount(screening: Screening): Money {
        return if (discountConditions.any { it.isSatisfiedBy(screening) }) {
            discountAmount
        } else {
            Money.ZERO
        }
    }
}