package com.isi.socketapp.entities;

import com.isi.socketapp.Client;
import com.isi.socketapp.controller.MessagerieController;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
public class Chat {
    public Chat(String message, int idClient) {
        this.message = message;
        this.idClient = idClient;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_chat")
    private int idChat;
    @Basic
    @Column(name = "message")
    private String message;
    @Basic
    @Column(name = "date_enreg")
    private Timestamp dateEnreg;
    @Basic
    @Column(name = "id_client")
    private int idClient;

    public Chat() {

    }

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDateEnreg() {
        return dateEnreg;
    }

    public void setDateEnreg(Timestamp dateEnreg) {
        this.dateEnreg = dateEnreg;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return idChat == chat.idChat && idClient == chat.idClient && Objects.equals(message, chat.message) && Objects.equals(dateEnreg, chat.dateEnreg);
    }

    @Override
    public String toString() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT u.nom FROM User u WHERE  u.id = :idClient");
        query.setParameter("idClient", idClient);
        String nomcli = (String) query.getSingleResult();
        if(MessagerieController.nomCli ==nomcli) {
            return "                                                      " +nomcli + "  :    " + message + "\n" +
                   "                                                               date   :  " + dateEnreg.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" + "\n" + "\n";

        }else {
            return nomcli + "  :    " + message + "\n" +
                    "      date   :  " + dateEnreg.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" + "\n" + "\n";

        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(idChat, message, dateEnreg, idClient);
    }
}
