package ru.practicum.ewm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "endpoint_hit")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EndpointHit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "app", nullable = false)
    String app;

    @Column(name = "ip", nullable = false, length = 15)
    String ip;

    @Column(name = "uri", nullable = false)
    String uri;

    @Column(name = "timestamp", nullable = false, columnDefinition = "timestamp")
    LocalDateTime timestamp;
}
