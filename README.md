# API CONNECTOR

## Buildumgebung

Erforderliche Tools:

- Java 8
- Maven
- Docker
- Docker-Compose
- Chrome
- Node 16.13
- Angular CLI ab Version 15

## Test-Infrastruktur

Starten einer PostgreSQL:

    docker-compose -f stack.yml up

## Backend
Bauen mit 

    mvn install

Starten mit

    spring-boot:run


### Tests
Unit und Integrationstest kann man einzeln mit

    mvn test

starten. Da hierbei Integrationstests mit einer PostgreSQL 
laufen muss der docker-compose-stack für die Integrationstests vorher hochgefahren sein.

### Hinweis zu Spring Profilen:
* `test-data` - führt zusätzlich zu den "normalen" Flyway-Migrationen
  auch die aus den `test-migrations` Verzeichnissen aus und spielt so
  einen Bestand an Testdaten ein.


### Konfigurationsparameter (application.conf)

Diese sind in der [application.yml](src/main/resources/application.yml) zu finden:

* `db_host`: Hostname der Postgres Datenbank
* `db_name`: Name der Postgres Datenbank
* `db_schema`: Schema der Postgres Datenbank
* `db_username`: Username der Postgres Datenbank
* `db_password`: Passwort der Postgres Datenbank
* `cors.origin_url`:  Erlaubte origins

## Frontend
Man wechsle in den Ordner [api-connector-spa](api-connector-spa) und führe dort folgende Schritte aus:

Installieren der NPM Abhängigkeiten:

    npm i

Starten eines Testservers zur Auslieferung der SPA mit:

    ng serve

Dann kann unter [http://localhost:4200](http://localhost:4200) die Anwendung im Browser ausgeführt werden.


### UI Tests
Man kann die Unit-Tests mit folgenden Befehl starten:

    npm run test

End-2-End Tests mit laufenden Backend kann man über diesen Befehl starten:

    npm run e2e

Zum Tests-Schreiben und Debuggen dieser empfiehlt sich allerdings

    npx cypress open

Hierbei wird der installierte Chrome-Browser gestartet und die Tests
können über eine UI gestartet und debugged werden.

## Deployment

Zum Deployen der Applikation in eine container-basierte Cloud Umgebung (Kubernetes) 
müssen die folgenden Schritte unternommen werden:

Für das Backend liegt ein [Dockerfile](Dockerfile) auf Root-Ebene des Projektes,
mit diesem Dockerfile kann die Spring-Boot Backend-Applikation als Docker-Container gebaut und deployed werden.

Ebenso steht im Ordner [api-connector-spa](/api-connector-spa/Dockerfile) 
für das Frontend ein [Dockerfile](/api-connector-spa/Dockerfile) bereit. 
So kann auch das Frontend containerisiert ausgeliefert werden. 

Nach dem erstellen der Docker-Images müssen diese Images in eine passende Docker-Registry gepushed werden.

Liegen diese Images in einer Docker-Registry vor können diese z.b. in einer 
Kubernetes Deploy-Ressource referenziert und damit schlussendlich in einen Kubernetes Cluster deployed werden.

Ein beispielhaftes Kubernetes Deployment findet sich im deployment Ordner.



