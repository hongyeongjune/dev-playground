### 스레드와 코루틴의 차이는?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

Thread  
* 작업 하나하나의 단위 : Thread
* 각 Thread 가 독립적인 Stack 메모리 영역 가짐
* 동시성 보장 수단 : Context Switching
* 운영체제 커널에 의한 Context Switching 을 통해 동시성 보장
* 블로킹 (Blocking) : Thread A 가 Thread B 의 결과가 나오기까지 기다려야 한다면, Thread A 은 블로킹되어 Thread B 의 결과가 나올 때 까지 해당 자원을 못 씀

Coroutine  
* 작업 하나하나의 단위 : Coroutine Object
* 여러 작업 각각에 Object 를 할당함
* Coroutine Object 도 엄연한 객체이기 때문에 JVM Heap 에 적재 됨 (코틀린 기준)
* 동시성 보장 수단 : Programmer Switching (No-Context Switching)
* 프로그래머의 코드를 통해 Switching 시점을 마음대로 정함 (OS 관여 X)
* Suspend (Non-Blocking) : Object 1 이 Object 2 의 결과가 나오기까지 기다려야 한다면, Object 1 은 Suspend 되지만, Object 1 을 수행하던 Thread 는 그대로 유효하기 때문에 Object 2 도 Object 1 과 동일한 Thread 에서 실행될 수 있음
* Coroutine Object 는 Heap 영역에 생성된다.

Stack  
* 기본 자료형(원시 자료형, Primitive type), 지역변수, 매개변수가 저장되는 메모리.(int, double, boolean, byte)
* Heap 영역에 생성된 데이터의 참조값이 할당됨
* 메소드가 호출될 때 메모리에 할당, 메서드 종료시 메모리에서 삭제됨.
* 자료구조 Stack의 구조이다, LIFO(Last In First Out)
* 각 Thread 마다 자신만의 Stack 을 가진다. (1:1) - (Thread : Stack)
* Thread는 내부적으로 Static, Heap, Stack 영역을 가진다.
* Thread는 다른 Thread에 접근 할 수 없지만, static, Heap 영역을 공유하여 사용 가능.

Heap  
* JVM이 관리하는 프로그램 상에서 데이터를 저장하기 위해 런타임 시 동적으로 할당하여 사용하는 영역 참조형(Reference Type) 데이터 타입을 갖는 객체(인스턴스), 배열 등이 저장 되는 공간
* 단, Heap 영역에 있는 오브젝트들을 가리키는 레퍼런스 변수는 stack에 적재
* Heap 영역은 Stack 영역과 다르게 보관되는 메모리가 호출이 끝나더라도 삭제되지 않고 유지된다.그러다 어떤 참조 변수도 Heap 영역에 있는 인스턴스를 참조하지 않게 된다면, GC(가비지 컬렉터)에 의해 메모리에서 청소된다.
* stack은 스레드 갯수마다 각각 생성되지만, heap은 몇개의 스레드가 존재하든 상관없이 단 하나의 heap 영역만 존재
* Heap 영역은 모든 Thread 들이 공유

</details>

### 코루틴에서 스레드를 대체할 수 있는 이유는? 

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

코루틴은 스레드보다 훨씬 가볍기 때문에, 하나의 스레드에서 수천 개의 코루틴을 실행할 수 있습니다.  
스레드처럼 Stack을 사용하지 않고, Heap에 상태만 저장하고 필요할 때만 실행되므로, CPU나 메모리를 훨씬 효율적으로 사용할 수 있습니다.
또한 코루틴은 suspend 기능을 통해 I/O 작업에서 스레드를 점유하지 않고 일시 정지할 수 있어, 스레드보다 높은 동시성 처리량을 갖습니다.
코루틴은 스레드보다 적은 리소스로 더 많은 동시 작업을 처리할 수 있기 때문에 스레드를 대체할 수 있습니다.  

</details>

### 코루틴에서 suspend function 이란?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

suspend 함수는 일시 중단 가능한 함수입니다.  
코루틴 내부에서만 호출할 수 있으며, 호출 도중 delay, I/O, withContext 등의 작업을 만나면 **현재 상태를 저장하고 중단(suspend)**됩니다.  
중단된 후에는 **다시 원래 위치부터 자동으로 재개(resume)**되며, 이 모든 과정이 Stack이 아니라 Heap에 있는 객체로 관리됩니다.  
suspend 함수는 코루틴의 핵심 구성 요소로, 비동기 작업을 직관적으로 구현할 수 있게 해줍니다.  

</details>

### 코루틴이 비동기 처리에 어떻게 활용될 수 있는가?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

코루틴은 suspend 키워드와 withContext, async, launch 같은 빌더를 사용하여, 비동기 I/O 작업을 마치 동기처럼 순차적으로 표현할 수 있게 해줍니다.
예를 들어, 파일 읽기, DB 쿼리, 외부 API 호출을 할 때 기존에는 콜백이나 Future를 사용했지만, 코루틴을 쓰면 val result = api.get()처럼 간단하게 표현하면서도 실제로는 논블로킹으로 작동합니다.
복잡한 콜백 없이 비동기 처리를 자연스럽게 구현할 수 있어 유지보수성과 가독성이 크게 향상됩니다.  

</details>