/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.core;

import com.client.model.Message;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josip
 */
public class BroadcastServerProxy {

    private ChatListener chatListener;
    private String username;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;
    public Gson gson;

    public BroadcastServerProxy(String username, Socket socket) {
        try {
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.socket = socket;
        } catch (IOException ex) {
            Logger.getLogger(BroadcastServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    public void join() {
        try {
            Message message = new Message();
            message.setCommand(Command.JOIN);
            message.setFrom(this.username);
            processMessage(message);
            String toJson = new Gson().toJson(message, Message.class);
            bufferedWriter.write(toJson);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(BroadcastServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void leave() {
        try {
            Message message = new Message();
            message.setCommand(Command.LEAVE);
            message.setFrom(this.username);
            String toJson = new Gson().toJson(message, Message.class);
            bufferedWriter.write(toJson);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(BroadcastServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(String msg) {
        try {
            Message message = new Message();
            message.setCommand(Command.MESSAGE);
            message.setFrom(this.username);
            message.setMessage(msg);

            String toJson = new Gson().toJson(message);
            bufferedWriter.write(toJson);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(BroadcastServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processMessage(Message message) {
        if (chatListener == null) {
            return;
        }
        if (Command.JOIN.equals(message.getCommand())) {
            chatListener.onJoin(message.getFrom());
        } else if (Command.LEAVE.equals(message.getCommand())) {
            chatListener.onLeave(message.getFrom());
        } else {
            chatListener.onMessage(message.getFrom(), message.getMessage());
        }
    }

    public void listenForMessage() {
        new Thread(() -> {

            while (socket.isConnected()) {
                try {
                    String msg = bufferedReader.readLine();
                    final Message message = gson.fromJson(msg, Message.class);
                    processMessage(message);
                } catch (IOException ex) {
                    Logger.getLogger(BroadcastServerProxy.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }).start();
    }
}
