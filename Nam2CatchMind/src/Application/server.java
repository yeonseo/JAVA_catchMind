package Application;

import java.net.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.util.ArrayList;

public class server {
	private ServerSocket serverSocket;

	// constructor
	public server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	// thread manager
	public void acceptClients() throws IOException {
		// for keeping track of client number
		int clientNumber = 0;

		// create an open-ended thread pool
		ExecutorService threadPool = Executors.newCachedThreadPool();
		try {
			while (!Thread.currentThread().isInterrupted()) {
				// wait for client to connect
				log("Up and listening on port " + serverSocket.getLocalPort() + "...");
				Socket clientSocket = serverSocket.accept();
				log("Just connected to " + clientSocket.getRemoteSocketAddress());

				// create new client handler and fork to background thread
				threadPool.submit(new ClientHandler(clientSocket, clientNumber++));
			}
		} finally {
			// shut down thread pool when done
			threadPool.shutdown();
		}
	}

	// method for killing the server
	public void stop() throws IOException {
		serverSocket.close();
	}

	// this class handles each client connection
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private int clientNumber;

		// constructor
		public ClientHandler(Socket clientSocket, int clientNumber) {
			this.clientSocket = clientSocket;
			this.clientNumber = clientNumber;
			log("New connection with client #" + clientNumber + " at " + clientSocket);
		}

		// main brains
		public void run() {
			String clientResponse;

			try {
				// initialize input and output streams
				BufferedReader in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				// send greeting to client
				out.println("Welcome, you are client #" + clientNumber + ".");

				while (true) {
					// read and parse client response
					clientResponse = in.readLine();
					parseOpcode(clientResponse, out);
				}
			} catch (IOException e) {
				log("Error handling client #" + clientNumber + ": " + e);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					log("Error closing client connection");
				}
			}
			log("Connection with client #" + clientNumber + " has been terminated.");
		}

		// method for parsing opcode and passing parameters to correct handler
		public void parseOpcode(String clientResponse, PrintWriter out) throws Exception {
			Boolean authenticationFlag = false;
			String[] params = clientResponse.split(",");
			int opcode = Integer.parseInt((params[0]));

			switch (opcode) {
			// login
			case (0):
				try {
					// parse remaining params
					String username = params[1];
					String password = params[2];

					if (authenticate(username, password)) {
						// set online field to 1
						setOnline(username);
						authenticationFlag = true;
						out.println(authenticationFlag.toString());
						out.flush();
						out.println("Authentication succeeded, welcome " + username + "!");
						out.flush();
						out.println(sqlDB.getNameDB(username));
						out.flush();
					} else {
						log("Incorrect username or password.");
						out.println(authenticationFlag.toString());
						out.flush();
						out.println("Incorrect username or password.");
						out.flush();
						out.println("");
						out.flush();
					}
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// sign-up
			case (1):
				try {
					// parse remaining params
					String username = params[1];
					String password = params[4];
					String name = params[2];
					String surname = params[3];

					Boolean addFlag = false;

					if(addNewUser(name, surname, username, password))
					{
						addFlag = true;
						out.println(addFlag.toString());
						out.flush();
						out.println("User " + username + " added successfully!");
						out.flush();
					}
					else
					{
						addFlag = false;
						out.println(addFlag.toString());
						out.flush();
						out.println("Signup failed!");
						out.flush();
					}
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// change password
			case (2):
				try {
					// parse remaining params
					String username = params[1];
					String newpassword = params[2];

					if(changePass(username, newpassword))
					{
						out.println("Password changed successfully!");
						out.flush();
					}
					else
					{
						out.println("Password change Failed");
						out.flush();
					}
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// get messages sent to a particular user
			case (3):
				try {
					// parse remaining params
					String username = params[1];
					String chatID = params[2];
					String sender = params[3];

					ArrayList<ArrayList<String>> results = sqlDB.getChatUserDB(username, Integer.parseInt(chatID), sender);
					String msgs = "";

					// check if empty
					if (results.size() == 0) {
						log("sql returned no msgs!!!!");
						out.println(msgs);
						out.flush();
					} else {
						for (int i = 0; i < results.size(); i++) {
							msgs += ("#" + results.get(i).get(2) + "."  +results.get(i).get(0) + ": " + results.get(i).get(1));
						}
						out.println(msgs.substring(1));
						out.flush();
					}
					Arrays.fill(params, null);
					break;
				} catch(Exception e) {
					e.printStackTrace();
				}
			// send a chat message
			case (4):
				try {
					// parse remaining params
					String sender = params[1];
					String receiver = params[2];
					String message = params[3];

					sqlDB.sendChatDB(sender, receiver, message);

					out.println("Chat sent!");
					out.flush();

					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// get list of all registered users
			case (5):
				try {
					// parse remaining params
					String username = params[1];
					String[] users;
					
					users = sqlDB.getUsersDB(username);

					String msg = "";
					
					if (users.length > 0) {
						log("There is an online user for: " + username);
						for (int i = 0; i < users.length; i++) {
							msg += "#" + users[i];
						}
						out.println(msg.substring(1));
					} else {
						// empty case
						out.println(msg);
					}

					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// logout
			case (6):
				try {
					String username = params[1];

					// log the user out
					setOffline(username);
					
					// flush gameRequests
					sqlDB.deleteGameRequests(username);
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e){
					e.printStackTrace();
				}
			// global chat
			case(7):
				try {

					ArrayList<ArrayList<String>> results = sqlDB.getChatAllDB();
					String msgs = "";

					// check if empty
					if (results.size() == 0) {
						log("sql returned no msgs!!!!");
						out.println(msgs);
						out.flush();
					} else {
						for (int i = 0; i < results.size(); i++) {
							msgs += ("#" + results.get(i).get(0) + "."  +results.get(i).get(0) + ": " + results.get(i).get(1));
						}
						out.println(msgs.substring(1));
						out.flush();
					}
					Arrays.fill(params, null);
					break;
				} catch(Exception e) {
					e.printStackTrace();
				}
			// get users available to add as a friend
			case (8):
				try {
					// parse remaining params
					String username = params[1];
					String[] users;
					
					users = sqlDB.getUsersFriendsDB(username);

					String msg = "";
					
					if (users.length > 0) {
						for (int i = 0; i < users.length; i++) {
							msg += "#" + users[i];
						}
						out.println(msg.substring(1));
					} else {
						// empty case
						out.println(msg);
					}

					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// add friend
			case (9):
				try {
					// parse remaining params
					String user1 = params[1];
					String user2 = params[2];

					sqlDB.addFriendDB(user1,user2);

					out.println("Friendship successful!");
					out.flush();

					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// get friends of a user
			case (10):
				try {
					// parse remaining params
					String username = params[1];
					String[] users;
					
					users = sqlDB.getFriendsDB(username);

					String msg = "";
					
					if (users.length > 0) {
						for (int i = 0; i < users.length; i++) {
							msg += "#" + users[i];
						}
						out.println(msg.substring(1));
					} else {
						// empty case
						out.println(msg);
					}

					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			case (11):
				try {
					// parse remaining params
					String user1 = params[1];
					String user2 = params[2];

					sqlDB.removeFriendDB(user1,user2);

					out.println("Friendship deleted!");
					out.flush();

					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			case(12):
				try {
					String[] games;
					games = sqlDB.getAvailableGames();

					String msg = "";
					
					if (games.length > 0) {
						log("There are available games!");
						for (int i = 0; i < games.length; i++) {
							msg += "#" + games[i];
						}
						out.println(msg.substring(1));
					} else {
						// empty case
						out.println(msg);
					}

					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// send game request
			case(13):
				try {
					// parse remaining params
					String player = params[1];
					String game = params[2];
					String againstWho = params[3];
					
					String output =  sqlDB.sendGameRequest(player, game, againstWho);
					
					out.println(output);
					out.flush();
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					out.println("error");
					break;
					//e.printStackTrace();
				}
			// get game requests from server
			case(14):
				try {
					// parse remaining params
					String player = params[1];
					
					ArrayList<ArrayList<String>> results = sqlDB.getGameRequests(player);
					String msgs = "";

					// check if empty
					if (results.size() == 0) {
						log("SQL returned no game requests!");
						out.println(msgs);
						out.flush();
					} else {
						for (int i = 0; i < results.size(); i++) {
							msgs += ("#" + results.get(i).get(0) + " - " + results.get(i).get(1));
						}
						log(msgs);
						out.println(msgs.substring(1));
						out.flush();
					}

					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// get online friends of a user
			case (15):
				try {
					// parse remaining params
					String username = params[1];
					String[] users;
					
					users = sqlDB.getOnlineFriendsDB(username);

					String msg = "";
					
					if (users.length > 0) {
						for (int i = 0; i < users.length; i++) {
							msg += "#" + users[i];
						}
						out.println(msg.substring(1));
					} else {
						// empty case
						out.println(msg);
					}

					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// decline a game request
			case (16):
				try {
					// parse remaining params
					String toPlayer = params[1];
					String fromPlayer = params[2];
					
					sqlDB.declineGameRequest(toPlayer, fromPlayer);
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// accept a game request
			case (17):
				try {
					// parse remaining params
					String toPlayer = params[1];
					String fromPlayer = params[2];
					
					sqlDB.acceptGameRequest(toPlayer, fromPlayer);
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// grab accepted games for a user
			case (18):
				try {
					// parse remaining params
					String user = params[1];
					String result;
					
					result = sqlDB.findAcceptedRequests(user);
					
					// respond to client
					out.println(result);
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// grab moves from opponent
			case (19):
				try {
					// parse remaining params
					String opponent = params[1];
					String result;
					
					result = sqlDB.findMovesOpponent(opponent);
					
					// respond to client
					out.println(result);
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			//put move into DB
			case (20):
				try {
					// parse remaining params
					String user = params[1];
					String coordinate = params[2];
					
					sqlDB.putMove(user,coordinate);
					

					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// increase score of user
			case (21):
				try {
					// parse remaining params
					String user = params[1];
					String game = params[2];
					
					sqlDB.increaseScore(user, game);
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// get scores
			case (22):
				try {
					// parse remaining params
					String game = params[1];
					
					String [] users = sqlDB.getScores(game);
					
					String msg = "";
					
					if (users.length > 0) {
						for (int i = 0; i < users.length; i++) {
							msg += "#" + users[i];
						}
						out.println(msg.substring(1));
					} else {
						// empty case
						out.println(msg);
					}
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// save to history
			case (23):
				try {
					// parse remaining params
					String loser = params[1];
					String winner = params[2];
					String game = params[3];
					
					sqlDB.saveToHistory(loser, winner, game);
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			// get hisotry of games of particular user
			case (24):
				try {
					// parse remaining params
					String user = params[1];
					
					String [] users = sqlDB.getHistory(user);
					
					String msg = "";
					
					if (users.length > 0) {
						for (int i = 0; i < users.length; i++) {
							msg += "#" + users[i];
						}
						out.println(msg.substring(1));
					} else {
						// empty case
						out.println(msg);
					}
					
					Arrays.fill(params, null);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			default:
				break;
			}
		}
	}

	// method for authenticating client
	public static Boolean authenticate(String username, String pass1) throws Exception {
		if (MD5(pass1).equals(sqlDB.getPassDB(username))) {
			return true;
		}
		return false;
	}

	// method for setting online field of authenticated user to 1
	public static void setOnline(String username) throws Exception {
		sqlDB.setOnline(username, true);
	}

	// method for setting online field of authenticated user to offline
	public static void setOffline(String username) throws Exception {
		sqlDB.setOnline(username, false);
	}

	// method for signing up a new user
	public static Boolean addNewUser(String name, String surname, String username, String password) throws ClassNotFoundException, SQLException {
		return sqlDB.addUserDB(username, name, surname, password);
	}

	public static Boolean changePass(String username, String pass) throws ClassNotFoundException, SQLException {
		return sqlDB.changePassDB(username, pass);
	}

	// for logging to server console
	private static void log(String message) {
		System.out.println(message);
	}

	// Create MD5 hash for password authentication
	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	// parse command line port number and start server
	public static void main(String [] args) {
		int port = Integer.parseInt(args[0]);
		try {
			new server(port).acceptClients();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}