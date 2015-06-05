/* Copyright 2004 Sun Microsystems, Inc. All  Rights Reserved.
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */
package javamazegame_final;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;
import java.util.*;
public class Client extends JPanel implements ClientCallBack{

 Client()
 {
      KeyListener listener = new MyKeyListener();
      addKeyListener(listener);
      setFocusable(true);

 }
    PositionTreasureInterface pTr;
    int noOfRows;
    int noOfColumns;
    boolean gameStart=false;
    boolean gameOver=false;
    Hello stub;
    String id;
    public void AnnounceGameOver(String id, int treasureCollected)
    {
        System.out.println("The game is over");
        System.out.println("The winner is"+ " "+id);
        System.out.println("The treasures collected by him is"+ treasureCollected);
    }
    public void ConnectServer()
    {
       try
       {
       Registry registry = LocateRegistry.getRegistry("localhost",1299);
       stub = (Hello) registry.lookup("Hello");
       ClientCallBack callBack=(ClientCallBack)UnicastRemoteObject.exportObject(this,0);
      TokenRemoteInterface response=(TokenRemoteInterface)stub.RequestJoin(callBack);
     // Tokens response=(Tokens)res;
      if(response.GetAcceptance()==true)
      {
          System.out.println("It is accepted");
          this.id=response.GetClientId();
          GameMazeInterface gameMaze=(GameMazeInterface)response.GetGameMaze();
          if(gameMaze==null)
              System.out.println("GameMaze is  null");
          this.noOfRows=gameMaze.GetNoOfRows();
          this.noOfColumns=gameMaze.GetNoOfColumns();
          System.out.println(this.noOfColumns);
          System.out.println(this.noOfRows);
          int Treasure[][]=gameMaze.GetTreasureMapping();
           HashMap m=response.GetGameMaze().GetClientTreasureLocationMapping();
          
          pTr=(PositionTreasureInterface)m.get(id);

          if(response.GetAcceptance()==true)
              gameStart=true;
          System.out.println("The tresure locations are");
          for(int i=0;i<noOfRows;i++)
          {
              for(int j=0;j<noOfColumns;j++)
              {
                   System.out.print(Treasure[i][j]+" ");
              }
              System.out.println(" ");
          }
          Iterator mit=m.entrySet().iterator();
          while (mit.hasNext())
          {

            Map.Entry pairs = (Map.Entry)mit.next();
            System.out.println(pairs.getKey());
            PositionTreasureInterface P=(PositionTreasureInterface)pairs.getValue();
            System.out.println("The position is"+P.GetX()+" "+P.GetY());
            //System.out.println("The ")
          }
       }
      else
      {
          System.out.println(response.Getmessage());

      }
        }
       catch (Exception e)
       {
	    System.err.println("Client exception: " + e.toString());
	    e.printStackTrace();
	}
    }
    public static void main(String[] args)
    {

	String host = (args.length < 1) ? null : args[0];
	    //for JPanel
           JFrame frame = new JFrame("Key event Receiver");
            Client c = new Client();
            c.ConnectServer();
            frame.add(c);
            frame.setSize(200, 200);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     }
    boolean MakeMoveOrPickTreasure(KeyEvent e)
    {
        boolean isPick=false;
        try
        {
        if(e.getKeyCode()==KeyEvent.VK_LEFT)
            {
            System.out.println("The key is left");
            if(pTr.GetX()==0)
               return false;
            }

            else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                System.out.println("Key is right");
                if(pTr.GetX()==noOfColumns-1)
                    return false;
            }
            else if(e.getKeyCode() == KeyEvent.VK_UP)
            {
                if(pTr.GetY()==0)
                    return false;
            }
            else if(e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                if(pTr.GetY()==noOfRows-1)
                    return false;
            }

            else
            {
                if(e.getKeyCode()!=10&&e.getKeyCode()!=16)
                {
                    System.out.println("Wrong Keys"+e.getKeyCode());
                    return false;
                }
                if(e.getKeyCode()==16)
                      isPick=true;
                else
                {
                    isPick=false;
                   System.out.println("The key is Enter");
                }
                System.out.println("Moving");
            }

            try
            {
               ResponseOnMoveInterface res=(ResponseOnMoveInterface)stub.MakeMove(id, e,isPick);
               res.GetGameDetails(id);
            }
            catch(Exception exception)
            {
                  System.out.println(exception.getStackTrace());

            }
        }
        catch(Exception ex)
        {
            return false;
        }
            return true;
        }


 public class MyKeyListener implements KeyListener
 {
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
           // System.out.println("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()));
            System.out.println("keyPressed="+ e.getKeyCode());
            if(gameStart==true&&gameOver==false)
                MakeMoveOrPickTreasure(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
            //System.out.println("keyReleased="+KeyEvent.getKeyText(e.getKeyCode()));
    }
}
}
