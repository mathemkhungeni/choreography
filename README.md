# Saga Choreography Example

A Spring Boot saga choreography demo with two services:

- **order-service** (port `8081`) — creates orders and listens for payment events
- **payment-service** (port `8082`) — processes payments via Kafka

Infrastructure is provided by Docker Compose: MySQL, Zookeeper, Kafka, and both application services.

## Prerequisites

- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/) (included with Docker Desktop)

## Clone the repository

```bash
git clone https://github.com/mathemkhungeni/choreography.git
cd choreography
```

## Run with Docker Compose

Build the service images and start all containers:

```bash
docker compose up --build
```

Run in the background:

```bash
docker compose up --build -d
```

The first startup may take a few minutes while Maven builds the images and MySQL/Kafka become healthy.

### Services

| Service          | URL / Port              |
|------------------|-------------------------|
| Order service    | http://localhost:8081   |
| Payment service  | http://localhost:8082   |
| MySQL            | localhost:3306          |
| Kafka            | localhost:9092          |

### Stop the stack

```bash
docker compose down
```

Remove volumes as well (clears MySQL data):

```bash
docker compose down -v
```

## API examples

## request
```bash
curl --location --request POST 'http://localhost:8081/order/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId": 103,
    "productId": 33,
    "amount": 4000
}'
```
## Kafka payload

### Happy scenario
```bash
{"eventId":"b0e47448-eeb8-4cf4-bd29-b3a4315fc592","date":"2021-09-03T17:26:46.777+00:00","orderRequestDto":{"userId":103,"productId":33,"amount":4000,"orderId":1},"orderStatus":"ORDER_CREATED"}
```

```bash
{"eventId":"c48c5593-9f81-4ab4-9de8-b9fca2d2bef2","date":"2021-09-03T17:26:51.989+00:00","paymentRequestDto":{"orderId":1,"userId":103,"amount":4000},"paymentStatus":"PAYMENT_COMPLETED"}
```

## Request
```bash
curl --location --request POST 'http://localhost:8081/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userId": 103,
    "productId": 12,
    "amount": 800
}'
```

### insufficent amount
```bash
{"eventId":"fecacc77-017d-49cd-bdfa-58e47170da49","date":"2021-09-03T17:28:23.126+00:00","orderRequestDto":{"userId":103,"productId":12,"amount":800,"orderId":2},"orderStatus":"ORDER_CANCELLED"}
```

```bash
{"eventId":"46378bbc-5d15-4436-bed1-c6f3ddb1dc31","date":"2021-09-03T17:28:15.940+00:00","paymentRequestDto":{"orderId":2,"userId":103,"amount":800},"paymentStatus":"PAYMENT_FAILED"}
```

```bash
curl --location --request GET 'http://localhost:8081/orders' \
--header 'Content-Type: application/json' \
--data-raw ''
```

## Response

```bash
[
    {
        "id": 1,
        "userId": 103,
        "productId": 33,
        "price": 4000,
        "orderStatus": "ORDER_COMPLETED",
        "paymentStatus": "PAYMENT_COMPLETED"
    },
    {
        "id": 2,
        "userId": 103,
        "productId": 12,
        "price": 800,
        "orderStatus": "ORDER_CANCELLED",
        "paymentStatus": "PAYMENT_FAILED"
    }
]
```

## user Balance
![Screen Shot 1400-06-12 at 23 01 03](https://user-images.githubusercontent.com/25712816/132045620-183ecf3e-80e4-447d-b07c-a875d027bf01.png)

## Order repo
![Screen Shot 1400-06-12 at 23 01 19](https://user-images.githubusercontent.com/25712816/132045727-092ba09b-e6ea-42ac-9dba-9aa6aebb050a.png)
