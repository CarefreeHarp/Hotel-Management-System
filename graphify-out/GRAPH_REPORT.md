# Graph Report - Hotel-Management-System  (2026-08-23)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 79 nodes · 102 edges · 14 communities (5 shown, 9 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 6 edges (avg confidence: 0.83)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9126d173`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- index/main.js
- Servicio
- ServiceController.java
- mvnw
- DemoApplicationTests.java
- DemoApplication
- servicio_especifico.js
- hotelVideoObserver
- com.example.demo.entitys.Servicio
- com.example.demo.repository.ServicioRepository
- Servicio
- java.security.Provider.Service
- com.example:demo
- ServicioService

## God Nodes (most connected - your core abstractions)
1. `Servicio` - 13 edges
2. `ServicioRepositoryMemoria` - 8 edges
3. `StudentServiceImpl` - 6 edges
4. `ServiceController` - 5 edges
5. `ServicioService` - 5 edges
6. `IndexController` - 3 edges
7. `DemoApplicationTests` - 3 edges
8. `DemoApplication` - 3 edges
9. `startHotelVideo()` - 2 edges
10. `hotelVideoObserver` - 2 edges

## Surprising Connections (you probably didn't know these)
- `StudentServiceImpl` --implements--> `ServicioService`  [EXTRACTED]
  demo/src/main/java/com/example/demo/service/StudentServiceImpl.java → demo/src/main/java/com/example/demo/service/ServicioService.java
- `ServicioRepositoryMemoria` --references--> `Servicio`  [EXTRACTED]
  demo/src/main/java/com/example/demo/repository/ServicioRepositoryMemoria.java → demo/src/main/java/com/example/demo/entitys/Servicio.java
- `StudentServiceImpl` --references--> `ServicioRepositoryMemoria`  [EXTRACTED]
  demo/src/main/java/com/example/demo/service/StudentServiceImpl.java → demo/src/main/java/com/example/demo/repository/ServicioRepositoryMemoria.java
- `ServiceController` --references--> `ServicioService`  [EXTRACTED]
  demo/src/main/java/com/example/demo/controller/ServiceController.java → demo/src/main/java/com/example/demo/service/ServicioService.java

## Import Cycles
- None detected.

## Communities (14 total, 9 thin omitted)

### Community 0 - "index/main.js"
Cohesion: 0.11
Nodes (13): fadeObserver, fadeTargets, header, headerObserver, hero, scrollProgress, scrollVideo, suiteCards (+5 more)

### Community 1 - "Servicio"
Cohesion: 0.23
Nodes (9): Servicio, ServicioRepositoryMemoria, StudentServiceImpl, lombok.AllArgsConstructor, lombok.Data, lombok.NoArgsConstructor, org.springframework.stereotype.Repository, org.springframework.stereotype.Service (+1 more)

### Community 2 - "ServiceController.java"
Cohesion: 0.24
Nodes (8): IndexController, ServiceController, ServicioService, org.springframework.stereotype.Controller, org.springframework.ui.Model, org.springframework.web.bind.annotation.GetMapping, org.springframework.web.bind.annotation.RequestMapping, Servicio

### Community 3 - "mvnw"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 4 - "DemoApplicationTests.java"
Cohesion: 0.60
Nodes (3): DemoApplicationTests, org.junit.jupiter.api.Test, org.springframework.boot.test.context.SpringBootTest

## Knowledge Gaps
- **16 isolated node(s):** `fadeObserver`, `fadeTargets`, `header`, `headerObserver`, `hero` (+11 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **9 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Servicio` connect `Servicio` to `ServiceController.java`?**
  _High betweenness centrality (0.077) - this node is a cross-community bridge._
- **Why does `ServicioService` connect `ServiceController.java` to `Servicio`?**
  _High betweenness centrality (0.014) - this node is a cross-community bridge._
- **What connects `fadeObserver`, `fadeTargets`, `header` to the rest of the system?**
  _16 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `index/main.js` be split into smaller, more focused modules?**
  _Cohesion score 0.1111111111111111 - nodes in this community are weakly interconnected._