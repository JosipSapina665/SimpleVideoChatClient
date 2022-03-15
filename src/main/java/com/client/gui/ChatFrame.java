/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.gui;

import com.client.core.BroadcastServerProxy;
import com.client.core.ChatListener;
import com.client.model.Message;
import java.awt.Button;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author josip
 */
public class ChatFrame extends JFrame{
    JTextArea textArea = new JTextArea();
    JTextField textField = new JTextField();
    JButton btnSend = new JButton("Send");
    
    BroadcastServerProxy broadcastServerProxy;
    
    public ChatFrame(String title, BroadcastServerProxy broadcastServerProxy) {
		setTitle(title);
		this.broadcastServerProxy = broadcastServerProxy;
		this.broadcastServerProxy.setChatListener(new ChatMessageListener());
		prepareFrame();
	}

	private void prepareFrame() {
		Container content = getContentPane();

		Box south = Box.createHorizontalBox();
		south.add(textField);
		south.add(btnSend);

		JScrollPane scrollPane = new JScrollPane(textArea);
		content.add(scrollPane, "Center");
		content.add(south, "South");

		setBounds(100, 100, 300, 300);
		setResizable(false);
		addWindowListener(new WindowHandler());
		btnSend.addActionListener(new SendHandler());

	}
	
	class WindowHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			broadcastServerProxy.leave();
			dispose();
		}
	}
	
	class SendHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			broadcastServerProxy.send(textField.getText());
		}
	}
	
	class ChatMessageListener implements ChatListener {

		public void onJoin(String username) {
			String message = username + " joined";
			String appendedText = textArea.getText() + "\n" + message;
			textArea.setText(appendedText);
		}

		public void onLeave(String username) {
			String message = username + " left";
			String appendedText = textArea.getText() + "\n" + message;
			textArea.setText(appendedText);
		}

		public void onMessage(String username, String message) {
			String strMessage = username + ":" + message;
			String appendedText = textArea.getText() + "\n" + strMessage;
			textArea.setText(appendedText);
		}
		
	}
}
