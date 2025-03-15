package chapter05.discount

import chapter05.Screening

class SequenceDiscountCondition(
    private val sequence: Int,
) : DiscountCondition {
    override fun isSatisfiedBy(screening: Screening): Boolean {
        return this.sequence == screening.sequence
    }
}