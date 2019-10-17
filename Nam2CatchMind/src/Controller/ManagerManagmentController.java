package Controller;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ManagerManagmentController implements Initializable {
	@FXML private TextArea txtAreaServerLog;
	@FXML private Button btnServerAction;

	
	public static ExecutorService threadPool;
	public static Vector<Client> clients = new Vector<Client>();
	ServerSocket serverSocket;

	String IP = "localhost";
	int port = 9876;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnServerAction.setText("start");
		btnServerAction.setOnAction(event ->{
			if(btnServerAction.getText().equals("start")) {
				startServer(IP, port);
				Platform.runLater(() ->{
					String message = String.format("server start \n",IP, port);
					txtAreaServerLog.appendText(message);
					btnServerAction.setText("exit");
				});
			}else {
				stopServer();
				Platform.runLater(() ->{
					String message = String.format("server exit \n",IP, port);
					txtAreaServerLog.appendText(message);
					btnServerAction.setText("start");
				});//end of Platform.runLater
			}//end of if/else
		});//end of btnServerAction

	}
	
	// Client submit threadPool(ExecutorService)
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));

		} catch (Exception e) {
			e.printStackTrace();
			if (!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		} // end of try/catch

		Runnable thread = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						clients.add(new Client(socket));
						System.out.println("[acsses client] " + socket.getRemoteSocketAddress() + " : "
								+ Thread.currentThread().getName());
					} catch (Exception e) {
						e.printStackTrace();
						if (!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					} // end of if/else
				} // end of while
			}// end of run()
		};// end of thread
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}// end of startServer

	public void stopServer() {
		try {
			Iterator<Client> itr = clients.iterator();
			while (itr.hasNext()) {
				Client client = itr.next();
				client.socket.close();
				itr.remove();
			}
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			if (threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {

		}
	}

}
