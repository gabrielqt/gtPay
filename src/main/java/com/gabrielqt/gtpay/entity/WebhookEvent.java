package com.gabrielqt.gtpay.entity;

import com.gabrielqt.gtpay.entity.enums.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(columnDefinition = "TEXT")
    private String payload; // json do que foi enviado

    private Boolean delivered;
    private Integer attempts;
    private LocalDateTime lastAttemptAt;

    @ManyToOne
    @JoinColumn(name = "charge_id")
    private Charge charge;

    @ManyToOne()
    @JoinColumn(name =  "subscription_id")
    private WebhookSubscription subscription;

}
