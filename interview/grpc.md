### rpc 란?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

* RPC 는 네트워크로 연결된 서버 상의 프로시저(함수, 메서드 등)를 원격으로 호출할 수 있는 기능
* 코드 상으로는 마치 로컬 함수의 호출과 같지만 실제로는 함수가 원격 서버에서 실행된다.
    * 네트워크 통신을 위한 작업 하나하나 챙기기 귀찮으니 통신이나 call 방식에 신경 쓰지 않고 원격지의 자원을 내 것처럼 사용할 수 있다는 의미
* IDL(Interface Definication Language) 기반으로 다양한 언어를 가진 환경에서도 쉽게 확장이 가능하며, 인터페이스 협업에도 용이하다는 장점이 있다.
* IDL 은 "서버와 클라이언트가 서로 이해할 수 있도록 함수, 메시지 구조를 미리 정의하는 언어"
* gRPC에서는 IDL로 Protocol Buffers (.proto 파일) 를 사용합니다.
* RPC 의 핵심 개념은 Stub(스텁)이다.
    * 서버와 클라이언트는 서로 다른 주소 공간을 사용하므로, 함수 호출에 사용된 매개 변수를 꼭 변환해줘야 한다.
    * 변환하지 않는다면 메모리 매개 변수에 대한 포인터가 다른 데이터를 가리키게 되기 때문인데, 이 변환을 담당하는 게 스텁이다.
* Client Stub 은 함수 호출에 사용된 파라미터의 변환(Marshalling, 마샬링) 및 함수 실행 후 서버에서 전달된 결과의 변환을, Server Stub 은 클라이언트가 전달한 매개 변수의 역변환(Unmarshalling, 언마샬링) 및 함수 실행 결과 변환을 담당하게 된다.

</details>

### 기본적인 RPC 통신 과정은?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlFcne%2FbtsGB2v99QC%2FVkOQ0RzIwOmJL1CcLs7dOk%2Fimg.jpg)

1. IDL 을 사용하여 호출 규악을 정의 (함수명, 인자, 반환값에 대한 데이터형이 정의된 IDL 파일을 rpcgen 으로 컴파일하면 stub code 가 자동으로 생성됨)
2. Stub Code 에 명시된 함수는 원시코드의 형태로, 상세 기능은 Server 에서 구현된다.
3. Client 에서 Stub 에 정의된 함수를 사용할 때, Client Stub 은 RPC Runtime 을 통해 함수를 호출하고 Server 는 수신된 Procedure 호출에 대한 처리 후 결과 값을 반환한다.
4. 최종적으로 Client 는 Server 의 결과 값을 반환받고, 함수를 로컬에 있는 것처럼 사용할 수 있다.

------------------------

1. IDL 로 인터페이스를 정의한다.
2. IDL 파일을 컴파일하면 Stub 이 생성된다.
3. Client 에서 Stub 을 호출한다.
4. Stub이 내부적으로 요청 데이터를 직렬화하여 네트워크로 전송한다.
5. 서버에서 요청을 수신하고, 역직렬화하여 실제 구현체(Service 구현체)를 호출한 후 응답 값을 반환한다.
6. 클라이언트는 Stub으로부터 응답을 받고, 역직렬화하여 결과를 전달받는다.

</details>

### gRPC 란?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

* gRPC 는 google 사에서 개발한 오픈소스 RPC(Remote Procedure Call) 프레임워크이다.
* 이전까지는 RPC 기능은 지원하지 않고, 메세지(JSON 등)를 Serialize 할 수 있는 프레임워크인 PB(Protocol Buffer, 프로토콜 버퍼)만을 제공해 왔는데, google 에서 PB 기반 Serizlaizer 에 HTTP/2를 결합한 새로운 RPC 프레임워크를 탄생시켰다.

</details>

### gRPC 장점은?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

* http/1.1은 기본적으로 클라이언트의 요청이 올 때만 서버가 응답을 하는 구조로 매 요청마다 connection을 생성해야만 한다.
* cookie 등 많은 메타 정보들을 저장하는 무거운 header 가 요청마다 중복 전달되어 비효율적이고 속도도 느려진다.
* http/2에서는 한 connection 으로 동시에 여러 개 메시지를 주고받으며, header 를 압축하여 중복 제거 후 전달하기에 1.x에 비해 효율적이다.
* 또한, 필요시 클라이언트 요청 없이도 서버가 리소스를 전달할 수도 있기 때문에 클라이언트 요청을 최소화할 수 있다.

</details>

### gRPC 통신 과정은?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

1. 클라이언트 Stub 은 RPC 함수를 호출한다.
2. 클라이언트 Stub 은 인코딩 메시지로 application/grpc 접두어가 붙은 Content-Type 을 가진 HTTP POST 요청을 생성한다. 이 때, 호출하는 원격함수는 별도의 HTTP 헤더로 전송된다. (ex. :method POST :path /TitleGroup/getTitleGroup)
3. HTTP 요청 메세지는 네트워크를 통해 서버 머신으로 전송
4. 서버에 메세지가 수신되면, 서버는 메세지 헤더를 검사해 어떤 서비스 함수를 호출해야하는지 확인하고 메세지를 서비스 Stub 에 넘긴다.
5. 서비스 Stub 은 메세지 바이트를 언어별 데이터 구조로 파싱한다.
6. 파싱된 메시지를 사용해 서비스는 RPC 함수를 로컬로 호출한다.
7. 서비스 함수의 응답이 인코딩되어 클라이언트로 다시 전송된다. (응답 메세지는 클라이언트에서와 동일한 절차를 따른다.)

------------------------

1. .proto (IDL) 정의 후, Protobuf 컴파일러로 Stub 및 Service 코드 생성 
2. 클라이언트는 Stub을 통해 gRPC 메서드를 호출 
3. Stub은 요청 메시지를 Protobuf 바이너리로 직렬화하고, application/grpc 헤더와 함께 HTTP/2 POST 요청을 전송 
4. ManagedChannel은 HTTP/2 기반으로 서버와 통신 (헤더+바디는 HTTP/2 프레임으로 분리되어 전송됨)
5. 서버는 HTTP/2 요청을 수신하고 Protobuf 역직렬화 후, 실제 서비스 로직을 실행 
6. 서버는 응답 메시지를 Protobuf로 직렬화하여 클라이언트로 전송 
7. 클라이언트 Stub은 응답을 역직렬화하여 사용자에게 반환

</details>

### protobuf 란?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FczOKQW%2FbtsGCuseeeR%2FWtRJTUBUdvjAnSk69KgDX0%2Fimg.png)

* ProtoBuf 는 google 사에서 개발한 구조화된 데이터를 직렬화(Serialization)하는 기법이다.
* 직렬화란, 데이터 표현을 바이트 단위로 변환하는 작업을 의미한다.
* 위 그림처럼 같은 정보를 저장해도 text 기반인 json 인 경우 82 byte 가 소요되는데 반해, 직렬화된 protocol buffer 는 필드 번호, 필드 유형 등을 1byte 로 받아서 식별하고, 주어진 length 만큼만 읽도록 하여 단 33 byte 만 필요하게된다.
* Proto Type 의 경우, 기본적으로 null 값을 허용하지 않는다.
* null 값을 허용하기 위해서는 별도의 google 에서 제공하는 wrapper 타입을 사용해야 한다. 
* Protobuf는 데이터를 이진(binary) 형식으로 직렬화합니다. 
* 즉, 사람이 읽을 수 없는 형태지만 컴퓨터가 매우 빠르게 읽고 쓸 수 있는 포맷입니다.

</details>

### Stub 이란?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

stub은 함수 호출에 사용한 파라미터를 직렬화하고 서버에서 전달된 결과를 역직렬화하는 기능을 담당한다.  
grpc의 client 역할을 한다고 보면 된다.
gRPC에서 stub은 클라이언트가 서버에 있는 gRPC 서비스를 호출할 수 있도록 도와주는 프록시 객체입니다.

</details>

### Channel 이란?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

gRPC에서 Channel은 클라이언트가 서버에 RPC 요청을 보낼 수 있도록 해주는 논리적 연결 객체입니다.
내부적으로는 HTTP/2 기반의 스트리밍 연결을 유지하며, Stub이 이 Channel을 통해 서버로 요청을 전달합니다.

gRPC 통신의 기본 흐름

```[Client Stub] ---(요청)---> [Channel] ---(HTTP/2)--> [gRPC Server]```

Stub: 서버 메서드를 호출하는 프록시 객체 (클라이언트에 자동 생성됨)
Channel: Stub과 실제 서버 사이의 네트워크 통신을 담당
HTTP/2: Channel 내부의 전송 프로토콜 (멀티플렉싱, 스트리밍 지원)

일반적으로 Channel은 애플리케이션 초기화 시 1회 생성하여, 여러 Stub이 공유하도록 설계합니다.
Channel은 로드밸런싱, 연결 풀 관리, 보안 설정 등을 포함하고 있어서, 단순한 Socket 이상의 기능을 담당합니다.

Channel은 gRPC 클라이언트가 서버와 통신하는 연결 추상화입니다.  
내부적으로는 HTTP/2 커넥션을 유지하면서, 각 RPC 호출마다 Stream을 열고 프레임을 주고받는 역할을 합니다.  

</details>

### gRPC 인증은 어떻게 하는가?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

TLS 통신을 위해 channel에 세팅할 수 있고 mTLS도 지원한다.


</details>

### mTLS 란?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

* mTLS 는 TLS 의 확장된 형태로, TLS 는 서버의 신원만을 인증하는 반면 mTLS 는 서버뿐만 아니라 클라이언트도 자신의 신원을 인증서를 통해 증명해야 하는 상호 인증 방식이다.
* 이는 양방향 인증이라고도 하며, 서로의 인증서를 검증함으로써 보다 강화된 보안을 제공한다.

</details>

### TLS 의 통신과정은?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

![images](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbxXC3v%2FbtsG8R1eVoB%2FVPnO1UcErtxd2QaoP2DVc0%2Fimg.png)

1. 클라이언트가 서버에 연결
2. 서버가 TLS 인증서를 제시
3. 클라이언트가 서버의 인증서를 확인
4. 클라이언트와 서버가 암호화된 TLS 연결을 통해 정보를 교환

</details>

### mTLS 의 통신과정은?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

* 일반적으로 TLS 에서 서버는 TLS 인증서와 공개/개인 키 쌍이 있지만 클라이언트에는 없다.
* 그러나 mTLS 에서는 클라이언트와 서버 모두에 인증서가 있고 양측 모두 공개/개인 키 쌍을 사용하여 인증한다.
* 일반 TLS 와 비교하여 mTLS 에는 양 당사자를 확인하기 위한 추가 단계가 있다.
* mTLS 는 조직 내의 사용자, 장치, 서버를 확인하기 위해 Zero Trust 보안 프레임워크에서 자주 사용된다. API 엔드포인트를 확인하여 승인되지 않은 당사자가 잠재적으로 악의적인 API 요청을 보낼 수 없도록 하여 API를 안전하게 유지하는 데에도 도움이 될 수 있다.
    * Zero Trust 는 사용자, 장치, 네트워크 트래픽이 기본적으로 신뢰할 수 없음을 의미하며, 이는 많은 보안 취약점을 제거하는 데 도움이 되는 접근 방식이다.

![images](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FceAymP%2FbtsG9xOKDSP%2Fes5aMx8TdMbedC2c4Pc4wk%2Fimg.png)

1. 클라이언트가 서버에 연결
2. 서버가 TLS 인증서를 제시
3. 클라이언트가 서버의 인증서를 확인
4. 클라이언트가 TLS 인증서를 제시
5. 서버가 클라이언트의 인증서를 확인
6. 서버가 액세스 권한을 부여
7. 클라이언트와 서버가 암호화된 TLS 연결을 통해 정보를 교환

</details>

### gRPC 에서 로드밸런싱은 어떻게 하는가?

<details>
   <summary> 예비 답안 보기 (👈 Click)</summary>

-----------------------

L4 로드 밸런서는 http1.x의 경우 client와 server가 직접 connection을 맺는다. 그리고 복수의 요청을 처리하기 위해 복수의 connection을 생성한다.
grpc의 경우 L4를 사용하게 되면 적절하게 로드밸런싱이 안되는 이슈가 생긴다. http 2.0을 사용하는데 multiplex stream 기능으로 인해 하나의 커넥션을 맺고 여러 데이터를 전송하게 되기 때문이다.
따라서, Traefik 같은 L7 로드 밸랜서의 경우, L4와 달리 애플리케이션 레벨에서 동작하기 때문에 HTTP/2 프로토콜을 이해하고 스트림 요청을 개별적으로 로드밸런싱 할 수 있다. 
또한, client와 LB가 커넥션을 맺고 LB와 서버가 커넥션을 맺기 때문에 L7 로드밸런서에서 http2 프로토콜을 이해하고 적절하게 로드밸런싱 해줄 수 있다.

</details>