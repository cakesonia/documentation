package com.team.rent.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A RentalPoint.
 */
@Entity
@Table(name = "rental_point")
public class RentalPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "worktime")
    private ZonedDateTime worktime;

    @OneToOne
    @JoinColumn(unique = true)
    private Autopark autopark;

    @ManyToMany
    @JoinTable(name = "rental_point_clients",
               joinColumns = @JoinColumn(name = "rental_point_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "clients_id", referencedColumnName = "id"))
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public RentalPoint location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZonedDateTime getWorktime() {
        return worktime;
    }

    public RentalPoint worktime(ZonedDateTime worktime) {
        this.worktime = worktime;
        return this;
    }

    public void setWorktime(ZonedDateTime worktime) {
        this.worktime = worktime;
    }

    public Autopark getAutopark() {
        return autopark;
    }

    public RentalPoint autopark(Autopark autopark) {
        this.autopark = autopark;
        return this;
    }

    public void setAutopark(Autopark autopark) {
        this.autopark = autopark;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public RentalPoint clients(Set<Client> clients) {
        this.clients = clients;
        return this;
    }

    public RentalPoint addClients(Client client) {
        this.clients.add(client);
        client.getRentalPoints().add(this);
        return this;
    }

    public RentalPoint removeClients(Client client) {
        this.clients.remove(client);
        client.getRentalPoints().remove(this);
        return this;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentalPoint)) {
            return false;
        }
        return id != null && id.equals(((RentalPoint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RentalPoint{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", worktime='" + getWorktime() + "'" +
            "}";
    }
}
