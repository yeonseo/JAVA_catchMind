package Controller;

import java.io.PrintWriter;
import java.util.Arrays;

import Application.ref_sqlDB;

public class UserGameState {
	/*
	 * 관리자의 상태
	 * */
	
	public static final String MANAGER_ONLINE = "MANAGER_ONLINE";
	public static final String MANAGER_OFFLINE= "MANAGER_OFFLINE";
	public static String MANAGER_EnterUserGame;
	
	
	/* 유저들의 접속상태
	 * 
	 * */
	public static final String GAMER_WAITROOM = "GAMER_WAITROOM";
	public static final String GAMER_GAMEROOM_ENTER_AND_WAIT = "GAMER_GAMEROOM_ENTER_AND_WAIT";
	public static final String GAMER_GAMEROOM_ENTER_AND_START = "GAMER_GAMEROOM_ENTER_AND_START";
	public static final String GAMER_GAMEROOM_OUT_AND_WAITROOM = "GAMER_GAMEROOM_OUT_AND_WAITROOM";
	public static final String GAMER_OFFLINE = "GAMER_OFFLINE";
	
	/*
	 * IP 지정
	 * 
	 * */
//	public static final String IP = "localhost";
//	public static final String IP = "192.168.0.210";
	public static final String IP = "192.168.0.209";
	
	//<a href="https://www.freepik.com/free-photos-vectors/background">Background vector created by rawpixel.com - www.freepik.com</a>

}