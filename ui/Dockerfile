FROM oven/bun:1-alpine

WORKDIR /app

COPY . .

RUN bun i --frozen-lockfile

ENTRYPOINT ["bun", "run", "dev"]

EXPOSE 5173