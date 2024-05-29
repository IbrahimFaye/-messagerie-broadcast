module com.isi.socketapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;


    opens com.isi.socketapp to javafx.fxml;
    exports com.isi.socketapp;
    opens com.isi.socketapp.controller;
    exports com.isi.socketapp.controller;
    opens com.isi.socketapp.entities;
   exports com.isi.socketapp.entities;
}