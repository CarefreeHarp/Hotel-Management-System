# Graph Report - Hotel-Management-System  (2026-08-23)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 79 nodes · 102 edges · 12 communities (5 shown, 7 thin omitted)
- Extraction: 94% EXTRACTED · 6% INFERRED · 0% AMBIGUOUS · INFERRED: 6 edges (avg confidence: 0.83)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9126d173`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- ServiceController.java
- index/main.js
- ServicioRepositoryMemoria
- mvnw
- DemoApplicationTests.java
- DemoApplication
- servicio_especifico.js
- hotelVideoObserver
- com.example.demo.repository.ServicioRepository
- Servicio
- java.security.Provider.Service
- com.example:demo

## God Nodes (most connected - your core abstractions)
1. `ServicioRepositoryMemoria` - 8 edges
2. `Servicio` - 7 edges
3. `StudentServiceImpl` - 6 edges
4. `ServiceController` - 5 edges
5. `ServicioService` - 4 edges
6. `IndexController` - 3 edges
7. `DemoApplicationTests` - 3 edges
8. `DemoApplication` - 3 edges
9. `startHotelVideo()` - 2 edges
10. `hotelVideoObserver` - 2 edges

## Surprising Connections (you probably didn't know these)
- `ServiceController` --references--> `ServicioService`  [EXTRACTED]
  demo/src/main/java/com/example/demo/controller/ServiceController.java → demo/src/main/java/com/example/demo/service/ServicioService.java
- `StudentServiceImpl` --references--> `ServicioRepositoryMemoria`  [EXTRACTED]
  demo/src/main/java/com/example/demo/service/StudentServiceImpl.java → demo/src/main/java/com/example/demo/repository/ServicioRepositoryMemoria.java

## Import Cycles
- None detected.

## Communities (12 total, 7 thin omitted)

### Community 0 - "ServiceController.java"
Cohesion: 0.21
Nodes (11): IndexController, ServiceController, Servicio, ServicioService, lombok.AllArgsConstructor, lombok.Data, lombok.NoArgsConstructor, org.springframework.stereotype.Controller (+3 more)

### Community 1 - "index/main.js"
Cohesion: 0.11
Nodes (13): fadeObserver, fadeTargets, header, headerObserver, hero, scrollProgress, scrollVideo, suiteCards (+5 more)

### Community 2 - "ServicioRepositoryMemoria"
Cohesion: 0.24
Nodes (8): com.example.demo.entitys.Servicio, ServicioRepositoryMemoria, StudentServiceImpl, org.springframework.stereotype.Repository, org.springframework.stereotype.Service, Override, Servicio, ServicioService

### Community 3 - "mvnw"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 4 - "DemoApplicationTests.java"
Cohesion: 0.60
Nodes (3): DemoApplicationTests, org.junit.jupiter.api.Test, org.springframework.boot.test.context.SpringBootTest

## Knowledge Gaps
- **16 isolated node(s):** `fadeObserver`, `fadeTargets`, `header`, `headerObserver`, `hero` (+11 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **7 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What connects `fadeObserver`, `fadeTargets`, `header` to the rest of the system?**
  _16 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `index/main.js` be split into smaller, more focused modules?**
  _Cohesion score 0.1111111111111111 - nodes in this community are weakly interconnected._