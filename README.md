# Atlan Suites – Hotel Management System

Atlan Suites es un sistema de gestión hotelera desarrollado como proyecto semestral de Desarrollo Web. La aplicación permite administrar reservas, habitaciones, servicios, cuentas de consumo y los diferentes portales para clientes, operarios y administradores.

## Diseño

El diseño de la interfaz está disponible en Figma:

**Figma:** https://www.figma.com/design/AzKmb3UaMsc4t8JZCZXiL4/AtlanSuites?node-id=0-1&t=IUIAEeybnugZna8y-1
** Figma Interactivo:** https://pants-toggle-56795002.figma.site

### Identidad visual

#### Logo

![Logo de Atlan Suites](<ReadMe assets/atlan-suites-logo.jpeg>)

#### Paleta de colores

![Paleta de colores de Atlan Suites](<ReadMe assets/atlan-suites-color-palette.jpeg>)

---

## Navigation Diagram

[Open the interactive navigation diagram (download and open it in your browser)](<ReadMe assets/NavigationDiagramSprint2AtlanSuites.html>)

1. **Home:** `/home` — Landing page
2. **Suites:** `/suites` — Catálogo de habitaciones (con botones para ver detalles)
   - **Suite Detail:** `/suites/{suiteId}` — Detalle de una habitación (con botón para reservar)
3. **Book Now:** `/book-now` — Diligenciar fechas de entrada, salida y número de huéspedes
4. **Check Availability:** `/check-availability` — Mostrar las habitaciones realmente disponibles según los datos de Book Now y, si se llegó desde una suite, filtrar también por la habitación seleccionada
   - **Guest Details:** `/guest-details` — Datos del huésped
   - **Payment:** `/payment` — Pago y confirmación
5. **Experiences:** `/experiences` — Catálogo de experiencias que no sean de temporada ni románticas
   - **Experiences Detail:** `/experiences/{experienceId}` — Detalle de una experiencia específica
   - **Seasonal Events:** `/experiences/seasonal-events` — Catálogo de eventos de temporada
   - **Seasonal Event Detail:** `/experiences/seasonal-events/{eventId}` — Detalle de un evento de temporada
   - **Romantic Experiences:** `/experiences/romantic-experiences` — Catálogo de experiencias románticas
   - **Romantic Experience Detail:** `/experiences/romantic-experiences/{experienceId}` — Detalle de una experiencia romántica
6. **Getting Here:** `/getting-here` — Mapa para visualizar dónde queda el hotel
7. **Explore the Hotel:** `/explore` — Fotos y videos del hotel por dentro y por fuera
8. **Restaurant:** `/restaurant` — Fotos del restaurante
   - **Restaurant Menu:** `/restaurant/menu` — Menú del restaurante
9. **Hotel Awards:** `/awards` — Premios otorgados al hotel
10. **Services List:** `/services/list`
11. **Services Cards:** `/services/cards`
12. **Service:** `/services/{serviceName}`

---

# Diagrama Entidad–Relación (ER)

Este diagrama representa la estructura de la base de datos del sistema. Muestra las entidades, sus atributos, claves primarias (PK), claves foráneas (FK), restricciones de unicidad (UK) y las relaciones de cardinalidad entre ellas.

```mermaid
erDiagram

    CLIENTE {
        INT id_cliente PK
        VARCHAR nombre
        VARCHAR apellido
        VARCHAR cedula UK
        VARCHAR telefono
        VARCHAR correo UK
        VARCHAR password
    }

    OPERARIO {
        INT id_operario PK
        VARCHAR nombre
        VARCHAR apellido
        VARCHAR correo UK
        VARCHAR password
        INT id_admin FK
    }

    ADMINISTRADOR {
        INT id_admin PK
        VARCHAR nombre
        VARCHAR correo UK
        VARCHAR password
    }

    TIPO_HABITACION {
        INT id_tipo PK
        VARCHAR nombre UK
        TEXT descripcion
        DECIMAL precio_noche
        INT capacidad_maxima
    }

    HABITACION {
        INT numero PK
        INT piso
        ENUM estado
        BOOLEAN disponible
        INT id_tipo FK
        INT id_admin FK
    }

    RESERVA {
        INT id_reserva PK
        DATE fecha_inicio
        DATE fecha_fin
        INT cantidad_personas
        ENUM estado
        DATE fecha_creacion
        INT id_cliente FK
        INT numero_habitacion FK
        INT id_operario FK
    }

    SERVICIO {
        INT id_servicio PK
        VARCHAR nombre UK
        TEXT descripcion
        DECIMAL precio
        VARCHAR categoria
        BOOLEAN activo
        INT id_admin FK
    }

    CUENTA {
        INT id_cuenta PK
        DECIMAL total
        INT numero_habitacion FK
    }

    ITEM_CUENTA {
        INT id_item PK
        INT cantidad
        DECIMAL subtotal
        INT id_cuenta FK
        INT id_servicio FK
    }

    PAGO {
        INT id_pago PK
        DATE fecha
        DECIMAL monto
        VARCHAR metodo
        INT id_cuenta FK
        INT id_operario FK
    }

    CLIENTE ||--o{ RESERVA : realiza
    TIPO_HABITACION ||--o{ HABITACION : clasifica
    HABITACION ||--o{ RESERVA : asignada
    HABITACION ||--|| CUENTA : posee
    CUENTA ||--o{ ITEM_CUENTA : contiene
    SERVICIO ||--o{ ITEM_CUENTA : contratado
    CUENTA ||--o{ PAGO : genera

    ADMINISTRADOR ||--o{ OPERARIO : crea
    ADMINISTRADOR ||--o{ HABITACION : administra
    ADMINISTRADOR ||--o{ SERVICIO : administra

    OPERARIO ||--o{ RESERVA : gestiona
    OPERARIO ||--o{ PAGO : registra
```

---

# Diagrama de Clases (UML)

Este diagrama describe la arquitectura orientada a objetos del proyecto. Incluye las clases principales del dominio, sus atributos, métodos, enumeraciones y las relaciones existentes entre clientes, reservas, habitaciones, servicios y los distintos roles del sistema.

```mermaid
classDiagram
direction LR

class Cliente{
  +int idCliente
  +string nombre
  +string apellido
  +string cedula
  +string telefono
  +string correo
  +string password
  +registrarse()
  +iniciarSesion()
  +actualizarPerfil()
  +verReservasActivas()
  +verHistorial()
  +crearReserva()
  +cancelarReserva()
  +modificarReserva()
}

class Operario{
  +int idOperario
  +string nombre
  +string apellido
  +string correo
  +string password
  +iniciarSesion()
  +consultarReservas()
  +filtrarReservas()
  +cancelarReserva()
  +agregarServicio()
  +eliminarServicio()
  +cobrarCuenta()
  +realizarCheckout()
}

class Administrador{
  +int idAdministrador
  +string nombre
  +string correo
  +string password
  +iniciarSesion()
  +crearOperario()
  +editarOperario()
  +eliminarOperario()
  +crearServicio()
  +editarServicio()
  +eliminarServicio()
  +crearHabitacion()
  +editarHabitacion()
  +deshabilitarHabitacion()
  +eliminarHabitacion()
}

class Reserva{
  +int idReserva
  +date fechaInicio
  +date fechaFin
  +int cantidadPersonas
  +EstadoReserva estado
  +date fechaCreacion
  +confirmar()
  +cancelar()
  +modificar()
  +calcularNoches()
}

class Habitacion{
  +int numero
  +int piso
  +EstadoHabitacion estado
  +bool disponible
  +habilitar()
  +deshabilitar()
  +estaDisponible()
}

class TipoHabitacion{
  +int idTipo
  +string nombre
  +string descripcion
  +double precioNoche
  +int capacidadMaxima
  +actualizarPrecio()
}

class Servicio{
  +int idServicio
  +string nombre
  +string descripcion
  +double precio
  +CategoriaServicio categoria
  +bool activo
  +actualizar()
  +desactivar()
}

class Cuenta{
  +int idCuenta
  +double total
  +agregarItem()
  +eliminarItem()
  +calcularTotal()
  +vaciar()
  +pagar()
}

class ItemCuenta{
  +int idItem
  +int cantidad
  +double subtotal
  +calcularSubtotal()
}

class Pago{
  +int idPago
  +date fecha
  +double monto
  +MetodoPago metodo
  +procesar()
}

class EstadoReserva{
  <<enumeration>>
  Pendiente
  Confirmada
  Activa
  Finalizada
  Cancelada
}

class EstadoHabitacion{
  <<enumeration>>
  Disponible
  Ocupada
  Mantenimiento
}

class MetodoPago{
  <<enumeration>>
  Efectivo
  Tarjeta
  Transferencia
}

class CategoriaServicio{
  <<enumeration>>
  Spa
  Restaurante
  Transporte
  Tour
  Lavanderia
}

Cliente "1" --> "0..*" Reserva : realiza
Reserva "1" --> "1" Habitacion : asigna
Habitacion "1" --> "1" TipoHabitacion : pertenece

Habitacion "1" *-- "1" Cuenta : posee
Cuenta "1" *-- "0..*" ItemCuenta : contiene
ItemCuenta "*" --> "1" Servicio : corresponde

Cuenta "1" --> "0..*" Pago : registra

Operario ..> Reserva : gestiona
Operario ..> Cuenta : administra
Operario ..> Servicio : agrega

Administrador ..> Habitacion : CRUD
Administrador ..> Servicio : CRUD
Administrador ..> Operario : CRUD

Reserva ..> EstadoReserva : usa
Habitacion ..> EstadoHabitacion : usa
Pago ..> MetodoPago : usa
Servicio ..> CategoriaServicio : usa
```
