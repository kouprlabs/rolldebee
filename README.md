<!-- markdownlint-disable MD033 MD041 -->
<p align="center">
  <img height="70" src="assets/brand.svg"/>
  <h1 align="center">Rolldebee</h1>
</p>

![](./assets/screencast.gif)

## Getting Started

Install [Docker](https://docs.docker.com/get-docker) and [Docker Compose](https://docs.docker.com/compose/install).

### Run for Development

Start infrastructure services:

```sh
docker compose up
```

Additional instructions:

- [Rolldebee API](api/README.md)
- [Rolldebee UI](ui/README.md)
- [Rolldebee SQL Generator](sqlgen/README.md)

### Run for Production

Build Docker images:

```sh
docker-compose -f ./docker-compose.prod.yml build
```

Then:

```sh
docker-compose -f ./docker-compose.prod.yml up
```

## Licensing

Rolldebee is released under the [The MIT License](./LICENSE).
