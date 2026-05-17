package com.gabrielqt.gtpay.entity;

import com.gabrielqt.gtpay.entity.enums.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "webhook_subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(nullable = false)
    private String path; // ex: store/webhook/paid

    @Column(nullable = false, name = "is_active")
    private boolean isActive;

    @NotEmpty
    private String secret; // to sign the payload with HMAC

    @Enumerated(EnumType.STRING)
    private EventType event; // the event that this url listens to

    public String getUrl(){
        return merchant.getBaseUrl() + "/" + path;
    }
}
