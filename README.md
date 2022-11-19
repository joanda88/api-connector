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

    mvn spring-boot:run


### Tests
Unittest kann man einzeln mit

    mvn test

starten. 


Die Integrationstests starten über:

    mvn verify 

Da hierfpr eine PostgreSQL verwendet wird, 
sollte der docker-compose-stack für die Integrationstests vorher hochgefahren sein.

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
Man wechsle in den Ordner [api-connector-spa](api-connector-spa) 
und führe dort folgende Schritte aus:

Installieren der NPM Abhängigkeiten:

    npm i

Starten eines Testservers zur Auslieferung der SPA mit:

    ng serve

Dann kann unter [http://localhost:4200](http://localhost:4200) 
die Anwendung im Browser ausgeführt werden.


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

Nach dem Erstellen der Docker-Images müssen diese Images in eine passende Docker-Registry gepushed werden.

Liegen diese Images in einer Docker-Registry vor können diese z.b. in einer 
Kubernetes Deployment-Ressource referenziert und damit schlussendlich in einen Kubernetes Cluster deployed werden.

Ein beispielhaftes Kubernetes Deployment findet sich im [deployment](deployment) Ordner.

Dort finden sich eine Reihe von Kubernetes Ressourcen:

- [configMap.yaml](deployment/configMap.yaml): Hier kann zur CORS Konfiguration der Host des Backends angegebn werden
- [dbSecret.yaml](deployment/dbSecret.yaml): Hier wird in einem Secret der Zugang zur DB definiert
- [deploymentBackend.yaml](deployment/deploymentBackend.yaml), [deploymentFrontend.yaml](): Deployment des Backends und Frontends. Hier wird auch auf die configmap und das DB-Secret zurückgegriffen
- [serviceBackend.yaml](deployment/serviceBackend.yaml), [serviceFrontend.yaml](deployment/serviceFrontend.yaml): Diese dienen dazudie deployten Pods für Frontend und Backend dem jeweiligen LoadBalancer(ingress) zur Erreichbarkeit von außerhalb des Clusters bereitzustellen
- [ingressBackend.yaml](deployment/ingressBackend.yaml), [ingressFrontend.yaml](deployment/ingressFrontend.yaml): LoadBalancer zum Verknüpfen der Services mit einer externen aufrufbaren URL

Ist der Kubernetes Kontext richtig gesetzt kann man diese Ressourcen jeweils mit

    kubectl apply -f <Dateiname>

im Kubernetes Cluster applizieren.
Diese Ressourcen können auch wenn das im konkreten Deployment-Szenario praktisch ist als helm-chart gebündelt deployed werden.

*Hinweis*: Die Bereitstellung mit TLS Zertifikaten ist in diesem Beispiel nicht mit aufgezeigt. Dies wäre für den produktiven Betrieb noch zu ergänzen. 
Dazu kann z.b. ein ClusterIssuer zum Einsatz kommen.

## Skalierung
*Vertikale* Skalierung ist durch Änderung der *replicas* im Kubernetes-Deployment leicht möglich. 

Dabei ist zu beachten, dass die periodische Aktualisierung der DB durch den Abruf der User von der randomuser-API in einen
weiteren Microservice ausgelagert werden sollte,

*Horizontale* Skalierung ist beispielsweise durch Erhöhung der JVM-Ressourcen über die Umgebungsvaribale *JAVA_OPTS* gegeben.








