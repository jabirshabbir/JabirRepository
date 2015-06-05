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
public class PositionTreasure implements PositionTreasureInterface
{
    public
    int x;
    int y;
    int treasureCollected;
    PositionTreasure()
    {

    }
    public int GetX()
    {
        return x;
    }

    public int GetY()
    {
        return y;
    }
    public int GetTotalTreasureCollected()
    {
      return treasureCollected;
    }
    
}
