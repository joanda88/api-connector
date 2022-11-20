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

Starten einer PostgreSQL-Datenbank:

    docker-compose -f stack.yml up

## Backend
Bauen mit:

    mvn install

Starten mit:

    mvn spring-boot:run

### Tests
Unittests kann man einzeln mit:

    mvn test

starten. 

Die Integrationstests starten mit:

    mvn verify 

Da hierfür eine PostgreSQL-DB verwendet wird, 
sollte der docker-compose-stack für die Integrationstests vorher hochgefahren sein (siehe Test-Infrastruktur).

### Hinweis zu Spring Profilen:
* `test-data` - führt zusätzlich zu den "normalen" Flyway-Migrationen
  auch die aus den `test-migrations` Verzeichnissen aus und spielt so
  einen Bestand an Testdaten ein.

### Wichtige Konfigurationsparameter

Diese sind in der [application.yml](src/main/resources/application.yml) zu finden:

* `db_host`: Hostname der Postgres Datenbank
* `db_name`: Name der Postgres Datenbank
* `db_schema`: Schema der Postgres Datenbank
* `db_username`: Username der Postgres Datenbank
* `db_password`: Passwort der Postgres Datenbank
* `cors.origin_urls`:  Erlaubte origins
* `users_url`: URL der randomuser-API

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

Zum Tests-Schreiben und Debuggen dieser empfiehlt sich allerdings:

    npx cypress open

Hierbei wird der installierte Chrome-Browser gestartet und die Tests
können über eine UI gestartet und debugged werden.

## Deployment

Zum Deployen der Applikation in eine container-basierte Cloud Umgebung (Kubernetes) 
müssen die folgenden Schritte unternommen werden:

Für das Backend liegt ein [Dockerfile](Dockerfile) auf Root-Ebene des Projektes,
mit diesem Dockerfile kann die Spring-Boot Backend-Applikation als Docker-Container gebaut und deployed werden.
Vorraussetzung ist, dass im target-Ordner vorher mittels 'mvn install' das entsprechende jar bereitgestellt wurde. 

Ebenso steht im Ordner [api-connector-spa](api-connector-spa) 
für das Frontend ein [Dockerfile](api-connector-spa/Dockerfile) bereit. 
So kann auch das Frontend containerisiert ausgeliefert werden. 

Nach dem Erstellen der Docker-Images müssen diese Images in eine passende Docker-Registry gepushed werden.

Liegen diese Images in einer Docker-Registry vor, können diese z.B. in einer 
Kubernetes Deployment-Ressource referenziert und damit schlussendlich in einen Kubernetes Cluster deployed werden.

Ein beispielhaftes Kubernetes Deployment findet sich im [deployment](deployment) Ordner.
Dort finden sich eine ganze Reihe von nötigen/empfehlenswerten Kubernetes Ressourcen:

- [configMap.yaml](deployment/configMap.yaml): Hier kann zur CORS Konfiguration der Host des Backends angegeben werden
- [dbSecret.yaml](deployment/dbSecret.yaml): Hier wird in einem Secret der Zugang zur DB definiert
- [deploymentBackend.yaml](deployment/deploymentBackend.yaml), [deploymentFrontend.yaml](deployment/deploymentFrontend.yaml): Deployment des Backends und Frontends. Hier wird auch auf die configmap und das DB-Secret zurückgegriffen
- [serviceBackend.yaml](deployment/serviceBackend.yaml), [serviceFrontend.yaml](deployment/serviceFrontend.yaml): Diese dienen dazu die Pods für Frontend und Backend dem jeweiligen LoadBalancer(ingress) zur Erreichbarkeit von außerhalb des Clusters bereitzustellen
- [ingressBackend.yaml](deployment/ingressBackend.yaml), [ingressFrontend.yaml](deployment/ingressFrontend.yaml): LoadBalancer zum Verknüpfen der Services mit einer externen aufrufbaren URL

Ist der Kubernetes Kontext richtig gesetzt kann man diese Ressourcen jeweils mit

    kubectl apply -f <Dateiname>

im Kubernetes Cluster applizieren.
Diese Ressourcen könnten, wenn das im konkreten Deployment-Szenario praktisch ist, als helm-chart gebündelt deployed werden.

*Hinweis*: Die Bereitstellung mit TLS Zertifikaten ist in diesem Beispiel nicht mit aufgezeigt. Dies wäre für den produktiven Betrieb noch zu ergänzen. 
Dazu kann z.b. ein ClusterIssuer zum Einsatz kommen.

## Skalierung
*Vertikale* Skalierung ist durch Änderung der *replicas* im Kubernetes-Deployment für Frontend und Backend leicht möglich. 

Bezüglich des Backends gilt es für das periodische Aktualisieren der User-Daten folgendes zu beachten:
Mit der derzeitigen Implementierung würden mit skalierten Backends die perodischen Aktualisierungen 
überflüssigerweise häufiger erfolgen, da jedes Backend dies im eingestellten Zeitraster stur vornehmen würde.

Um dies zu verhindern gäbe es z.B. zwei Lösungen:

- Auslagern des [UpdateUserService](src/main/java/de/jeggers/apiconnector/app/users/domain/UpdateUserService.java) 
  und [FetchUserService](src/main/java/de/jeggers/apiconnector/app/users/domain/FetchUserService.java) 
  in einen eigenen Microservice, der unskaliert als Singleton betrieben wird
- Verwenden eines Quartz-Jobs, hiermit ist es mit einfacher Konfiguration 
([@DisallowConcurrentExecution](http://www.quartz-scheduler.org/api/2.1.7/org/quartz/DisallowConcurrentExecution.html)) 
  möglich konkurrierende Updates zu verhindern. 
  Dabei wird die PostgreSQL Datenbank z.B. als Synchronisierungsinstanz verwendet.


*Horizontale* Skalierung ist durch Verwendung leistungsfähigerer Knoten im Kubernetes-Cluster möglich. 
Hier spielen ggf. auch
die [RAM](https://kubernetes.io/docs/tasks/configure-pod-container/assign-memory-resource/) 
und [CPU](https://kubernetes.io/docs/tasks/configure-pod-container/assign-cpu-resource/) 
Zuweisungen für die Container / Pods eine Rolle.

Im Backend sollten idealerweise der JVM passende Limits für den RAM über die Umgebungsvariable *JAVA_OPTS* 
mitgeteilt werden.

### *Allgemeiner Hinweis*: 
Frontend und Backend sind idealerweise in zwei getrennten Git-Repositories zu verwalten. 
Das gemeinsame Git-Repository existiert in diesem Fall nur um es als "gebündelten Showcase" leicht präsentieren zu können.  




