# User API Spec

## Register User

Endpoint : **POST** `/api/users`

Request Body :

```json
{
  "username": "tester1",
  "password": "password",
  "name": "user tester 1"
}
```

Response Body (success) :

```json
{
  "data": {
    "username": "tester1",
    "name": "user tester 1"
  }
}
```

Response Body (failed) :

```json
{
  "data": "Failed",
  "errors": "Username is required"
}
```

## Login User

Endpoint : **POST** `/api/auth/login`

Request Body :

```json
{
  "username": "tester1",
  "password": "password"
}
```

Response Body (success) :

```json
{
  "data": {
    "token": "exampleToken",
    "expiredAt": 1691674511431
  }
}
```

Response Body (failed, 401) :

```json
{
  "errors": "Username or password is wrong"
}
```

## Get User
Endpoint : **GET** `/api/users/current`

Request Header :

- X-API-TOKEN : `Token`

Response Body (success) :

```json
{
  "data": {
    "username": "tester1",
    "name": "user tester 1"
  }
}
```

Response Body (failed, 401) :

```json
{
  "errors": "Unauthorized"
}
```

## Update User

Endpoint : **PATCH** `/api/users/current`

Request Header :

- X-API-TOKEN : `Token`

Request Body :

```json
{
  "name": "user tester 1",
  "password": "password"
}
```

Response Body (success) :

```json
{
  "data": {
    "username": "tester1",
    "name": "user tester 1"
  }
}
```

Response Body (failed, 401) :

```json
{
  "errors": "Username or password is wrong"
}
```

## Logout User

Endpoint : **DELETE** `/api/auth/logout`

Request Header :

- X-API-TOKEN : `Token`
  Response Body (success) :

```json
{
  "data": "OK"
}
```