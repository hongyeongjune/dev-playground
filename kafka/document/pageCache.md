## 순차 I/O

* 일반적으로 디스크 I/O 는 메모리에 비해 굉장히 느리다고 알려져 있지만, 그것은 Random Access 일 때 느리다.
* 순차적으로 데이터에 접근하는 속도는 디스크 랜덤 엑세스에 비해 150,000배 빠르고 메모리에 랜덤 엑세스 하는 것보다도 빠르다.
* 최신 OS 들은 read-ahead와 write-behind 같은 기술을 제공해 순차 읽기/쓰기 작업이 더 빠르게 수행되도록 지원한다. 카프카는 데이터를 메시지 큐 방식으로 저장하는데, 이는 순차 I/O 혜택을 볼 수 있어 빠른 성능을 제공한다.
* 카프카가 데이터를 디스크에 쓰고 읽으면서도 처리량이 뛰어난 이유 중의 하나는, 카프카의 주요 용도인 메시지 큐의 특성상 데이터를 읽고 쓰는 방식을 순차 접근으로 구성할 수 있기 때문이다.

![images](https://dl.acm.org/cms/attachment/d7e5eb0f-c89d-401e-84bb-c85042b4c072/jacobs3.jpg)

## Page Cache

* 최신 운영체제들은 디스크 I/O 의 성능을 향상시키기 위해 Page Cache 라는 메모리영역을 만들어서 사용한다. 한 번 읽은 파일의 내용을 Page Cache 영역에 저장시켜놨다가 다시 한 번 동일한 파일 접근이 일어나면 디스크에서 읽지 않고 Page Cache 에서 읽어서 제공하는 방식이다.
* 디스크 검색을 줄이고 처리량을 높이기 위해 최신 운영체제들은 Page Cache 를 위해 메인 메모리를 더 공격적으로 사용하게 되었다.
* 모든 디스크 읽기/쓰기는 페이지 캐시를 거치게 되며, 사용자나 어플리케이션에 의해 관리되는게 아니라 운영체제에 의해 관리되므로 사용자 영역과 커널 영역의 중복 저장 없이 2배 정도의 캐시를 저장할 수 있다. 또한, 이 기능을 강제로 끄는 것은 쉽지 않으므로, 애플리케이션에서 내부적으로 캐시를 사용한다면 실제로는 같은 내용을 운영체제의 Page Cache 와 애플리케이션 내부 캐시에 중복 저장하게 된다.
* 카프카는 JVM 위에서 동작하는데 힙 메모리에 객체를 저장하는 비용은 매우 비싸고, 힙 메모리가 커질수록 GC가 느려진다는 단점이 있다.
* 따라서, 카프카는 힙 메모리 내에 객체로 저장하는 대신 바이트 구조로 압축하고 운영체제가 훌륭하게 최적화하고 있는 Page Cahce 를 활용해서 디스크에 저장하는 방식을 도입해서 성능을 높일 수 있었다.
* Page Cahce 는 애플리케이션 수준이 아니라 운영체제 수준에서 관리되므로, 애플리케이션 서비스 재시작 시에도 별도의 워밍업 없이도 좋은 성능을 낼 수 있다.

![images](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*rjlyW4hhBaJGqVJfKu7dSw.png)

## Zero Copy

* Zero Copy : CPU 의 개입을 받지 않고 한 메모리의 영역에서 다른 메모리의 영역으로 데이터를 카피하는 작업을 말한다.

### 일반적인 데이터 전송 흐름

![images](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*wb6XhkM0vKf9YxyEHLlvsw.png)

* 운영체제는 디스크로부터 데이터를 읽어 커널 영역의 Page Cache 에 저장한다.
* 애플리케이션은 Page Cache 의 데이터를 사용자 영역으로 읽어온다.
* 애플리케이션은 커널 영역에 있는 Socket Buffer 로 데이터를 쓴다.
* 운영체제는 Socket Buffer 에 있는 데이터를 Network Interface Card Buffer 로 복사하고 네트워크를 통해 전송한다.

### Kafka 에서 Zero Copy

* 위 과정에서 4번의 copy 와 2번의 system call 이 발생한다.
* 운영체제가 제공하는 sendFile 함수는 커널 영역의 Page Cache 에서 Network Interface Card Buffer 로 직접 복사가 가능해 효율적으로 데이터를 전송할 수 있다.
* Page Cache 와 Zero Copy 의 조합을 통해 카프카 클러스터에서는 데이터가 Page Cache 에서 직접 Network Interface Card Buffer 로 직접 전송되므로 디스크 읽기가 전혀 발생하지 않기도 한다.

> https://medium.com/sjk5766/kafka-disk-i-o%EA%B0%80-%EB%B9%A0%EB%A5%B8-%EC%9D%B4%EC%9C%A0-899c4da5084
> https://github.com/HomoEfficio/dev-tips/blob/master/Kafka%20%EB%91%98%EB%9F%AC%EB%B3%B4%EA%B8%B0.md
