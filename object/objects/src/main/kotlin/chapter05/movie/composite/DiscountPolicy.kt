package chapter05.movie.composite

import chapter05.Money
import chapter05.Screening

interface DiscountPolicy {
    fun calculateDiscountAmount(screening: Screening): Money
}