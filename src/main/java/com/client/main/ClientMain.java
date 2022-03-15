/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.main;

import com.client.core.BroadcastServerProxy;
import com.client.gui.ChatFrame;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author josip
 */
public class ClientMain {
    public static void main(String[] args) throws IOException {
        String username = JOptionPane.showInputDialog("Enter your name: ");
        
        Socket socket = new Socket("127.0.0.1", 9000);
        
        BroadcastServerProxy broadcastServerProxy = new BroadcastServerProxy(username, socket);
        
        broadcastServerProxy.join();
        ChatFrame frame = new ChatFrame(username, broadcastServerProxy);
        frame.setVisible(true);
    }
}
