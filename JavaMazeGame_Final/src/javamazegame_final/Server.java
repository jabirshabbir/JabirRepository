
/* Copyright 2004 Sun Microsystems, Inc. All  Rights Reserved.
 *
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

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.math.*;
import java.awt.event.*;


public class Server implements Hello
{
    public static long time;
    static long Max=1000000;
    static long Min=100000;
    int noOfClients=0;
    ArrayList<String> Clients;
    static GameMaze gameMaze;
    static GameMazeInterface gameMazeInterface;
    static boolean initializationPlayers;
    static int noOfClientsAssignedID;
    static HashMap ClientObj;
    static String Winner;

    public Server()
    {

        Clients=new  ArrayList <String>();
        ClientObj=new HashMap();
    }
    public  String GenerateClientID(ClientCallBack obj)
    {
        synchronized(this){
        System.out.println("Generating Client id");
        Long ID= new Long(Min + (long)(Math.random() * (( Min) + 1)));
        Min=Min+ID;
        ClientObj.put(ID.toString(), obj);
        System.out.println("returning ids...");
        return ID.toString();
        }
    }

    public static void AnnounceWinner()
    {
        System.out.println("aNNOUNCING WINNER");
        HashMap m=gameMaze.ClientPosNTreasure;
        Iterator mit=m.entrySet().iterator();
        int max_treasure=0;
        String Winner=null;
        while(mit.hasNext())
        {
            Map.Entry pairs = (Map.Entry)mit.next();
            PositionTreasure PTr=(PositionTreasure)pairs.getValue();
            if(PTr.treasureCollected>max_treasure)
            {
                max_treasure=PTr.treasureCollected;
                Winner=(String)pairs.getKey();
            }
        }
        Iterator it = ClientObj.entrySet().iterator();
        while (it.hasNext())
        {

            Map.Entry pairs = (Map.Entry)it.next();
            ClientCallBack c=(ClientCallBack)pairs.getValue();
            try
            {
            c.AnnounceGameOver(Winner, max_treasure);
            }
            catch(Exception ex)
            {
                System.out.println("The exception in announcing winner"+ ex.getStackTrace());

            }
        }
    }

    public static void InitializeGame(String row,String column)
    {
        gameMaze=new GameMaze(Integer.parseInt(row),Integer.parseInt(column));
        gameMaze.TreasureSetUp();
        for(int i=0;i<Integer.parseInt(row);i++)
        {
            for(int j=0;j<Integer.parseInt(column);j++)
            {
                System.out.print(gameMaze.Treasure[i][j]+" ");
            }
            System.out.println(" ");
        }
        System.out.println("Doneee");
    }


    void Blocker()
    {
        while(System.currentTimeMillis()-time<10000)
        {

        }
    }
     void Waiting_IdsAssigned()
    {
        while(noOfClients!=noOfClientsAssignedID||noOfClients==0)
        {

        }
         System.out.println("Finished waiting");
        initializationPlayers=gameMaze.InitializeClientPosition(Clients);
    }

    public TokenRemoteInterface RequestJoin(ClientCallBack obj)
    {
        if(obj==null)
              System.out.println("Sorr object null cannot call back");
        if(noOfClients==0)
      {
          synchronized(this)
          {
          noOfClients++;
          if(noOfClients==1)
              time=System.currentTimeMillis();

          }
          Blocker();

          String UniqueClientid=GenerateClientID(obj);
          //System.out.println(UniqueClientid);
          Clients.add(UniqueClientid);
          noOfClientsAssignedID++;
          
          while(!initializationPlayers)
          {

          }
          Tokens token=new Tokens(UniqueClientid,"Hello Welcome to Game",true);
          try
          {
              token.gameMaze=(GameMazeInterface)UnicastRemoteObject.exportObject(gameMaze,0);
          }
          catch(Exception e)
          {
              System.out.println("The game maze Exception is"+ e);
          }
          TokenRemoteInterface Response;
          System.out.println("Returning response....");
          try
          {
             Response =(TokenRemoteInterface)UnicastRemoteObject.exportObject(token,0);
             System.out.println(Response.GetAcceptance());
             if(Response==null)
                 System.out.println("It is null");
             return Response;
          }
          catch(Exception e)
          {
            System.out.println("The exception is "+e);
          }
          return null;
      }
      else
      {
          if(System.currentTimeMillis()-time>10000)
          {

              System.out.println( System.currentTimeMillis()-time);
              System.out.println("too late");
              Tokens token= new Tokens(null,"Sorry too late to join...",false);
              try
              {
               TokenRemoteInterface t=(TokenRemoteInterface)UnicastRemoteObject.exportObject(token,0);
                return t;
              }
              catch(Exception e)
              {
                  return null;
              }
          }
          else
          {
              noOfClients++;
              System.out.println("Blocking....");
              Blocker();
              String UniqueClientid=GenerateClientID(obj);
              Clients.add(UniqueClientid);
              noOfClientsAssignedID++;
              while(!initializationPlayers)
              {

              }
              System.out.println("initialization doone finally.....");
              Tokens token=new Tokens(UniqueClientid,"Hello Welcome to Game",true);
              try
              {

                  token.gameMaze=(GameMazeInterface)gameMaze;
              }
              catch(Exception e)
              {
                  System.out.println("The game maze Exception is"+ e);
              }
              TokenRemoteInterface Response;
              System.out.println("Returning response....");
              try
              {
                 Response =(TokenRemoteInterface)UnicastRemoteObject.exportObject(token,0);
                 System.out.println(Response.GetAcceptance());
                 if(Response==null)
                     System.out.println("It is null");
                 return Response;
              }
              catch(Exception e)
              {
                System.out.println("The exception is "+e);
                return null;
              }

              //Response.gameMaze=gameMaze;
              
          }

      }

    }
 static void Waiting_For_GameOver()
 {
     while(!gameMaze.isGameOver)
     {


     }

    AnnounceWinner();
 }
    public static void main(String args[])
    {
        Hello stub = null;
        Registry registry = null;


    try
    {
     Server obj = new Server();
     System.out.println("Starting...");
     stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);
     registry = LocateRegistry.createRegistry(1299);
     registry.bind("Hello", stub);
     System.out.println("Server ready");
     Integer a=new Integer(3);
     Integer b=new Integer(3);
     InitializeGame(a.toString(),b.toString());
     obj.Waiting_IdsAssigned();
     Waiting_For_GameOver();
     //gameMaze.InitializeClientPosition(Clients);
    }
    catch (Exception e)
    {
        try
        {
           registry.unbind("Hello");
           registry.bind("Hello",stub);
           System.err.println("Server ready");
        }
        catch(Exception ee)
        {
           System.err.println("Server exception: " + ee.toString());
           ee.printStackTrace();

        }
     }
    }
    public ResponseOnMoveInterface MakeMove(String id,KeyEvent e,boolean isPick)
    {

        System.out.println("Move function called");
        ResponseOnMove response;
        if(gameMaze.isGameOver)
        {
            //System.out.println("Game over");
             response=new ResponseOnMove(null,"Game over",null);
             return response;
        }
            if(!Clients.contains(id))
        {
             System.out.println("Client not found");
        }
        if(!isPick)
        {
            System.out.println("Moving ...mesage from Server");
            if(e.getKeyCode()!=10)
            {
               gameMaze.MakeMove(id,e);
               response=new ResponseOnMove(gameMaze.Treasure,"successfully moved",gameMaze.ClientPosNTreasure);
            }
            else
            {
                System.out.println("Printing your info");
                response=new ResponseOnMove(gameMaze.Treasure,"Your game information",gameMaze.ClientPosNTreasure);
            }
        }
       else
        {
            if(gameMaze.PickTreasure(id))
            {
             response=new ResponseOnMove(gameMaze.Treasure,"successfully picked",gameMaze.ClientPosNTreasure);
            }
            else
            {
                response=new ResponseOnMove(gameMaze.Treasure,"Treasure you were looking for is not available",gameMaze.ClientPosNTreasure);
            }
        }
        try
        {

        ResponseOnMoveInterface res=(ResponseOnMoveInterface)UnicastRemoteObject.exportObject(response,0);
        return res;
        }
        catch(Exception ex)
        {
            return null;
        }
       // ResponseOnMove response=new ResponseOnMove(gameMaze.Treasure,"successfully moved",gameMaze.ClientPosNTreasure);;
       // return response;
    }


}
