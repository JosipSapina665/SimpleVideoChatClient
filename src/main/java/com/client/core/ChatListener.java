/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.core;

import com.client.model.Message;

/**
 *
 * @author josip
 */
public interface ChatListener {
    void onJoin(String username);
    void onLeave(String username);
    void onMessage(String username, String message);
}
