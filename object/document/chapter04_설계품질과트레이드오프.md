* 객체지향 설계란 올바른 객체에게 올바른 **책임**을 할당하면서 **낮은 결합도**와 **높은 응집도**를 가진 구조를 창조하는 활동이다.
* 높은 응집도와 낮은 결합도를 가지기 위해서는 객체의 상태가 아니라 **객체의 행동에 초점**을 맞춰야 한다.

### 책임 vs 데이터
* 책임을 중심으로 분할
  * 다른 객체가 요청할 수 있는 오퍼레이션을 위해 필요한 상태를 보관
  * 책임이 무엇인지 확인하며 시작
* 상태(데이터)를 중심으로 분할
  * 객체는 자신이 포함하고 있는 데이터를 조작하는 데 필요한 오퍼레이션을 정의
  * 객체가 내부에 저장해야하는 데이터가 무엇인지 확인하며 시작


### 캡슐화 위반

* 데이터 중심 설계에서 객체의 상태(private 필드)를 숨긴다고 해서 반드시 책임 중심 설계가 되는 것은 아니다. 
* ✅ 핵심 차이점은 단순히 상태를 ```private```으로 가리느냐가 아니라, 객체가 행동(책임)을 중심으로 상태를 관리하는가 여부에 달려 있다. 
* ✅ 외부에서 상태를 직접 변경할 수 있는 get/set 메서드는 캡슐화를 깨뜨리는 주요 원인이다. 
* ✅ 객체의 상태 변경은 반드시 객체 내부의 "행동"을 통해 이루어져야 한다.

#### 🚨 데이터 중심 설계 (private 으로 캡슐화를 유지한 것처럼 보이지만, 캡슐화를 위반하고 데이터 중심임)
```kotlin
class Money(private val amount: Double) {
    fun getAmount(): Double = amount
}

class Movie {
    private var fee: Money = Money(10000.0)  // 상태를 가지고 있음

    fun getFee(): Money {  // 외부에서 직접 조회 가능 ❌
        return fee
    }

    fun setFee(newFee: Money) {  // 외부에서 직접 변경 가능 ❌
        fee = newFee
    }
}

class MovieService {
    fun applyDiscount(movie: Movie, discountAmount: Double) {
        val currentFee = movie.getFee().getAmount()  // ❌ 상태를 노출
        val newFee = Money(currentFee - discountAmount)
        movie.setFee(newFee)  // ❌ 외부에서 상태 변경
    }
}

fun main() {
    val movie = Movie()
    val service = MovieService()
    service.applyDiscount(movie, 2000.0) // 가격 할인

    println("할인 후 영화 가격: ${movie.getFee().getAmount()} 원") // ❌ 직접 접근
}

```

#### ✅ 책임 중심 설계
```kotlin
class Money(private val amount: Double) {
    fun minus(discount: Double): Money {
        return Money(amount - discount)
    }

    fun getAmount(): Double = amount
}

class Movie(private var fee: Money) {  // 상태를 가지고 있음

    fun applyDiscount(discountAmount: Double) {  // ✅ Movie가 직접 할인 적용
        fee = fee.minus(discountAmount)
    }

    fun getFeeAmount(): Double {  // 내부 구현을 완전히 감추지는 않음 (읽기 가능)
        return fee.getAmount()
    }
}

fun main() {
    val movie = Movie(Money(10000.0))
    movie.applyDiscount(2000.0)  // ✅ Movie 객체에 메시지를 보냄 (책임을 위임)

    println("할인 후 영화 가격: ${movie.getFeeAmount()} 원") // ✅ 내부 상태를 노출하지 않고 필요한 값만 제공
}
```

### 높은 결합도

* 결합도가 높은 원인은 대부분 캡슐화가 내부 구현을 그대로 노출하기 때문이다. 객체 내부의 구현이 객체의 인터페이스에 드러난다는 것은 클라이언트가 구현에 강하게 결합된다는 것을 의미한다.

#### 🚨 데이터 중심 설계의 결합도 문제

```kotlin
class Money(private val amount: Double) {
    fun getAmount(): Double = amount  // ❌ 내부 상태를 그대로 노출
}

class Movie {
    private var fee: Money = Money(10000.0)

    fun getFee(): Money {  // ❌ 외부에서 직접 fee에 접근 가능
        return fee
    }

    fun setFee(newFee: Money) {  // ❌ 외부에서 직접 fee 변경 가능
        fee = newFee
    }
}

class MovieService {
    fun applyDiscount(movie: Movie, discountAmount: Double) {
        val currentFee = movie.getFee().getAmount()  // ❌ 내부 상태 직접 조회
        val newFee = Money(currentFee - discountAmount)
        movie.setFee(newFee)  // ❌ 내부 상태 직접 변경
    }
}
```

#### ✅ 책임 중심 설계로 결합도 낮추기

```kotlin
class Money(private val amount: Double) {
    fun minus(discount: Double): Money {
        return Money(amount - discount)
    }

    fun getAmount(): Double = amount
}

class Movie(private var fee: Money) {  

    fun applyDiscount(discountAmount: Double) {  // ✅ Movie가 직접 할인 책임을 가짐
        fee = fee.minus(discountAmount)
    }

    fun getFeeAmount(): Double {  // ✅ 내부 상태를 직접 노출하지 않음
        return fee.getAmount()
    }
}

fun main() {
    val movie = Movie(Money(10000.0))
    movie.applyDiscount(2000.0)  // ✅ 클라이언트가 Movie에 메시지를 보냄

    println("할인 후 영화 가격: ${movie.getFeeAmount()} 원") // ✅ 결합도 낮음
}
```

### 낮은 응집도

* 서로 다른 이유로 변경되는 코드가 하나의 모듈 안에 공존할 때 모듈의 응집도가 낮다고 말한다. 
* 변경의 이유가 서로 다른 코드들을 하나의 모듈 안에 뭉쳐 놓았기 때문에 변경과 아무 상관이 없는 코드들이 영향을 받게 된다.
* 하나의 요구사항 변경을 반영하기 위해 동시에 여러 모듈을 수정해야 한다. 
* 응집도가 낮을 경우 다른 모듈에 의지해야 할 책임의 일부가 엉뚱한 곳에 위치하게 되기 때문이다.

### 📌 데이터 중심 설계는 객체의 행동보다는 상태에 초점을 맞춘다

* 데이터 중심의 설계를 시작할 때 던졌던 첫 번째 질문은 "이 객체가 포함해야 하는 데이터가 무엇인가?"다. 데이터는 구현의 일부이다.
* 데이터 주도 설계는 시작하는 처음부터 데이터에 관해 결정하도록 강요하기 때문에 너무 이른 시기에 내부 구현에 초점을 맞춘다.
* 데이터 중심의 관점에서 객체는 그저 단순한 데이터의 집합체일 뿐이다. 
* 이로 인해 접근자와 수정자를 과도하게 추가하게 되고 이 데이터 객체를 사용하는 절차를 분리된 별도의 객체 안에 구현하게 된다. 
* 결론적으로 데이터 중심의 설계는 너무 이른 시기에 데이터에 대해 고민하기 때문에 캡슐화에 실패하게 된다. 
* 객체의 내부 구현이 객체의 인터페이스를 어지럽히고 객체의 응집도와 결합도에 나쁜 영향을 미치기 때문에 변경에 취약한 코드를 낳게 된다.

### 📌 데이터 중심 설계는 객체를 고립시킨 채 오퍼레이션을 정의하도록 만든다.

* 올바른 객체지향 설계의 무게 중심은 항상 객체의 내부가 아니라 **외부**에 맞춰져 있어야 한다 객체가 내부에 어떤 상태를 가지고 그 상태를 어떻게 관리하는가는 부가적인 문제다.
* 안타깝게도 데이터 중심 설계에서 초점은 객체의 외부가 아니라 내부로 향한다. 
* 실행 문맥에 대한 깊이 있는 고민 없이 객체가 관리할 데이터의 세부 정보를 먼저 결정한다. 
* 객체의 구현이 이미 결정된 상태에서 다른 객체와의 협력 방법을 고민하기 때문에 이미 구현된 객체의 인터페이스를 억지로 끼워맞출수밖에 없다.