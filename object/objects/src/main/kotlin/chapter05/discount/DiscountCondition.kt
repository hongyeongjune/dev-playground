package chapter05.discount

import chapter05.Screening

interface DiscountCondition {
    fun isSatisfiedBy(screening: Screening): Boolean
}