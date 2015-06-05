/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javamazegame_final;

/**
 *
 * @author Jabir
 */
import java.rmi.*;

public interface TokenRemoteInterface extends Remote
{
  boolean GetAcceptance()  throws RemoteException;
  String Getmessage()throws RemoteException;
  String GetClientId() throws RemoteException;
  GameMazeInterface GetGameMaze() throws RemoteException;

}
