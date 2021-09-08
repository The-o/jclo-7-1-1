.PHONY: build-dev
build-dev:
	docker build \
		-t jclo-7-6-1:dev \
		--build-arg PROFILE=dev \
		.

.PHONY: run-dev
run-dev:
	docker run \
		-it \
		--rm \
		-p 80:8080 \
		--name jclo-7-6-1-dev \
		jclo-7-6-1:dev

.PHONY: build-prod
build-prod:
	docker build \
		-t jclo-7-6-1:prod \
		--build-arg PROFILE=prod \
		.

.PHONY: run-prod
run-prod:
	docker run \
		-it \
		--rm \
		-p 81:8081 \
		--name jclo-7-6-1-prod \
		jclo-7-6-1:prod
