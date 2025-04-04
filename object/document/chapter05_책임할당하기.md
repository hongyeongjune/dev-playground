## 책임 할당하기

* 책임에 초점을 맞춰서 설계할 때 직면하는 가장 큰 어려움은 어떤 객체에게 어떤 책임을 할당할지를 결정하기가 쉽지 않다는 것이다. 
* 따라서 올바른 책임을 할당하기 위해서는 다양한 관점에서 설계를 평가할 수 있어야 한다.


* -> 올바른 책임 할당을 위해 GRASP(General Responsibility Assignment Software Pattern) 패턴을 적용

### 책임 주도 설계를 향해

* 데이터보다 행동을 먼저 결정하라
* 협력이라는 문맥 안에서 책임을 결정하라

### 데이터보다 행동을 먼저 결정하라

* 객체에서 중요한 건 데이터가 아닌, 외부에 제공하는 행동
* 클라이언트의 관점에서 객체가 수행하는 행동이란 곧 객체의 책임을 의미한다.
* 책임 중심의 설계에서는 "이 객체가 수행해야 하는 책임은 무엇인가"를 결정한 후에 "이 책임을 수행하는 데 필요한 데이터는 무엇인가"를 결정한다.

### 협력이라는 문맥 안에서 책임을 결정하라

* 협력을 시작하는 주체는 메세지 전송자이기 때문에 메세지를 전송하는 클라이언트의 의도에 적합한 책임을 할당해야 한다.
* 객체를 가지고 있기 때문에 메세지를 보내는 것이 아니다. **메세지를 전송하기 때문에 객체를 갖게 된 것이다.**


* 클라이언트는 임의의 객체가 메세지를 수신할 것이라는 사실을 믿고 **자신의 의도를 표현한 메세지를 전송**할 뿐이다.
* 메세지를 수신하기로 결정된 객체는 메세지를 처리할 **책임**을 할당 받게 된다.
* 따라서, 메세지 전송자 관점에서 메세지 수신자는 깔끔하게 캡슐화가 된다.


* 적절한 책임이란 클라이언트 관점에서 적절한 책임을 의미한다. 클라이언트가 전송할 메세지를 결정한 후에야 비로소 객체의 상태를 저장하는데 필요한 내부 데이터에 대해 고민하면 된다.

### 책임 주도 설계

* 시스템이 사용자에게 제공해야 하는 기능인 시스템 책임을 파악한다. 
* 시스템 책임을 더 작은 책임으로 분할한다. 
* 분할된 책임을 수행할 수 있는 적절한 객체 또는 열학을 찾아 책임을 할당한다. 
* 객체가 책임을 수행하는 도중 가른 객체의 도움이 필요한 경우 이를 책임질 적절한 객체 또는 역할을 찾는다. 
* 해당 객체 또는 역할에게 책임을 할당함으로써 두 객체가 협력하게 한다.

### 책임 할당을 위한 GRASP 패턴

* General Responsibility Assignment Software Pattern 의 약자
* 객체에게 책임을 할당할 때 지침으로 삼을 수 있는 원칙들의 집합을 패턴 형식으로 정리한 것이다.

### 도메인 개념에서 출발하기

* 설계를 시작하기 전에 도메인에 대한 개략적인 모습을 그려 보는 것이 유용하다.
* 중요한 것은 설계를 시작하는 것이지 도메인 개념들을 완벽하게 정리하는 것이 아니다. 도메인 개념을 정리하는 데 너무 많은 시간을 들이지 말고 빠르게 설계와 구현을 진행하는 것이 좋다.

#### ✅ 도메인과 객체의 관계

* 도메인은 현실 세계의 개념
  * "고객이 영화를 예매한다"는 도메인 개념이지만, 그대로 코드로 만들 수는 없다. 
  * 도메인의 개념을 분석해서 소프트웨어적으로 구현할 방법을 고민해야 한다.
* 객체는 도메인을 소프트웨어적으로 표현한 것 
  * 객체는 도메인의 개념을 바탕으로 하지만, 더 정교하게 설계됨
  * "예매"라는 도메인 개념을 코드로 표현할 때, Reservation 객체뿐만 아니라 Ticket, Payment 같은 추가적인 객체가 필요할 수 있음

### 정보 전문가에게 책임을 할당하라

* 책임 주도 설계 방식의 첫 단계는 **애플리케이션이 제공해야 하는 기능**을 **애플리케이션의 책임으로 생각**하는 것이다.


* 메세지는 메세지를 수신할 객체가 아니라 메세지를 전송할 객체의 의도를 반영해서 결정해야 한다. -> **메세지를 전송할 객체는 무엇을 원하는가?**
* 메세지를 결정했으면, 메세지에 적합한 객체를 선택해야 한다. -> **메세지를 수신할 적합한 객체는 누구인가?**


* 객체에게 책임을 할당하는 첫 번째 원칙은 책임을 수행할 정보를 알고 있는 객체에게 책임을 할당하는 것이다. (정보 전문가 패턴)
  * 정보 전문가 패턴 : **객체가 자신이 소유하고 있는 정보**와 관련된 작업을 수행한다는 일반적인 직관을 표현
  * **정보**는 데이터와 다르다.
  * 책임을 수행하는 객체가 **정보**를 **알고** 있다고 해서 그 정보를 저장하고 있을 필요는 없다.
  * 객체는 해당 정보를 제공할 수 있는 다른 객체를 알고 있거나 필요한 정보를 계산해서 제공할 수 있다.

### 예시

* 영화 예매 시스템


* 📌 1단계: 가장 중요한 메세지 정하기
* 👉 "예매해라"
  * 영화를 예매하는 것이 중요하므로 첫 메세지 시작은 **예매하라**로 정한다.


* 📌 2단계: 메세지를 수신할 적절한 객체 선택 
* 👉 * 영화에 대한 정보와 상영 시간, 상영 순번처럼 영화 예매에 필요한 다양한 정보를 알고 있는 Screening 객체에 할당
  * "예매해라" 메세지를 받는 적절한 객체는 예매하는 데 필요한 정보를 가장 많이 알고 있는 객체에게 할당

* ✅ 결론
* Screening 은 필요한 정보를 직접 저장하거나 해결하지 않고 외부에 도움을 받는다. (협력)
  * 할인 여부를 판단하는 것은 DiscountCondition 에게
  * 할인 여부 결과와 함께 + 가격을 계산하는 것은 Movie 에게

### 높은 응집도와 낮은 결합도

* 책임을 할당할 수 있는 다양한 대안들이 존재한다면 **응집도와 결합도**의 측면에서 더 나은 대안을 선택하는 것이 좋다.
* **낮은 결합도 (LOW COUPLING)** : 설계의 전체적인 결합도가 낮게 유지되도록 책임을 할당하는 방법
* **높은 응집도 (HIGH COHESION)** : 높은 응집도를 유지할 수 있게 책임을 할당


* 위 예시에서 Screening 은 Movie, DiscountCondition 과 결합되어 있어 결합도가 높다.
* 그리고 Screening 은 Movie, DiscountCondition 과 관련된 책임을 모두 갖고 있으므로 응집도도 높다고 할 수 없다.
* Screening 이 가격계산과 할인여부 판단을 모두 하는 것이 아니라 Screening 이 가격 계산을 Movie 에게 요청하고 Movie 가 할인 여부를 DiscountCondition 에게 요청하면 결합도도 낮아지고 응집도가 높아진다.
* **예매하라 -> Screening -> 가격을 계산하라 -> Movie -> 할인 여부를 판단하라 -> DiscountCondition**

