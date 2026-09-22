package com.example.demo.config;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Provee un reloj compartido que puede alternar entre tiempo real y tiempo fijo. */
@Component
public class ApplicationClock extends Clock {

    private final ZoneId zone;
    private volatile Clock delegate;
    private volatile boolean fixed;

    public ApplicationClock(
            @Value("${app.clock.mode:system}") String mode,
            @Value("${app.clock.zone:America/Bogota}") String zoneName,
            @Value("${app.clock.fixed-instant:}") String fixedInstant) {
        this.zone = ZoneId.of(zoneName);
        if ("fixed".equalsIgnoreCase(mode)) {
            if (fixedInstant.isBlank()) {
                throw new IllegalStateException("app.clock.fixed-instant is required when app.clock.mode is fixed.");
            }
            this.delegate = Clock.fixed(OffsetDateTime.parse(fixedInstant).toInstant(), zone);
            this.fixed = true;
            return;
        }
        this.delegate = Clock.system(zone);
        this.fixed = false;
    }

    /** Fija la fecha y hora de la aplicación usando el huso horario configurado. */
    public synchronized void setFixedDateTime(LocalDateTime dateTime) {
        this.delegate = Clock.fixed(dateTime.atZone(zone).toInstant(), zone);
        this.fixed = true;
    }

    /** Restaura el reloj de la aplicación a la hora real. */
    public synchronized void useSystemTime() {
        this.delegate = Clock.system(zone);
        this.fixed = false;
    }

    /** Indica si la aplicación está utilizando una hora fija de pruebas. */
    public boolean isFixed() {
        return fixed;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId requestedZone) {
        return delegate.withZone(requestedZone);
    }

    @Override
    public Instant instant() {
        return delegate.instant();
    }
}
