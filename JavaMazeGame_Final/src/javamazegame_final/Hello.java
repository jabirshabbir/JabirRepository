/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javamazegame_final;
import java.awt.event.*;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote {
    TokenRemoteInterface RequestJoin(ClientCallBack obj) throws RemoteException;
    ResponseOnMoveInterface MakeMove(String id,KeyEvent e,boolean isPick) throws RemoteException;
   // ResponseOnMove PickTreasure(String id,KeyEvent e,boolean isPick) throws RemoteException;
}

