객체지향 패러다임 관점에서 핵심은 **역할(role)**, **책임(responsibility)**, **협력(collaboration)** 이다.

### 협력

* 객체지향 시스템은 자율적인 객체들의 공동체다.
* 객체는 고립된 존재가 아니라 시스템의 기능이라는 더 큰 목표를 달성하기 위해 다른 객체와 **협력**하는 사회적인 존재다.
  * 협력이란 어떤 객체가 다른 객체에게 무엇인가를 요청하는 것 
* 객체는 다른 객체의 상세한 내부 구현에 직접 접근하지 않고, 오직 외부에 공개된 인터페이스(메서드) 호출을 통해서만 자신의 요청을 전달할 수 있다.
* 메서드 호출을 받은 객체는 메서드를 실행하여 요청에 응답한다.
* 외부의 객체는 오직 메서드 호출만 할 수 있을 뿐 내부적으로 어떻게 처리되는지는 알지 못하고, 메서드를 호출을 수신한 객체가 직접 결정한다.
* 즉, 객체를 자율적으로 만드는 가장 기본적인 방법은 내부 구현을 **캡슐화** 하는 것이다.
* 객체란 상태와 행동을 캡슐화하는 실행 단위 -> **어떤 객체가 필요**하다면 그 이유는 **객체가 어떤 협력에 참여**하고 있기 때문이고, **협력에 참여할 수 있는 이유는** 협력에 필요한 **적절한 행동**을 보유하고 있기 때문이다.
* 즉, **객체의 행동**을 결정하는 것이 **협력**이고 **객체의 상태**를 결정하는 것은 **행동**이다.
  * 객체의 상태는 행동을 수행하는데 필요한 정보가 무엇인지로 결정
* 객체는 자신의 상태를 스스로 결정하고 관리하는 자율적인 존재이므로 객체가 수행하는 행동에 필요한 상태도 함께 가지고 있어야한다.


* **협력**은 객체를 설계하는데 필요한 일종의 **문맥**을 제공한다.

### 책임

* 협력에 필요한 행동을 수행할 수 있는 적절한 객체를 찾는게 중요한데, 이떄 협력에 참여하기 위해 객체가 수행하는 행동을 **책임**이라고 부른다.
* 책임은 객체가 수행해야 하는 행동(Behavior)과 정보(Knowledge)의 집합이다. 
* 객체는 협력 속에서 자신의 역할을 수행하기 위해 책임을 가진다. 
* 책임은 크게 두 가지로 나뉜다. 
  * 하는 것(Doing) → 행동을 수행하는 책임 (ex. 영화를 예매해라 -> 행동)
  * 아는 것(Knowing) → 정보를 알고 있는 책임 (ex. Screening 객체는 자신이 상영할 영화(Movie)를 알고 있어야 한다. -> 상태)


* 자율적인 객체를 만드는 가장 기본적인 방법은 **책임을 수행하는 데 필요한 정보를 가장 잘 알고 있는 객체에게 그 책임을 할당**하는 것
* 객체에게 책임을 할당하기 위해서는 먼저 **협력이라는 문맥을 정의**해야 한다.
  * 객체들 역시 협력에 필요한 지식과 방법을 가장 잘 알고 있는 객체에게 도움을 요청
  * 요청에 응답하기 위해 필요한 이 행동이 객체가 수행할 책임
  * 따라서, 협력이라는 문맥을 알아야 책임도 지정할 수 있음


* 어떤 객체를 만들지 고민하기 전에, 먼저 어떤 메시지를 주고받을지를 고민해야 한다.
* 즉, "객체를 먼저 설계하고 메시지를 나중에 정하는 것이 아니라, 메시지를 먼저 정의하고 그에 맞는 객체를 만들어야 한다."


* ❌ 안 좋은 접근 방식: 먼저 객체를 정의하고 나서 메시지를 결정하는 방식
  * "음... 주문(Order)이라는 객체가 필요하겠네!"
  * "결제(Payment) 객체도 필요하고..."
  * "그리고 결제 정보를 Order에 저장해야겠지?"
  * "어떤 메시지를 주고받을지는 나중에 고민하자!" ❌

* ✅ 좋은 접근 방식: 어떤 메시지가 오가야 하는지 먼저 고민하고, 그 메시지를 처리할 객체를 결정
  * "주문을 처리할 때, 어떤 메시지가 필요할까?"
  * "주문이 생성될 때 '결제 요청'이라는 메시지가 필요하겠네."
  * "그럼 결제 요청을 받아서 처리하는 객체가 필요하겠군!"
  * "이제 객체를 정의해야겠다!"

```kotlin
class OrderService(private val paymentProcessor: PaymentProcessor) {
    fun placeOrder(item: String, quantity: Int, price: Double) {
        println("$item 상품을 $quantity개 주문 완료")
        paymentProcessor.processPayment(price * quantity)
    }
}

interface PaymentProcessor {
    fun processPayment(amount: Double)
}

class CreditCardPayment : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("신용카드로 $amount 원 결제")
    }
}

class BankTransferPayment : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("계좌이체로 $amount 원 결제")
    }
}
```

* 중요 포인트 1 : 객체가 최소한의 인터페이스를 가질 수 있게 된다.
* ✔️ 최소한의 인터페이스란? 
  * 인터페이스(Interface) 는 객체가 외부와 소통하는 "공개된 메시지의 집합" 이다. 
  * 메시지를 먼저 설계하면 불필요한 메시지를 줄이고, 꼭 필요한 메시지만 인터페이스에 포함할 수 있다. 
  * 이를 통해 "작고 단순한 인터페이스" 를 만들 수 있으며, 이는 유지보수성과 확장성을 높인다.

* 중요 포인트 2 : 객체는 충분히 추상적인 인터페이스를 가질 수 있게 된다.
* ✔️ 추상적인 인터페이스란? 
  * 추상적인 인터페이스는 구체적인 구현을 노출하지 않고, 역할(Role)만을 정의하는 인터페이스를 의미한다. 
  * 메시지를 먼저 설계하면 특정 구현에 종속되지 않은, 유연한 인터페이스를 만들 수 있음. 
  * 이는 객체 간 결합도를 낮추고, 확장성을 극대화하는 효과가 있다.

```kotlin
// 🚨 안 좋은 설계 1 (불필요한 인터페이스 포함)
// 🚨 안 좋은 설계 2 (구체적인 구현이 인터페이스에 포함됨)
interface PaymentProcessor {
    fun processCreditCard(amount: Double)
    fun processBankTransfer(amount: Double)
    fun processPayPal(amount: Double)
}

// ✅ 좋은 설계 1 (최소한의 인터페이스)
// ✅ 좋은 설계 2 (충분히 추상적인 인터페이스)
interface PaymentProcessor {
    fun processPayment(amount: Double)
}

// ✅ 좋은 설계 2 (추상적인 인터페이스 재정의)
class CreditCardPayment : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("신용카드로 $amount 원 결제")
    }
}

// ✅ 좋은 설계 2 (추상적인 인터페이스 재정의)
class BankTransferPayment : PaymentProcessor {
    override fun processPayment(amount: Double) {
        println("계좌이체로 $amount 원 결제")
    }
}
```

### 역할

* 역할은 객체가 어떤 특정한 협력 안에서 수행하는 책임의 집합
  * 하나의 역할은 여러 개의 책임을 가질 수 있으며, 어떤 메시지를 처리해야 하는지를 결정하는 개념이라고 볼 수 있다. 
* 역할은 유연하고 재사용 가능한 협력을 얻을 수 있다.

```kotlin
// ❌ 안 좋은 설계: 역할을 고려하지 않은 경우
class CreditCardPayment {
    fun processPayment(amount: Double) {
        println("신용카드로 $amount 원 결제 완료")
    }
}

class BankTransferPayment {
    fun processPayment(amount: Double) {
        println("계좌이체로 $amount 원 결제 완료")
    }
}

// ✅ 좋은 설계: 역할을 활용한 경우
interface Payment {
    fun processPayment(amount: Double)
}

class CreditCardPayment : Payment {
    override fun processPayment(amount: Double) {
        println("신용카드로 $amount 원 결제 완료")
    }
}

class BankTransferPayment : Payment {
    override fun processPayment(amount: Double) {
        println("계좌이체로 $amount 원 결제 완료")
    }
}
```

* ✅ 해결된 문제 
  * Payment 역할(인터페이스)을 정의하고, CreditCardPayment, BankTransferPayment 가 이를 수행하도록 만듦 
  * OrderService 와 같은 서비스에서 결제 역할만 알고 있으면, 특정 결제 방식에 의존할 필요 없음 
  * 새로운 결제 방식(PointPayment)이 추가되어도, 기존 코드를 수정하지 않고 확장 가능!