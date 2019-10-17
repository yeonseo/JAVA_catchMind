package application.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {
public static void main(String[] args) throws Exception{
		
		System.out.println("Run Server");
		int clientId = 0;
		ServerSocket serverSocket = new ServerSocket(9111);
		
		try {
			while(true) {
				clientId++;
				Translator t = new Translator(serverSocket.accept(), clientId);
				t.start();
			}
		}finally {
			serverSocket.close();
		}
	}
	
	public static class Translator extends Thread {
		private Socket socket;
		private int myId;
		
		public Translator(Socket socket, int myId) {
			this.socket = socket;
			this.myId = myId;
		}
		
		@Override
		public void run() {
			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
				
				printWriter.println("HI Client number is " + myId);
				printWriter.println("input word !");
				
				while(true) {
					String inputWord = bufferedReader.readLine();
					if(inputWord == null) {
						break;
					}
					
					if(inputWord.equals("java") == true) {
						printWriter.println("java -> 자바");
						break;
						
					} else {
						printWriter.println("Not Found Word");
						break;
					}
				}
				
			} catch (IOException e) {
				System.out.println("Error ... Client : " + myId);
				e.printStackTrace();
			}finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("socket close Error");
					e.printStackTrace();
				}
				
				System.out.println("END !!!! Client : " + myId);
			}
		}
		
	}

}
