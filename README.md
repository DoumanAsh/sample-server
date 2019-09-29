# sample-server

This repository contains the skeleton for a working Spring project. It should be modified to satisfy the additional features and objectives below. The skeleton includes a few things to get you started:

* A working spring-boot application.

* A basic database schema for managing users.

* Basic user creation.

	`POST /users`

* Basic user authentication.

	`POST /users/login`

	An authentication token will be returned that can be used on subsequest, authenticated requests. This token should be submitted in the header for authenticated requests: `X-Auth-Token`.

* `GET /rates`

## Pre-requisites

* JDK 1.8+
* gradle 5.0+

## Impl notes

- For simplicity sake uses H2 for in-memory DB
