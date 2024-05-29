package com.isi.socketapp.controller;

import com.isi.socketapp.Client;
import com.isi.socketapp.Main;
import com.isi.socketapp.entities.Chat;
import com.isi.socketapp.entities.User;
import com.isi.socketapp.utils.Outils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.*;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConnexionControler extends Thread {
    @FXML
    private  TextField nomtfd;

    @FXML
    private Button bntlogin;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    EntityManager em = emf.createEntityManager();
    EntityTransaction et = em.getTransaction();
    public List<User> getClient(){
       List<User>utilisateur= new ArrayList<>();
        String jpql = "SELECT u FROM User u"; // Utilisation de JPQL
        try {
            et.begin();
            Query query = em.createQuery(jpql, User.class);
            utilisateur.addAll(query.getResultList());
        }catch (Exception e){
            if(et.isActive())
                et.rollback();
        }
        return  utilisateur;
    }
    @FXML
    void login(ActionEvent event) throws IOException {
    List<User> clients= getClient();
        boolean utilisateurPresent = false;
        if (!clients.isEmpty()){
            for (User client : clients) {
                if (client.getNom().equals(nomtfd.getText())) {
                    utilisateurPresent = true;
                    MessagerieController.nomCli=client.getNom();
                    MessagerieController.id_cli=client.getIdClient();
                    break; // Vous pouvez sortir de la boucle dès que l'utilisateur est trouvé
                }
            }

            if (utilisateurPresent) {
                try {
                    Outils.load(event, "MESSAGERIE", "/pages/messagerie.fxml");
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                try {

                    User u =new User();
                    u.setNom(nomtfd.getText());
                    u.setDate_enreg(Timestamp.valueOf(LocalDateTime.now()));
                    em.persist(u);
                    et.commit();
                    MessagerieController.nomCli=u.getNom();
                    MessagerieController.id_cli=u.getIdClient();
                    Outils.load(event, "MESSAGERIE", "/pages/messagerie.fxml");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else {
            try {
                User u =new User();
                u.setNom(nomtfd.getText());
                em.persist(u);
                et.commit();
                MessagerieController.nomCli=u.getNom();
                MessagerieController.id_cli=u.getIdClient();

                Outils.load(event, "MESSAGERIE", "/pages/messagerie.fxml");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, nomtfd.getText());
        // client.listenForMessage();
        //  client.sendMessage();

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // duplication du programme
    @Override
    public void run() {
        Platform.runLater(() -> {
            try {
                startNewConnexionControler();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static void main(String[] args) throws IOException {
        // Démarrage de l'application JavaFX
        Application.launch(App.class, args);
    }

    public static class App extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            // Création et démarrage de plusieurs threads
            for (int i = 0; i < 3; i++) {
                ConnexionControler thread = new ConnexionControler();
                thread.start();
            }
        }
    }
    private void startNewConnexionControler() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pages/connexion.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
