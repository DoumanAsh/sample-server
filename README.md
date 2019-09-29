# sample-server

This repository contains the skeleton for a working Spring project. It should be modified to satisfy the additional features and objectives below. The skeleton includes a few things to get you started:

* A working spring-boot application.

* A basic database schema for managing users.

* Basic user creation.

	`POST /users`

* Basic user authentication.

	`POST /users/login`

	An authentication token will be returned that can be used on subsequest, authenticated requests. This token should be submitted in the header for authenticated requests: `X-Auth-Token`.

* `GET /rates`, which occasionally re-fetched currency rates from Fixer API and caches it for 90 minutes

## Pre-requisites

* JDK 1.8+
* gradle 5.0+

## Impl notes

- For simplicity sake uses H2 for in-memory DB

- To provide Fixer API key, store the key in file `src/main/resources/secret.fixer` (only key alone)

- Uses Spring's `RestTemplate` which means it relies on thread pool for concurrency. Ideally we'd want to go full async, but this should be more than enough since we cache result.
And I'm completely unfamiliar with Spring stack, so switching to async was overkill.
