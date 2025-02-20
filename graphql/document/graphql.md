# graphql-playground

### React / TypeScript
> https://github.com/hwasurr/graphql-book-fullstack-project   
> GraphQL과 타입스크립트로 개발하는 웹 서비스, 강화수 지음

<hr />

### Spring-GraphQL
> https://techdozo.dev/category/microservices/graphql/  
> https://it-eldorado.tistory.com/179  
> https://tech.kakao.com/2019/08/01/graphql-basic/

#### What is GraphQL
1. Query language: GraphQL is a query language for API. In GraphQL, you design API based on its type system. A GraphQL API is expressed as a statically typed schema.
2. Server runtime: On the service side, a GraphQL service provides a runtime layer that describes the structure of data exposed by API, and this runtime layer is responsible for parsing GraphQL requests and calling the appropriate data fetcher (also called resolver) for each field.

* 동작
    * 서버에서는 클라이언트가 GraphQL 방식으로 요청할 수 있는 데이터의 타입들과 각 타입에 대해 요청할 수 있는 필드들을 정의하여 **타입 시스템을 구축**하고, 각 타입의 각 필드에 대한 요청을 해석 및 처리하는 로직을 **Resolver 함수들로 구현**합니다.
    * 이후 클라이언트가 GraphQL 쿼리를 보내면, 서버는 미리 정의해둔 타입 시스템에 따라 해당 쿼리를 검증(Validation)하고, 문제가 없다면 미리 구현해둔 Resolver 함수들을 호출하여 **해당 쿼리를 실행(Execution)한** 결과를 클라이언트에게 응답합니다.
    * 서버사이드 gql 어플리케이션은 gql로 작성된 쿼리를 입력으로 받아 쿼리를 처리한 결과를 다시 클라이언트로 돌려줍니다.
    * HTTP API 자체가 특정 데이터베이스나 플렛폼에 종속적이지 않은것 처럼 마찬가지로 gql 역시 어떠한 특정 데이터베이스나 플렛폼에 종속적이지 않습니다.
      ![image](https://github.com/hongyeongjune/graphql-playground/assets/39120763/c0290af7-3b1a-455d-99e3-e75fbeea2e4b)

#### Features of GraphQL
1. Strongly typed
    * GraphQL APIs are strongly typed (expressed as schema).
2. Hierarchal data
    * As the Graph in GraphQL suggests, GraphQL provides first-class support for hierarchal data.
    * In GraphQL, you can create a request as a graph of related fields.
    * A GraphQL response is shaped like the request which is a very natural way for a client to describe its data requirement.
3. Client-specific response
    * REST APIs are often criticized for over-fetching.
    * Generally, a REST API returns all data under a resource even though you need only a fraction of the data.
    * This is not a problem in GraphQL as a GraphQL client can choose to request data on the field level granularity.

#### Why GraphQL?
1. API Documentation
    * In REST API, we maintain API documentation in OpenAPI spec.
    * One of the common problems with REST API is that API documentation and implementation drift in due course.
    * Unlike REST API, in GraphQL, you don’t need to maintain APIs documentation separately.
2. Over fetching
    * Over fetching is a big problem in REST API, particularly for mobile applications.
    * Firstly, pure REST APIs are built around resources.
    * Secondly, it doesn’t have any concept of partial response.
    * For example – a mobile e-commerce app, that displays order history, needs to show the name of the product, purchase date, and price but REST API /orders API may return many more fields including payment details, discounts, shipment details, etc, making it inefficient for mobile use.
3. Under fetching
    * As REST APIs are built around resources and are usually fine-grained, a client application may have to call multiple APIs to construct a view.
    * This is typically not a problem in GraphQL because of its hierarchal nature.

#### GraphQL operation
A GraphQL service can support the following operations

1. **Queries**: queries represent READ operations.
2. **Mutation**: mutation involves WRITE then READ operation.
3. **Subscription**: subscription is used for continuous READ (for example, over WebSocket).

#### Server transport
GraphQL specification itself doesn’t talk anything about transport layer protocol. GraphQL over HTTP specification extends GraphQL specification to **cover the topic of serving GraphQL services over HTTP**.

As per GraphQL over HTTP specification, requests must use **HTTP POST with request details included as JSON in the request body**. Once the JSON body has been successfully decoded, the HTTP response status is always **200 (OK)**, and any errors from GraphQL request execution appear in the “errors” section of the GraphQL response.
> GraphQL over HTTP 사양에 따르면, 요청들은 요청 본문에 JSON 으로 포함된 요청 세부 정보가 포함된 HTTP POST 를 사용해야 합니다. JSON 본문이 성공적으로 Decoding 되면 HTTP 응답 상태는 항상 200(OK)이며 GraphQL 요청 실행에서 발생하는 모든 오류는 GraphQL 응답의 "errors" 섹션에 나타납니다.

The default and preferred choice of the media type is "application/graphql+json" , but "application/json" is also supported.

#### RuntimeWiring
GraphQL Java RuntimeWiring.Builder is used to register DataFetchers, type resolvers, custom scalar types, and more. You can declare RuntimeWiringConfigurer beans in your Spring config to get access to the RuntimeWiring.Builder.

#### HttpStatus
The way to return errors in GraphQL (at least in graphql-js) is to throw errors inside the resolve functions.
Because HTTP status codes are specific to the HTTP transport and GraphQL doesn't care about the transport, there's no way for you to set the status code there.
> https://stackoverflow.com/questions/42937502/graphql-how-to-respond-with-different-status-code

As you all know, REST APIs use status codes as a part of the response (200-ok, 404-not found, 500-internal server error etc.).
In GraphQL it’s a bit different because in most cases you just get 200 (or 500 if something goes really bad).
> https://medium.com/swlh/status-codes-in-graphql-4cbf699bc2be

A GraphQL API will always return a 200 OK Status Code, even in case of error. You'll get a 5xx error in case the server is not available at all.
> https://the-guild.dev/blog/graphql-error-handling-with-fp

<hr />

