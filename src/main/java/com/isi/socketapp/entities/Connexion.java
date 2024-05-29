package com.isi.socketapp.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Connexion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_con")
    private int idCon;
    @Basic
    @Column(name = "id_client")
    private int idClient;
    @Basic
    @Column(name = "date_con")
    private int dateCon;
    @Basic
    @Column(name = "date_decon")
    private int dateDecon;

    public int getIdCon() {
        return idCon;
    }

    public void setIdCon(int idCon) {
        this.idCon = idCon;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getDateCon() {
        return dateCon;
    }

    public void setDateCon(int dateCon) {
        this.dateCon = dateCon;
    }

    public int getDateDecon() {
        return dateDecon;
    }

    public void setDateDecon(int dateDecon) {
        this.dateDecon = dateDecon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connexion connexion = (Connexion) o;
        return idCon == connexion.idCon && idClient == connexion.idClient && dateCon == connexion.dateCon && dateDecon == connexion.dateDecon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCon, idClient, dateCon, dateDecon);
    }
}
