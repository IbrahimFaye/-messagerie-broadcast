package com.isi.socketapp.controller;

import com.isi.socketapp.Client;
import com.isi.socketapp.entities.Chat;
import com.isi.socketapp.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.persistence.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MessagerieController implements Initializable {
   public  static int id_cli;
   public static String nomCli;
    @FXML
    private ListView<Chat> listV;
    @FXML
    private TextField profiltfd;

    @FXML
    private TextArea messageArea;


    @FXML
    private Button btnEvoyer;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    EntityManager em = emf.createEntityManager();
    EntityTransaction et = em.getTransaction();

    @FXML
    void envoyer(ActionEvent event) {
        // Envoyer le message au serveur via le client
        Socket socket = null;
        try {
            Query query = em.createQuery("SELECT u.id FROM User u WHERE u.nom = :nom");
            query.setParameter("nom", profiltfd.getText());
            int idcli = (int) query.getSingleResult();

            et.begin();
            Chat chat = new Chat();
            chat.setMessage(messageArea.getText());
            chat.setIdClient(idcli);
            chat.setDateEnreg(Timestamp.valueOf(LocalDateTime.now()));
            em.persist(chat);
            et.commit();

            socket = new Socket("localhost", 1234);
            Client client = new Client(socket,profiltfd.getText());
            client.sendMessage(chat);
            // Ajouter le message à la liste observable pour l'afficher dans le ListView
            chatList.add(chat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Effacer le contenu de la zone de texte après l'envoi
        messageArea.clear();
    }
    public static ObservableList<Chat> chatList=FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profiltfd.setText(nomCli);
        // Connexion au serveur
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1234);
            Client client = new Client(socket,nomCli);
            client.listenForMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chatList.clear();
        List<Chat> AncienChat=getChat();
       // chatList.addAll(AncienChat);
        listV.setItems(chatList);
    }
    public List<Chat> getChat(){
        List<Chat>chat= new ArrayList<>();
        String jpql = "SELECT c FROM Chat c ORDER BY dateEnreg asc "; // Utilisation de JPQL
        try {

            Query query = em.createQuery(jpql, Chat.class);
            chat.addAll(query.getResultList());
        }catch (Exception e){
           e.printStackTrace();
        }
        return chat;
    }
}
