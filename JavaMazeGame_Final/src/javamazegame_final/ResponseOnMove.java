package javamazegame_final;
import java.util.*;
/**
 *
 * @author TOSHIBA
 */
public class ResponseOnMove implements ResponseOnMoveInterface
{
    int Treasure[][];
    static int M,N;
    String message;
    static HashMap ClientPosNTreasure;
    public ResponseOnMove(int[][] t ,String message,HashMap ClientPosNTreasure)
    {
        this.Treasure=t;
       // this.xloc = xloc;
       // this.yloc = yloc;
        this.message = message;
        //this.noOfTreasuresCollected = treasureCollected;
        this.ClientPosNTreasure=ClientPosNTreasure;

    }
    public void  GetGameDetails(String id)
    {
        PositionTreasureInterface PosTr=(PositionTreasureInterface)ClientPosNTreasure.get(id);
        try
        {
            System.out.println("Your X position is" +PosTr.GetX());
            System.out.println("Your Y position is"+ PosTr.GetY());
            System.out.println("The total treasure you collected"+ PosTr.GetTotalTreasureCollected());
            System.out.println("The details of remaining treasure in Maze");
            for(int i=0;i<M;i++)
            {
                for(int j=0;j<N;j++)
                {
                    System.out.print(Treasure[i][j]+" ");
                }
                System.out.println(" ");
            }
            System.out.println("The details of other Clients are");
         // HashMap m=response.GetGameMaze().GetClientTreasureLocationMapping();
          Iterator mit=ClientPosNTreasure.entrySet().iterator();
          while (mit.hasNext())
          {

            Map.Entry pairs = (Map.Entry)mit.next();
            System.out.println(pairs.getKey());
            PositionTreasureInterface P=(PositionTreasureInterface)pairs.getValue();
            System.out.println("The position is"+P.GetX()+" "+P.GetY());
            //System.out.println("The ")
          }
        }
       catch(Exception ex)
       {
           System.out.println("Exception in reading position..."+ex);    
       
       }
    
    }
}