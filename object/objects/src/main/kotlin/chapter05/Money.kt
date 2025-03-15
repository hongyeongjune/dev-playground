package chapter05

import java.math.BigDecimal

class Money(
    val amount: BigDecimal,
) {
    fun plus(amount: Money): Money {
        return Money.from(this.amount.add(amount.amount))
    }

    fun minus(amount: Money): Money {
        return Money.from(this.amount.subtract(amount.amount))
    }

    fun times(percent: Int): Money {
        return Money.from(this.amount.multiply(BigDecimal.valueOf(percent.toLong())))
    }

    fun times(percent: Double): Money {
        return Money.from(this.amount.multiply(BigDecimal.valueOf(percent)))
    }

    companion object {
        val ZERO = Money.wons(0)

        fun wons(amount: Long): Money {
            return Money(BigDecimal.valueOf(amount))
        }

        fun wons(amount: Double): Money {
            return Money(BigDecimal.valueOf(amount))
        }

        fun from(amount: BigDecimal): Money {
            return Money(amount)
        }
    }
}