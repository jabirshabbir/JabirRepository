/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javamazegame_final;
import java.rmi.*;
import java.util.*;
import java.awt.event.*;
/**
 *
 * @author Jabir
 */
public interface GameMazeInterface extends Remote
{
  boolean InitializeClientPosition(ArrayList<String> Clients)throws RemoteException;
  void TreasureSetUp()throws RemoteException;
  void MakeMove(String id,KeyEvent e)throws RemoteException;
  boolean PickTreasure(String clientId)throws RemoteException;
  int GetNoOfRows() throws RemoteException;
  int GetNoOfColumns() throws RemoteException;
  int [][] GetTreasureMapping()throws RemoteException;
  HashMap  GetClientTreasureLocationMapping() throws RemoteException;
}
