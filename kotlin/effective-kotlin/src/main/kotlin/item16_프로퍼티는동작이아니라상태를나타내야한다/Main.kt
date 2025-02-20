package item16_프로퍼티는동작이아니라상태를나타내야한다

import java.util.Date

class Main {
    var firstName: String = ""
        set(value) {
            if (value.isNotBlank()) {
                field = value
            }
        }

    var secondName: String = ""
        set(value) {
            if (value.isNotBlank()) {
                this.secondName = value
            }
        }

    var millis: Long = 0L

    var date: Date
        get() = Date(millis)
        set(value) {
            millis = value.time
        }
}

val String.lastChar: Char
    get() = get(this.length - 1)