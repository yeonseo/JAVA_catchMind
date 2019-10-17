package Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	public Socket socket;
	
	public Client(Socket socket) {
		this.socket = socket;
		receive();
	}

	//클라이언로부터 메세지를 전달받는 메소드
	private void receive() {
		//thread로 바꿔보기 
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						int length = in.read(buffer);
						while(length == -1) throw new IOException();
						System.out.println("[succese in message] "
								 + socket.getRemoteSocketAddress()
								 + " : " + Thread.currentThread().getName());
						String message = new String(buffer, 0, length, "UTF-8");
						for(Client client : ServerMainController.clients) {
							 client.send(message);
						}//end of for
					} //end of while
				}catch(Exception e) {
					try {
						 System.out.println("[fail in message] "
								 + socket.getRemoteSocketAddress()
								 + " : " + Thread.currentThread().getName());
						 ServerMainController.clients.remove(Client.this);
						 socket.close();
					 } catch (Exception e2) {
						 e2.printStackTrace(); 
					 }
				 }//end of catch
			}//end of run()
		}; //end of thread
		ServerMainController.threadPool.submit(thread);
	}//end of receive()
	
	
	
	// 클라이언트에게 메세지를 전송하는 메소드
	public void send(String message) {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
					
				} catch (Exception e) {
					System.out.println("messge out fail"
							+ socket.getRemoteSocketAddress()
							+ " : " + Thread.currentThread().getName());
					ServerMainController.clients.remove(Client.this);
					e.printStackTrace();
				}
			}//end of run
		};//end of thread
		ServerMainController.threadPool.submit(thread);
	}//end of send()
}
