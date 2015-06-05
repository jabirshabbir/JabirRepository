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
public interface ResponseOnMoveInterface extends Remote
{
   void  GetGameDetails(String id) throws RemoteException;
}
