/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javamazegame_final;
import java.rmi.*;
/**
 *
 * @author Jabir
 */
public interface PositionTreasureInterface extends Remote
{
   public int GetX() throws RemoteException;
   public int GetY()  throws RemoteException;
   public int GetTotalTreasureCollected() throws RemoteException;
}
