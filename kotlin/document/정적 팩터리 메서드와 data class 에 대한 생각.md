* effective kotlin 책을 읽으면서 kotlin 을 공부하던 중 고민포인트가 하나 생겼다.
* effective kotlin 1장 내용은 "가변성을 제한하라" 이다.
* 여기서 가변성을 제한하는 여러가지 방법 중 대표적인 방법을 data class 의 copy() 메서드 사용을 설명한다.
* 프로퍼티를 val 로 선언하고 copy() 메서드를 사용하여 깊은 복사를 하면, 불변성을 지키면서 개발을 할 수 있게된다.
* 그리고 effective kotlin 33장을 확인해보면 "생성자 대신 정적 팩터리 메서드를 사용하라" 이다.
* 정적 팩터리 메서드를 사용하는 방법을 요약하면 생성자를 private 를 선언하고 companion object 를 사용하여 팩터리 메서드를 만드는 것 이다.
* 그렇다면, 아래 처럼 구현하면 어떨까 ?

```kotlin
data class Webtoon private constructor(
    val id: Int?,
    val subject: String,
    val description: String?,
) {
    companion object {
        fun of(
            id: Int? = null,
            subject: String,
            description: String? = null,
        ): Webtoon {
            return Webtoon(
                id = id,
                subject = subject,
                description = description,
            )
        }
    }
}
```

* 얼핏 보면 문제 없어보이지만, data class 는 copy() 메서드를 제공하므로써 생성자가 그대로 노출된다.
* 나는 그래서 이것을 올바르지 않은 디자인이라고 생각했다.
* 따라서 관련해서 여러 글을 찾아봤다.

> http://gopalkri.com/2017/08/18/Kotlin-Data-Class-Constructor-Problem-Followup/
> https://youtrack.jetbrains.com/issue/KT-11914/Confusing-data-class-copy-with-private-constructor

* 내가 만족하는 답변은 없었다.
* 조금 더 고민이 필요하다.

### 결론
* data class 는 딱 프로퍼티만 담고 로직이 들어가면 안되는 클래스로 봤다.
* 비즈니스 로직을 선언해도 getter 정도에만 들어가는게 맞고, 추가로 비즈니스가 들어가면 안된다고 생각했다.
* 다만 그냥 class 의 경우에는 자바와 마찬가지로 사용하기 때문에 정적 팩터리 메서드가 들어가는게 맞다고 느껴졌다.
* data class 를 남발하는것도 좋다고 느껴지지 않았다.