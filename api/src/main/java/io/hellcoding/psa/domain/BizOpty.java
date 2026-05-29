package io.hellcoding.psa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "biz_opties")
public class BizOpty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealStatus dealStatus;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal contractAmount;

    protected BizOpty() {
    }

    public BizOpty(String name, String clientName, DealStatus dealStatus, BigDecimal contractAmount) {
        this.name = name;
        this.clientName = clientName;
        this.dealStatus = dealStatus;
        this.contractAmount = contractAmount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClientName() {
        return clientName;
    }

    public DealStatus getDealStatus() {
        return dealStatus;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }
}
