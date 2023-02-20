# Rolldebee SQL Generator

## Getting Started

Build and run with Docker:

```shell
docker build -t rolldebee/sqlgen .
docker run --rm -it --name rolldebee-sqlgen -p 9898:9898 rolldebee/sqlgen
```

Generate for MySQL:

```shell
curl -X GET "http://localhost:9898/scripts/compose?db=mysql"
```

Generate for Oracle:

```shell
curl -X GET "http://localhost:9898/compose?db=red"
```
