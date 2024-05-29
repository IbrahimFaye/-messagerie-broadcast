package com.isi.socketapp;

import com.isi.socketapp.controller.MessagerieController;
import com.isi.socketapp.entities.Chat;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import javax.persistence.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static String username;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    EntityManager em = emf.createEntityManager();
    EntityTransaction et = em.getTransaction();
    public Client(Socket socket, String username){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public  void sendMessage(Chat chat){
        try {
            // Requête JPA pour récupérer le nom de l'utilisateur avec l'ID
            Query query = em.createQuery("SELECT u.nom FROM User u WHERE u.id = :id_client");
            query.setParameter("id_client", chat.getIdClient());
            String nomcli = (String) query.getSingleResult();

            bufferedWriter.write(nomcli);
            bufferedWriter.newLine();
            bufferedWriter.flush();

                bufferedWriter.write(nomcli + " : " +chat.getMessage());
                bufferedWriter.newLine();
                bufferedWriter.flush();

        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);

                        Chat chat = new Chat();
                        chat.setMessage(msgFromGroupChat);
                        // Ajoutez le chat à la liste observable
                        Platform.runLater(() -> MessagerieController.chatList.add(chat));
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
