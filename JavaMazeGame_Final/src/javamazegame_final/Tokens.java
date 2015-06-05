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
public class Tokens implements TokenRemoteInterface
{
    String ClientId;
    String message;
    boolean acceptance;
    GameMazeInterface gameMaze;


    Tokens(String c,String m,boolean a)
    {
       ClientId=c;
       message=m;
       acceptance=a;

    }

    public boolean GetAcceptance(){
        return acceptance;
    }
    public String Getmessage()
    {
        return message;
    }

    public String GetClientId()
    {
        return ClientId;

    }
    public GameMazeInterface GetGameMaze()
    {
        return gameMaze;
    }
    /*void  SetLocation(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    int GetXLocation()
    {
        return this.x;
    }

    int GetYLocation()
    {
       return this.y;
    }*/
  }


