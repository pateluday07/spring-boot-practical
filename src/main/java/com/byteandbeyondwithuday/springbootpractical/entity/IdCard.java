package com.byteandbeyondwithuday.springbootpractical.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class IdCard {

    @Id
    @Column(name = "employee_id")
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String cardNumber;
    @Column(nullable = false)
    private LocalDate issueDate;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    @JsonIgnore
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IdCard idCard)) return false;
        return Objects.equals(id, idCard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
