package com.isi.socketapp.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id_client")
    private int idClient;
    @Basic
    @Column(name = "nom")
    private String nom;
    @Basic
    @Column(name = "date_enreg")
    private Timestamp date_enreg;

    public Timestamp getDate_enreg() {
        return date_enreg;
    }

    public void setDate_enreg(Timestamp date_enreg) {
        this.date_enreg = date_enreg;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return idClient == user.idClient && Objects.equals(nom, user.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClient, nom);
    }
}
