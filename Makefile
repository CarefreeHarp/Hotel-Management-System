.PHONY: run

JAVA_HOME ?= /usr/lib/jvm/java-17-openjdk-amd64

run:
	JAVA_HOME="$(JAVA_HOME)" PATH="$(JAVA_HOME)/bin:$$PATH" ./demo/mvnw -f demo/pom.xml spring-boot:run
