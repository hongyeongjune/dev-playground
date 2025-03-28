### 인터페이스와 설계 품질
* 좋은 인터페이스는 최소한의 인터페이스와 추상적인 인터페이스라는 조건을 만족해야 한다.
* 퍼블릭 인터페이스의 품질에 영향을 원칙과 기법 
  * 디미터 법칙 
  * 묻지 말고 시켜라 
  * 의도를 드러내는 인터페이스 
  * 명령-쿼리 분리

### 다미터 법칙

* 디미터 법칙은 객체의 내부 구조에 강하게 결합되지 않도록 협력 경로를 제한하는 것이다. 
* 자바나 C#과 같이 '도트(.)'를 이용해 메시지 전송을 표현하는 언어에서는 "오직 하나의 도트만 사용하라"라는 말로 요약 되기도 한다.
* 디미터 법칙을 따르면 부끄럼타는 코드를 작성할 수 있다. 
* 부끄럼타는 코드란 불필요한 어떤 것도 다른 객체에게 보여주지 않으며, **다른 객체의 구현에 의존하지 않는 코드**를 말한다.

```kotlin
// 디미터 법칙을 위반하는 코드
screening.getMovie().getDiscountConditions()
```

* 디미터 법칙을 따르도록 코드를 개선하면 메시지 전송자는 더 이상 메시지 수신자의 내부 구조에 관해 묻지 않게 된다.

```kotlin
// 디미터 법칙을 위반하지 않는 코드
screening.calculateFee(audienceCount)
```

* 정보를 처리하는 데 필요한 책임을 정보를 알고 있는 객체에게 할당하기 때문에 응집도가 높은 객체가 만들어진다.

* 특정한 조건을 만족하는 객체에게만 메시지를 보내야한다면 아래 조건을 충족하는게 일반적이다. 
  * this 객체 
  * 메서드의 매개변수 
  * this 의 속성 
  * this 의 속성인 컬렉션의 요소 
  * 메서드 내에서 생성된 지역 객체

### 묻지 말고 시켜라

* 디미터 법칙은 훌륭한 메시지는 객체의 상태에 관해 묻지 말고 원하는 것을 시켜야 한다는 사실을 강조한다. 
* 묻지 말고 시켜라는 이런 스타일의 메시지 작성을 장려하는 원칙을 가리키는 용어다. 
* 객체의 외부에서 해당 객체의 상태를 기반으로 결정을 내리는 것은 객체의 캡슐화를 위반한다. 
* 훌륭한 인터페이스를 수확하기 위해서는 객체가 어떻게 작업을 수행하는지를 노출해서는 안된다. 
* 인터페이스는 객체가 어떻게 하는지가 아니라 무엇을 하는지를 서술해야 한다.

### 의도를 드러내는 인터페이스

* 인터페이스의 메서드명을 짓는 방법
  * 메서드가 작업을 **어떻게** 하는지를 나타내도록 이름을 짓는 방법 (좋지 않은 방법)
  * 메서드가 작업을 **무엇을** 하는지를 나타내도록 이름을 짓는 방법 (좋은 방법)

```kotlin
// 메서드가 작업을 어떻게 하는지를 나타내도록 이름을 짓는 방법
class PeriodCondition {
    fun isSatisfiedByPeriod(screening: Screening): Boolean { 
        // ... 
    }
}

class SequenceCondition {
    fun isSatisfiedBySequence(screening: Screening): Boolean { 
        //... 
    }
}
```

* 메서드의 이름이 다르기 때문에 두 메서드의 내부 구현을 정확하게 이해해야 한다. 
* 이 메서드들은 클라이언트로 하여금 협력하는 객체의 종류를 알도록 강요한다. 
* 좋은 메서드명을 짓기 위해서는 객체가 협력 안에서 수행해야 하는 책임에 관해 고민해야 한다. 
* 이것은 외부의 객체가 메시지를 전송하는 목적을 먼저 생각하도록 만들며, 결과적으로 협력하는 클라이언트의 의도에 부합하도록 메서드의 이름을 짓게 된다.

```kotlin
// 메서드가 작업을 무엇을 하는지를 나타내도록 이름을 짓는 방법
class PeriodCondition : DiscountCondition {
    override fun isSatisfiedBy(screening: Screening): Boolean {
        // ... 
    }
}

class SequenceCondition {
    override fun isSatisfiedBy(screening: Screening): Boolean {
        // ... 
    }
}
```

* 메서드가 어떻게 수행하느냐가 아니라 무엇을 하느냐에 초엄을 맞추면 클라이언트의 관점에서 동일한 작업을 수행하는 메서드들을 하나의 타입 계층으로 묶을 수 있는 가능성이 커진다.

### 원칙의 함정

* 디미터 법칙과 묻지 말고 시켜라 스타일은 객체의 퍼블릭 인터페이스를 깔끔하고 유연하게 만들 수 있는 훌륭한 설계 원칙이다. 
* 하지만 절대적인 법칙은 아니다. 
* 소프트웨어 설계에 법칙이란 존재하지 않는다. 법칙에는 예외가 없지만 원칙에는 예외가 넘쳐난다.
* 설계를 적절하게 트레이드 오프 할 수 있는 능력이 숙련자와 초보자를 구분하는 가장 중요한 기준이라고 할 수 있다. 
* 초보자는 맹목적으로 추종한다. 원칙이 현재 상황에 부적합하다고 판단된다면 과감하게 원칙을 무시하라.

### 디미터 법칙은 하나의 도트(.)를 강제하는 규칙이 아니다

```java
IntStream.of(1, 15, 20, 3, ().filter(x -> x > 10).distinct().count();
```

* 디미터 법칙은 결합도와 관련된 것이며 이 결합도가 문제가 되는 것은 객체의 내부 구조가 외부로 노출되는 경우로 한정된다. 
* 기차 충돌처럼 보이는 코드라도 객체의 내부 구현에 대한 어떤 정보도 외부로 노출하지 않는다면 그것은 디미터 법칙을 준수한 것이다.

### 결합도와 응집도의 충돌
* 일반적으로 어떤 객체의 상태를 물어본 후 반환된 객체의 상태를 물어본 후 반환된 상태를 기반으로 결정을 내리고 그 결정에 따라 객체의 상태를 변경하는 코드는 묻지 말고 시켜라 스타일로 변경해야 한다.

```kotlin
// 낮은 응집도 높은 결합도
class Theater {
    fun enter(audience: Audience) {
        if (audience.getBag().hasInvitation()) {
            // ...
        }
    }
}

// 낮은 결합도 높은 응집도
class Audience {
    fun buy(ticket: Ticket): Long {
        if (bag.hasInvitation()) {
            // ...
        }
    }
}
```

* 이 예제에서 알 수 있는 것처럼 위임 메서드를 통해 내부 구조를 감추는 것은 협력에 참여하는 객체들의 결합도를 낮출 수 있는 동시에 객체의 응집도를 높일 수 있는 가장 효과적인 방법이다.


```kotlin
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
```

* 위 예시는 얼핏 보면 ```screening.getWhenScreened().getDayOfWeek()``` 와 같기 때문에 캡슐화를 위반한 것처럼 보일 수 있다.
* 따라서, 할인 여부 판단 로직을 Screening 으로 옮기면 묻지 말고 시켜라 스타일로 퍼블릭 인터페이스를 얻을 수 있다.
* 다만, 이렇게하면 Screening 이 기간에 따른 할인 조건을 판단하는 책임을 떠안게 된다.


* 모든 상황에서 맹목적으로 위임 메서드를 추가하면 같은 퍼블릭 인터페이스 안에 어울리지 않은 오퍼레이션들이 공존하게 된다.
* 결과적으로 객체는 상관 없는 책임들을 한꺼번에 떠안게 되기 때문에 결과적으로 응집도가 낮아진다.
* 클래스는 하나의 변경 원인만을 가져야 한다.(SRP) 서로 상관없는 책임들이 함께 뭉쳐있는 클래스는 응집도가 낮으며 작은 변경으로도 쉽게 무너질 수 있다.