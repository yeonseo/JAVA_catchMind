package Controller;

import java.io.PrintWriter;
import java.util.Arrays;

import Application.sqlDB;

public class UserGameState {
	
	/* 여기서 유저들의 접속상태를 다룰 예정
	 * 
	 * */
	public static String managerEnter = "managerMainTab";
	public static String managerEnterUserGame;
	
	
	public static String gamerEnter = "gamerWaitRoom";
	public static String gamerGameWait = "gamerGameWaitRoom";
	public static String gamerGameStart = "gamerGameStartRoom";
	

}