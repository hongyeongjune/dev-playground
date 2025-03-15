package chapter05.discount

import chapter05.Screening
import java.time.DayOfWeek
import java.time.LocalTime

class PeriodDiscountCondition(
    private val dayOfWeek: DayOfWeek,
    private val startTime: LocalTime,
    private val endTime: LocalTime,
) : DiscountCondition {
    override fun isSatisfiedBy(screening: Screening): Boolean {
        return this.dayOfWeek == screening.whenScreened.dayOfWeek &&
                this.startTime.isAfter(screening.whenScreened.toLocalTime()) &&
                this.endTime.isBefore(screening.whenScreened.toLocalTime())
    }
}