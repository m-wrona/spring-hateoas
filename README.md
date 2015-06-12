# REST - HATEOAS [![Circle CI](https://circleci.com/gh/m-wrona/spring-hateoas.svg?style=svg)](https://circleci.com/gh/m-wrona/spring-hateoas)

Sample REST application using HATEOAS approach.

Application depicts:
- REST API
- hypermedia support
- service versioning using VND
- XML/JSON content representation (any representation can be attached easily)
- server-side approach to testing of service API:

    Test are written using mock MVC thus they are pretty slow.
    Normally such tests should be placed in separate package with integration/functional tests.
    However for demo purposes they have been placed here with code of the services
    instead of unit tests which would enable to achieve similar results with minor effort (with the usage of mocks).

## Usage

1) Build package and run local server

```
run.sh
```

2) Deploys war package to Tomcat server (CATALINA_HOME variable must be set)

```
deploy-war.sh
```

3) Sends sample requests to local server using different versions of services.

```
demo.sh
```

***Note: local server (run.sh) must be up and running before running this script.***

## Samples

1) Create message:

```
curl -i -H 'Accept: application/vnd.messages-v1+json' -d "title='message 1'" http://localhost:8080/message

HTTP/1.1 201 Created
Server: Apache-Coyote/1.1
Content-Type: application/vnd.messages-v1+json
Transfer-Encoding: chunked

{
  "entityId":"258c916d-143f-48cb-97ef-f1861e231e70",
  "links":[
    {"rel":"self","href":"http://localhost:8080/message/258c916d-143f-48cb-97ef-f1861e231e70"}
  ]
}
```

2) Get chosen page of messages (supported in both V1 and V2 of services)

```
curl -i -G -H 'Accept: application/vnd.messages-v1+json' -H 'Content-type: application/json'  http://localhost:8080/messages/2

HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/vnd.messages-v1+json
Transfer-Encoding: chunked

{
 "resources":[
  {"entityId":"57f7aad4-0432-4f70-851b-74e41a5915e8","title":"'message 1'","links":[{"rel":"self","href":"http://localhost:8080/message/57f7aad4-0432-4f70-851b-74e41a5915e8"}]},
  {"entityId":"ea8eb940-a015-416f-b2db-484e68e830e3","title":"'message 2'","links":[{"rel":"self","href":"http://localhost:8080/message/ea8eb940-a015-416f-b2db-484e68e830e3"}]},
  {"entityId":"258c916d-143f-48cb-97ef-f1861e231e70","title":"'message 1'","links":[{"rel":"self","href":"http://localhost:8080/message/258c916d-143f-48cb-97ef-f1861e231e70"}]}
 ],
 "links":[
    {"rel":"self","href":"http://localhost:8080/messages/2?includeFields=content"},
    {"rel":"next","href":"http://localhost:8080/messages/3?includeFields=content"},
    {"rel":"prev","href":"http://localhost:8080/messages/1?includeFields=content"}
 ]
}
```

3) Get chosen page of messages with chosen fields (supported only in V2 of service)

```
curl -i -G -H 'Accept: application/vnd.messages-v2+json' -H 'Content-type: application/json' -d "includeFields=content"  http://localhost:8080/messages/2

HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/vnd.messages-v2+json
Transfer-Encoding: chunked

{
  "resources":[
    {"entityId":"e86c23c0-3eeb-4485-a555-4d283f0acf80","content":"'content 1'","links":[{"rel":"self","href":"http://localhost:8080/message/e86c23c0-3eeb-4485-a555-4d283f0acf80"}]},
    {"entityId":"4426ef9a-6a09-4245-8f16-50d4053c3e8b","links":[{"rel":"self","href":"http://localhost:8080/message/4426ef9a-6a09-4245-8f16-50d4053c3e8b"}]},
    {"entityId":"3d705250-c22d-4396-8620-0ae03a8a7bae","content":"'content 3'","links":[{"rel":"self","href":"http://localhost:8080/message/3d705250-c22d-4396-8620-0ae03a8a7bae"}]}
  ],
  "links":[
    {"rel":"self","href":"http://localhost:8080/messages/2?includeFields=content"},
    {"rel":"next","href":"http://localhost:8080/messages/3?includeFields=content"},
    {"rel":"prev","href":"http://localhost:8080/messages/1?includeFields=content"}
  ]
}
```
