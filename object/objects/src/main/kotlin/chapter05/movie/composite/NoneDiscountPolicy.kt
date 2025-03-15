package chapter05.movie.composite

import chapter05.Money
import chapter05.Screening

class NoneDiscountPolicy : DiscountPolicy {
    override fun calculateDiscountAmount(screening: Screening): Money {
        return Money.ZERO
    }
}