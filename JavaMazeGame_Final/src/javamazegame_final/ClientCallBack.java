/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javamazegame_final;

/**
 *
 * @author Jabir
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface ClientCallBack extends Remote
{
  void AnnounceGameOver(String id,int treasureCollected) throws RemoteException;
}
