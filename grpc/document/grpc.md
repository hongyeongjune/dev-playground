## RPC
* RPC 는 네트워크로 연결된 서버 상의 프로시저(함수, 메서드 등)를 원격으로 호출할 수 있는 기능
* 코드 상으로는 마치 로컬 함수의 호출과 같지만 실제로는 함수가 원격 서버에서 실행된다.
    * 네트워크 통신을 위한 작업 하나하나 챙기기 귀찮으니 통신이나 call 방식에 신경 쓰지 않고 원격지의 자원을 내 것처럼 사용할 수 있다는 의미
* IDL(Interface Definication Language) 기반으로 다양한 언어를 가진 환경에서도 쉽게 확장이 가능하며, 인터페이스 협업에도 용이하다는 장점이 있다.
* RPC 의 핵심 개념은 Stub(스텁)이다.
    * 서버와 클라이언트는 서로 다른 주소 공간을 사용하므로, 함수 호출에 사용된 매개 변수를 꼭 변환해줘야 한다.
    * 변환하지 않는다면 메모리 매개 변수에 대한 포인터가 다른 데이터를 가리키게 되기 때문인데, 이 변환을 담당하는 게 스텁이다.
* Client Stub 은 함수 호출에 사용된 파라미터의 변환(Marshalling, 마샬링) 및 함수 실행 후 서버에서 전달된 결과의 변환을, Server Stub 은 클라이언트가 전달한 매개 변수의 역변환(Unmarshalling, 언마샬링) 및 함수 실행 결과 변환을 담당하게 된다.

### 기본적인 RPC 통신 과정
![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlFcne%2FbtsGB2v99QC%2FVkOQ0RzIwOmJL1CcLs7dOk%2Fimg.jpg)

1. IDL 을 사용하여 호출 규악을 정의 (함수명, 인자, 반환값에 대한 데이터형이 정의된 IDL 파일을 rpcgen 으로 컴파일하면 stub code 가 자동으로 생성됨)
2. Stub Code 에 명시된 함수는 원시코드의 형태로, 상세 기능은 Server 에서 구현된다.
3. Client 에서 Stub 에 정의된 함수를 사용할 때, Client Stub 은 RPC Runtime 을 통해 함수를 호출하고 Server 는 수신된 Procedure 호출에 대한 처리 후 결과 값을 반환한다.
4. 최종적으로 Client 는 Server 의 결과 값을 반환받고, 함수를 로컬에 있는 것처럼 사용할 수 있다.

## gRPC

* gRPC 는 google 사에서 개발한 오픈소스 RPC(Remote Procedure Call) 프레임워크이다.
* 이전까지는 RPC 기능은 지원하지 않고, 메세지(JSON 등)를 Serialize 할 수 있는 프레임워크인 PB(Protocol Buffer, 프로토콜 버퍼)만을 제공해 왔는데, google 에서 PB 기반 Serizlaizer 에 HTTP/2를 결합한 새로운 RPC 프레임워크를 탄생시켰다.

### 장점
* http/1.1은 기본적으로 클라이언트의 요청이 올 때만 서버가 응답을 하는 구조로 매 요청마다 connection을 생성해야만 한다.
* cookie 등 많은 메타 정보들을 저장하는 무거운 header 가 요청마다 중복 전달되어 비효율적이고 속도도 느려진다.
* http/2에서는 한 connection 으로 동시에 여러 개 메시지를 주고받으며, header 를 압축하여 중복 제거 후 전달하기에 1.x에 비해 효율적이다.
* 또한, 필요시 클라이언트 요청 없이도 서버가 리소스를 전달할 수도 있기 때문에 클라이언트 요청을 최소화할 수 있다.

### HTTP/2
* Streaming
* Header Compression
* Multiplexing
    * 1.x의 경우 플레인 텍스트에 헤더와 바디 등의 데이터를 한 번에 전송했지만, 2.0부터는 헤더와 데이터를 프레임이라는 단위로 분리하고 다른 스트림에 속하는 각각의 프레임들을 프레임 단위로 하나의 커넥션에 상호 배치하여 목적지에 전달한다.

### 호출과정
1. 클라이언트 Stub 은 RPC 함수를 호출한다.
2. 클라이언트 Stub 은 인코딩 메시지로 application/grpc 접두어가 붙은 Content-Type 을 가진 HTTP POST 요청을 생성한다. 이 때, 호출하는 원격함수는 별도의 HTTP 헤더로 전송된다. (ex. :method POST :path /TitleGroup/getTitleGroup)
3. HTTP 요청 메세지는 네트워크를 통해 서버 머신으로 전송
4. 서버에 메세지가 수신되면, 서버는 메세지 헤더를 검사해 어떤 서비스 함수를 호출해야하는지 확인하고 메세지를 서비스 Stub 에 넘긴다.
5. 서비스 Stub 은 메세지 바이트를 언어별 데이터 구조로 파싱한다.
6. 파싱된 메시지를 사용해 서비스는 RPC 함수를 로컬로 호출한다.
7. 서비스 함수의 응답이 인코딩되어 클라이언트로 다시 전송된다. (응답 메세지는 클라이언트에서와 동일한 절차를 따른다.)

### ProtoBuf (Protocol Buffer)
![image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FczOKQW%2FbtsGCuseeeR%2FWtRJTUBUdvjAnSk69KgDX0%2Fimg.png)

* ProtoBuf 는 google 사에서 개발한 구조화된 데이터를 직렬화(Serialization)하는 기법이다.
* 직렬화란, 데이터 표현을 바이트 단위로 변환하는 작업을 의미한다.
* 위 그림처럼 같은 정보를 저장해도 text 기반인 json 인 경우 82 byte 가 소요되는데 반해, 직렬화된 protocol buffer 는 필드 번호, 필드 유형 등을 1byte 로 받아서 식별하고, 주어진 length 만큼만 읽도록 하여 단 33 byte 만 필요하게된다.

### 타입
* Proto Type 의 경우, 기본적으로 null 값을 허용하지 않는다.
* null 값을 허용하기 위해서는 별도의 google 에서 제공하는 wrapper 타입을 사용해야 한다.

JavaType|ProtoType|default Value
---|---|---
int|int32|0
long|int64|0
float|float|0
double|double|0
boolean|bool|false
string|string|empty string
byte[]|bytes|empty bytes
collection / List|repeated|empty list
map|map|empty map

```protobuf
syntax = "proto3";

import "google/protobuf/wrappers.proto";
import "google/protobuf/timestamp.proto";
import "google/protobuf/empty.proto";

option java_multiple_files = true;
option java_package = "com.example.proto.titlegroup";

package com.example.titlegroup;

service TitleGroupHandler {
  rpc createTitleGroup(CreateTitleGroupRequest) returns (TitleGroupResponse);
  rpc getTitleGroup(TitleGroupIdRequest) returns (TitleGroupResponse);
  rpc getTitleGroups(google.protobuf.Empty) returns (TitleGroupListResponse);
}

message TitleGroupIdRequest {
  int32 id = 1;
}

message CreateTitleGroupRequest {
  string subject = 1;
  int32 createdBy = 2;
}

message TitleGroupResponse {
  int32 id = 1;
  string subject = 2;
  int32 createdBy = 5;
  google.protobuf.Int32Value modifiedBy = 6;
  google.protobuf.Timestamp createdDate = 7;
  google.protobuf.Timestamp modifiedDate = 8;
}

message TitleGroupListResponse {
  repeated TitleGroupResponse titleGroup = 1;
}
```
* syntax
    * 규약을 명시하는 부분으로 proto version 3의 규약을 따르겠다고 명시
    * proto2와 proto3는 지원하는 언어에 차이가 있으며 문법적으로도 차이가 있다.
    * proto2의 경우에는 optional, required 를 사용했지만 proto3에서는 deprecated 되었고 repeated 만 proto3에서 사용된다.
* java_multiple_files
    * 기본 옵션 값은 false
    * false 로 지정할 경우 오직 하나의 .java 파일이 생성되고, top-level 메시지, 서비스, enum에 대해 생성된 모든 자바 클래스(enum 등)는 outer 클래스 내에 중첩된다.
    * true 로 지정할 경우 위와 같은 상황에 대해 각각의 .java 파일이 생성된다.
* import "google/protobuf/.."
    * proto Type 에서는 기본적으로 null 값을 허용하지 않지만, google 에서 제공하는 wrapper 타입을 사용하면 null 을 사용할 수 있다.
    * 값을 명시하지 않으면 기본적으로 default value 가 지정되지만 값을 꺼내서 사용할 때, XXOrNull 혹은 hasXX 함수 등을 사용하여 값이 채워지지 않았는지 확인할 수 있는 방법이 제공된다.
    * Timestamp 타입으로 시간 관련한 필드를 정의할 수 있다.
* java_package
    * 기본적으로 package 경로로 생성되나 명시하여 생성되는 파일들의 package 경로를 지정할 수 있다.
* package
    * message type 간의 이름이 겹치는 경우, 구분할 때 사용한다.
* service
    * 서비스 인터페이스를 정의한다.
        * Client 는 Stub 을 사용하여 해당 인터페이스를 호출하고 Server 에서는 해당 인터페이스를 구현하게 된다.
        * Server 가 해당 인터페이스를 구현하게 되는데 spring mvc 관점에서 본다면 controller 와 유사하다.
* message
    * 요청과 응답 타입 메시지를 정의한다.
    * 메시지에 정의된 필드들은 각각 고유한 번호(Field Tag)를 갖게 되고 encoding 이후 binary data 에서 필드를 식별하는 데 사용된다.
    * 최소 1부터 536,870,911까지 지정 가능하며, 19000 ~ 19999는 프로토콜 버퍼 구현을 위해 reserved 된 값이므로 사용할 수 없다.
    * 필드 번호가 1 ~ 15일 때는 1byte, 16 ~ 2047은 2byte 를 Tag 로 가져가게 되기 때문에 자주 호출되는 필드에 대해서는 1 ~ 15로 지정하는 것이 권장된다.

## Interceptor
> https://techdozo.dev/grpc-interceptor-unary-interceptor-with-code-example/

![images](https://techdozo.dev/wp-content/uploads/2022/04/grpc-interceptor.drawio_a4.png.webp)

* 클라이언트가 gRPC 서비스의 원격 메서드를 호출할 때 서버에서 인터셉터를 사용해 원격 메서드 실행 전에 공통 로직을 실행할 수 있다.
* grpc interceptor 는 크게 serverInterceptor 와  clientInterceptor 로 구분되며 각 구분의 하위로 streaming 과 unary 로 다시 분류된다.

```java
@ThreadSafe
public interface ServerInterceptor {
  <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
          ServerCall<ReqT, RespT> call,
          Metadata headers,
          ServerCallHandler<ReqT, RespT> next);
}
```
* ServerInterceptor 인터페이스는 interceptCall 단일 메서드만 가지고 있다.
* 이 메서드는 클라이언트로부터의 각 호출에 대해 실행되며, 인터셉터 체인을 통해 다음 인터셉터 또는 실제 서비스 메서드로 요청을 전달한다.
* ServerCall 은 클라이언트로부터 받은 RPC(원격 프로시저 호출) 요청을 나타낸다. 이 객체를 통해 서버는 클라이언트에게 응답을 보낼 수 있다.

```java
@ThreadSafe
public interface ServerCallHandler<RequestT, ResponseT> {
  ServerCall.Listener<RequestT> startCall(
      ServerCall<RequestT, ResponseT> call,
      Metadata headers);
}
```
* ServerCallHandler 는 요청을 처리하는 로직을 캡슐화한다.
* 이 핸들러는 serverCall, Metadata 를 인자로 받아 요청에 대한 실제 비즈니스 로직을 수행한다.
* 요청을 처리하면서 ServerCall.Listener 객체를 생성하고 반환하는데 이 리스너는 클라이언트로부터 추가적인 메시지를 수신하거나, 요청 처리가 반쪽 받힘 상태, 요청 완료되었을 때 등의 다양한 이벤트를 처리하는 콜백 메서드를 제공한다.

```kotlin
class SimpleLoggingInterceptor : ServerInterceptor {
  override fun <ReqT : Any?, RespT : Any?> interceptCall(
    call: ServerCall<ReqT, RespT>,
    headers: Metadata,
    next: ServerCallHandler<ReqT, RespT>
  ): ServerCall.Listener<ReqT> {
    val serverCall = LoggingServerCall(
      delegate = call,
      startCallMillis = System.currentTimeMillis(),
    )
    return LoggingServerCallListener(next.startCall(serverCall, headers))
  }

  class LoggingServerCall<ReqT, RespT>(
    private val delegate: ServerCall<ReqT, RespT>,
    private val startCallMillis: Long,
  ) : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(delegate) {

    override fun close(status: Status, trailers: Metadata?) {
      logger.info {
        "status:${status.code.name} " +
                "rpc:${delegate.methodDescriptor.fullMethodName.replace("/", ".")} " +
                "responseTime:${(System.currentTimeMillis() - startCallMillis)}ms "
      }
      super.close(status, trailers)
    }
  }

  class LoggingServerCallListener<ReqT>(
    delegate: ServerCall.Listener<ReqT>,
  ) : ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {

    override fun onMessage(message: ReqT) {
      logger.info("Receive Message : ${message.toString().trim()}")
      super.onMessage(message)
    }
  }

  companion object {
    val logger = KotlinLogging.logger { }
  }ㄱㄷ
}
```
* 인터셉터에서 전처리는 serverCallHandler 의 startCall 메서드를 호출하기 전에 수행할 수 있다.
* 후처리는 serverCall 을 재정의하거나, listener 재정의르 통해 할 수 있다.
* SimpleForwardingServerCall 과 SimpleForwardingServerCallListener 는 각각 ServerCall 과 ServerCall.Listener 의 편리한 래퍼 클래스로, 이 래퍼들은 gRPC 서버에서 요청을 다루는 데 필요한 메서드를 상속받아, 개발자가 특정 메소드를 오버라이드하는 것을 간소화한다.
* 위 코드는 이를 활용한 간단한 로깅 코드이다.

### SimpleForwardingServerCall 과 SimpleForwardingServerCallListener 를 재정의하여 다양한 지점에서 로직을 구현

![images](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcPEMya%2FbtsGAZttzph%2Fh0yMHRu02RS2MysjEdPUpk%2Fimg.png)

## mTLS
* mTLS 는 TLS 의 확장된 형태로, TLS 는 서버의 신원만을 인증하는 반면 mTLS 는 서버뿐만 아니라 클라이언트도 자신의 신원을 인증서를 통해 증명해야 하는 상호 인증 방식이다.
* 이는 양방향 인증이라고도 하며, 서로의 인증서를 검증함으로써 보다 강화된 보안을 제공한다.

### TLS 통신과정
![images](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbxXC3v%2FbtsG8R1eVoB%2FVPnO1UcErtxd2QaoP2DVc0%2Fimg.png)

1. 클라이언트가 서버에 연결
2. 서버가 TLS 인증서를 제시
3. 클라이언트가 서버의 인증서를 확인
4. 클라이언트와 서버가 암호화된 TLS 연결을 통해 정보를 교환

### mTLS 통신과정
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

### Self Signed Certificate(SSC)
* 일반적으로 인증서는 개인키 소유자의 공개키를 인증기관(CA)에 전달하면 인증기관에서는 전달받은 공개키와 기타 정보를 사용하여 인증기관의 개인키로 암호화하여 인증서를 만들어준다.
* 즉, 인증서는 개인키 소유자의 공개키(public key)에 인증기관의 개인키로 서명한 데이터이다.
* 따라서 모든 인증서는 발급기관(CA)이 있어야 한다.
* 하지만 최상위에 있는 인증기관(root ca)은 서명해 줄 상위 인증기관이 없으므로 root ca 의 개인키로 스스로의 인증서에 서명하여 최상위 인증기관 인증서를 만들게 된다.
* 이렇게 스스로 서명한 root ca 인증서를 Self Signed Certificate(SSC)라고 한다.

#### 참고
* IE, FireFox, Chrome 등의 Web Browser 제작사는 VeriSign 이나 comodo 같은 유명 ROOT CA 들의 인증서를 신뢰하는 CA 로 브라우저에 미리 탑재해 놓는다.
* 위와 같은 기관에서 발급된 SSL 인증서를 사용해야 browser 에서는 해당 SSL 인증서를 신뢰할 수 있는데 OpenSSL 로 만든 ROOT CA 와 SSL 인증서는 Browser 가 모르는 기관이 발급한 인증서이므로 보안 경고를 발생시키지만 내부 서버 간의 통신 용도 혹은 테스트 사용 시에는 지장이 없다.

### Certificate Signing Request(CSR)
* CSR(Certificate Signing Request) 은 인증기관에 인증서 발급 요청을 하는 특별한 ASN.1 형식의 파일이며(PKCS#10 - RFC2986) 그 안에는 내 공개키 정보와 사용하는 알고리즘 정보등이 들어 있다.
* 개인키는 외부에 유출되면 안 되므로 특별한 형식의 파일을 만들어서 인증기관에 전달하여 인증서를 발급반든다.
* 쉽게 말하자면, CSR 은 인증기관에 인증서 발급을 요청하는 인증서 발급 신청서라고 볼 수 있다.

### 인증서 생성 순서
1. 자체 서명 루트 인증서 생성 (ca.crt)
2. 서버 인증서 키 생성 (server.key)
3. 서버 인증서 CSR 생성 (server.csr)
4. 서버 인증서 생성 및 CA Root 키로 서명
5. 클라이언트 인증서 키 생성 (client.key)
6. 클라이언트 인증서 CSR 생성 (client.csr)
7. 클라이언트 인증서 생성 및 CA Root 키로 서명

### 인증서 생성 config 파일

#### ssl.conf
```shell
# req 섹션은 openSSL 의 예약되어 있는 섹션명으로 config 파일로 명시할 경우, CSR 을 만드는 명령어일때 참조된다.
# 예약된 명령어 참조 : https://www.openssl.org/docs/man3.0/man1/openssl.html
[ req ]
# 생성될 키의 크기를 비트 단위로 지정
default_bits       = 2048
# CSR 에 포함될 주체(Subject)의 정보를 입력받는 섹션의 이름을 지정
distinguished_name = req_distinguished_name
# CSR 생성 시 적용할 추가 확장 설정을 포함하는 섹션의 이름을 지정
req_extensions     = req_ext

# 구체적인 주체 정보 필드(예: 국가명, 조직명, 공통 이름 등)를 정의
[ req_distinguished_name ]
countryName                 = Country Name (2 letter code)
countryName_default         = KR
stateOrProvinceName         = State or Province Name (full name)
stateOrProvinceName_default = Gyeonggi-do
localityName                = Locality Name (eg, city)
localityName_default        = Seongnam-si
organizationName            = Organization Name (eg, company)
organizationName_default    = yeongjoon
commonName                  = Common Name (e.g. server FQDN or YOUR name)
commonName_max              = 64
commonName_default          = localhost

[ req_ext ]
subjectAltName = @alt_names

[ alt_names ]
DNS.1 = localhost
IP.1 = 127.0.0.1

# 아래는 CA, 서버, client 인증서를 만들때 사용할 커스텀 섹션
# 설정 참조 
#   https://www.openssl.org/docs/man3.0/man5/x509v3_config.html
#   https://superuser.com/questions/738612/openssl-ca-keyusage-extension
# 사용자 커스텀 섹션으로 적용하기 위해서는 명령어에 -extension 으로 명시해야 한다.

# 인증서가 CA(인증기관)로서 작동해야할 때 사용되는 extension
[ v3_ca ]
basicConstraints = critical,CA:TRUE
keyUsage = digitalSignature,keyCertSign,cRLSign
subjectKeyIdentifier = hash
authorityKeyIdentifier = keyid:always,issuer:always
subjectAltName = @alt_names

# 사용자 커스텀 섹션으로 적용하기 위해서는 명령어에 -extension 으로 명시해야 한다.
# 인증서가 client, server 인증서로 사용되며 하위 인증서를 발행할 권한로서 작동해야할 때 사용되는 extension
[ v3_req ]
basicConstraints = CA:FALSE
keyUsage = nonRepudiation,digitalSignature,keyEncipherment
extendedKeyUsage = serverAuth,clientAuth
subjectKeyIdentifier = hash
authorityKeyIdentifier  = keyid:always,issuer:always
subjectAltName = @alt_names
```

* basicConstraints : X.509 인증서에 대한 확장으로 인증서가 CA 로 사용될 수 있을지 여부를 결정한다.
* keyUsage : X.509의 확장 필드로 인증서에 포함된 공개 키가 어떤 목적으로 사용될 수 있을지 명시한다.
    * digitalSignature : 공개 키를 사용하여 디지털 서명을 생성하고 검증할 수 있음을 나타낸다. 이는 데이터의 무결성과 발신자의 인증을 보장하는 데 사용된다.
    * keyCertSign : 인증서가 다른 인증서에 대한 서명을 할 수 있음을 나타낸다.
    * cRLSign : 인증서 폐지 목록(Certificate Revocation List, CRL)에 서명할 수 있음을 나타낸다.
    * nonRepudiation : 데이터의 송신자가 나중에 데이터를 보냈다는 사실을 부인하는 것을 방지하는 데 사용된다.
    * keyEncipherment : 공개 키를 사용하여 세션 키와 같은 다른 키들을 암호화하는 데 사용된다.
* extendedKeyUsage : 증서에 포함된 공개 키가 사용될 수 있는 특정 목적을 제한하고 추가적으로 상세화하는 데 사용한다.
    * serverAuth : 서버 인증서에 사용
    * clientAuth: 클라이언트 인증서에 사용
* subjectKeyIdentifier : 인증서에 포함된 공개키 식별자 제공 방식
* authorityKeyIdentifier : 인증서가 발행된 인증 기관(CA)의 식별 정보를 포함한다. 이 정보는 인증서 체인을 검증하는 과정에서 해당 인증서가 어떤 CA 에 의해 서명되었는지를 식별하는 데 사용된다.
* subjectAltName (Subject Alternative Name)
    * 지정된 도메인 이름들은 해당 SSL/TLS 인증서를 사용하는 서버가 클라이언트로부터 받는 요청의 유효성을 검증하는 데 사용된다. 클라이언트는 SSL/TLS 핸드셰이크 과정에서 서버로부터 인증서를 받고, 인증서에 포함된 도메인 이름이 요청한 도메인과 일치하는지 검사한다.
    * 과거에는 CN(common name)이 주로 사용되었지만 CN 에는 오직 하나의 도메인 이름만 명시할 수 있는 한계가 있어 현재는 CN 보다 subjectAltName 을 우선적으로 사용한다.

### CA 인증서 생성
```shell
# CA 키 & 인증서 생성
openssl req -x509 -nodes -days 36500 -newkey rsa:2048 -keyout ca.key -out ca.crt -extensions v3_ca -config ssl.conf

# 인증서 점검
openssl x509 -in ca.crt -text -noout
```
* -x509 : 인증요청서(csr)를 대신 자체 서명된 인증서 생성
* -nodes : 개인 키를 암호 없이 저장
* -days : 인증서 유효기간
* -newkey rsa:2048 : 2048 비트 rsa 키 생성
* -keyout : 생성된 개인키 파일명 지정
* -out : 생성된 자체 서명된 인증서 파일명 지정
* -extensions : 확장 기능 설정으로 config 파일에 명시한 v3_ca를 적용

### Server 인증서 생성
```shell
# 서버키 생성
openssl genrsa -out _server.key 2048

# 형식 변환
# genrsa를 사용하여 키를 생성하면 PKCS#1형식으로 생성됩니다. 
# 자바에서는 PKCS#8 형식을 지원하기 때문에 변환해줘야 합니다.
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in _server.key -out server.key

# 키 제거
rm _server.key

# 서버 csr 생성
openssl req -new -key server.key -out server.csr -config ssl.conf

# CA로 서명된 서버 인증서 생성
openssl x509 -req -days 36500 -in server.csr -CA ca.crt -CAkey ca.key -out server.crt -CAcreateserial -extensions v3_req -extfile ssl.conf

# 인증서 점검
openssl x509 -in server.crt -text -noout
```

### Client 인증서 생성
```Shell
# 클라이언트키 생성
openssl genrsa -out _client.key 2048

# 형식 변환
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in _client.key -out client.key

# 키 제거
rm _client.key

# 클라이언트 csr 생성
openssl req -new -key client.key -out client.csr -config ssl.conf

# CA로 서명된 클라이언트 인증서 생성
openssl x509 -req -days 36500 -in client.csr -CA ca.crt -CAkey ca.key -out client.crt -CAcreateserial -extensions v3_req -extfile ssl.conf 

# 인증서 점검
openssl x509 -in client.crt -text -noout
```

> https://backtony.tistory.com/84  
> https://backtony.tistory.com/85