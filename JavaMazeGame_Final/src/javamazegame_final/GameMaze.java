/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javamazegame_final;

/**
 *
 * @author Gopal
 */
import java.util.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class GameMaze implements GameMazeInterface
{
    /*static*/ int noOfRows;
    /*static*/ int noOfColumns;
    /*static*/ int noOfTreasures;
    /*static*/ int Treasure[][];
    static HashMap ClientPosNTreasure;
    boolean isGameOver;
    public int GetNoOfRows()
    {
        return noOfRows;

    }

    public int GetNoOfColumns()
    {
        return noOfColumns;

    }

    public int [][] GetTreasureMapping()
    {
        return Treasure;
    }

    public HashMap GetClientTreasureLocationMapping()
    {
        return ClientPosNTreasure;
    }
    GameMaze(int m,int n)
    {
        noOfRows=m;
        noOfColumns=n;
        Treasure=new int[m][n];
        ClientPosNTreasure=new HashMap();
    }


     public boolean InitializeClientPosition(ArrayList<String> Clients)
    {
        System.out.println("Initializing....");
        ArrayList<Integer> AvailablePositions=new ArrayList<Integer>();
        for (int i=0;i<noOfRows*noOfColumns;i++)
        {
            AvailablePositions.add(i);
        }
        for(int i=0;i<Clients.size();i++)
        {
            System.out.println("Initializing in loop");
            Double pos=new Double(Math.random()*100000);
            int t=(int)pos.floatValue();
            int PlayerPos=AvailablePositions.get(t%AvailablePositions.size());
            AvailablePositions.remove(t%AvailablePositions.size());
            PositionTreasure posTr=new PositionTreasure();
            posTr.y=((int)PlayerPos)/noOfColumns;
            posTr.x=((int)PlayerPos)%noOfColumns;
            posTr.treasureCollected=0;
            try
            {
                  PositionTreasureInterface PositionTr=(PositionTreasureInterface)UnicastRemoteObject.exportObject(posTr,0);
                  ClientPosNTreasure.put(Clients.get(i),(PositionTreasureInterface)posTr);
            }
            catch(Exception e)
            {

            }

        }
        System.out.println("End of initialization func");
        return true;
    }

    public void TreasureSetUp()
   {
       Double t=new Double(Math.random()*100000);
       int noOfTreasuresCells=1+t.intValue()%(noOfColumns*noOfRows-1);
       ArrayList<Integer> AvailablePositions=new ArrayList<Integer>();
       for (int i=0;i<noOfRows*noOfColumns;i++)
       {
            AvailablePositions.add(i);
       }


       while(noOfTreasuresCells!=0)
       {
           Double d=new Double(Math.random()*100000);
           int selectCell=d.intValue()%(AvailablePositions.size());
           int vary=AvailablePositions.get(selectCell)/noOfColumns;
           int varx=AvailablePositions.get(selectCell)%noOfColumns;
           int cellval=(new Double(Math.random()*10)).intValue();
               cellval=1+cellval%3;

                Treasure[vary][varx]=cellval;
                noOfTreasures=noOfTreasures+cellval;
            noOfTreasuresCells=noOfTreasuresCells-1;
           AvailablePositions.remove(selectCell);

       }
   }
    public void MakeMove(String id,KeyEvent e)
    {
        PositionTreasure Tr=(PositionTreasure)ClientPosNTreasure.get(id);
        if(e.getKeyCode()==e.VK_LEFT)
           Tr.x--;
        else if(e.getKeyCode()==e.VK_RIGHT)
                Tr.x++;
        else if(e.getKeyCode()==e.VK_UP)
               Tr.y--;
        else if(e.getKeyCode()==e.VK_DOWN)
             Tr.y++;

        try
        {
            PositionTreasureInterface PositionTr=(PositionTreasureInterface)Tr;
            ClientPosNTreasure.put(id,PositionTr);
        }
        catch(Exception ex)
        {
            System.out.println("The exception while move is"+" "+ex.getStackTrace());
        }
    }
   public boolean PickTreasure(String clientId)
   {
            synchronized(this)
            {
                System.out.println("picking");
                if(!isGameOver)
                {
                PositionTreasure posNTreasure = (PositionTreasure)ClientPosNTreasure.get(clientId);
                int noOfTreasuresInPlayerLocation = Treasure[posNTreasure.y][posNTreasure.x];
                if(noOfTreasuresInPlayerLocation >0)
                {
                    if(noOfTreasures-1==0)
                    {
                        isGameOver=true;

                    }
                    Treasure[posNTreasure.y][posNTreasure.x]-=1;
                    posNTreasure.treasureCollected+=1;
                    System.out.println(posNTreasure.treasureCollected);
                    try
                    {
                        PositionTreasureInterface PositionTr=(PositionTreasureInterface)posNTreasure;
                        ClientPosNTreasure.put(clientId, PositionTr);
                    }
                    catch(Exception ex)
                    {
                       System.out.println("The exceptionin picking" +ex.getStackTrace());
                    }
                    noOfTreasures--;
                    System.out.println(noOfTreasures);
                    return true;
                }

                }
               return false;
        }

    }
}


