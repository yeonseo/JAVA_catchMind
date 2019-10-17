package Controller;

import java.io.PrintWriter;
import java.util.Arrays;

import Application.sqlDB;

public class UserGameState {
	
	/* 여기서 유저들의 접속상태를 다룰 예정
	 * 
	 * */
	public static String managerEnter = "managerMainTab";
	public static String managerMemberShip = "managerMemberShip";


	// method for parsing opcode and passing parameters to correct handler
	public void parseOpcode(String clientResponse, PrintWriter out) throws Exception {
		Boolean authenticationFlag = false;
//		String[] params = clientResponse.split(",");
//		int opcode = Integer.parseInt((params[0]));
		int opcode = 0;
		switch (opcode) {
		// login
		case (0):
			try {
				// parse remaining params
				String username = "root";
				String password = "1234";
//				String username = params[1];
//				String password = params[2];

				if (username.equals("root")) {
					// set online field to 1
					authenticationFlag = true;
					out.println(authenticationFlag.toString());
					out.flush();
					out.println("Authentication succeeded, welcome " + username + "!");
					out.flush();
					out.println(sqlDB.getNameDB(username));
					out.flush();
				} else {
					AlertDisplay.log("Incorrect username or password.");
					out.println(authenticationFlag.toString());
					out.flush();
					out.println("Incorrect username or password.");
					out.flush();
					out.println("");
					out.flush();
				}
//				Arrays.fill(params, null);
				break;
//				// parse remaining params
//				String username = params[1];
//				String password = params[2];
//
//				if (authenticate(username, password)) {
//					// set online field to 1
//					setOnline(username);
//					authenticationFlag = true;
//					out.println(authenticationFlag.toString());
//					out.flush();
//					out.println("Authentication succeeded, welcome " + username + "!");
//					out.flush();
//					out.println(sqlDB.getNameDB(username));
//					out.flush();
//				} else {
//					log("Incorrect username or password.");
//					out.println(authenticationFlag.toString());
//					out.flush();
//					out.println("Incorrect username or password.");
//					out.flush();
//					out.println("");
//					out.flush();
//				}
//				Arrays.fill(params, null);
//				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
//		// sign-up
//		case (1):
//			try {
//				// parse remaining params
//				String username = params[1];
//				String password = params[4];
//				String name = params[2];
//				String surname = params[3];
//
//				Boolean addFlag = false;
//
//				if(addNewUser(name, surname, username, password))
//				{
//					addFlag = true;
//					out.println(addFlag.toString());
//					out.flush();
//					out.println("User " + username + " added successfully!");
//					out.flush();
//				}
//				else
//				{
//					addFlag = false;
//					out.println(addFlag.toString());
//					out.flush();
//					out.println("Signup failed!");
//					out.flush();
//				}
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// change password
//		case (2):
//			try {
//				// parse remaining params
//				String username = params[1];
//				String newpassword = params[2];
//
//				if(changePass(username, newpassword))
//				{
//					out.println("Password changed successfully!");
//					out.flush();
//				}
//				else
//				{
//					out.println("Password change Failed");
//					out.flush();
//				}
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// get messages sent to a particular user
//		case (3):
//			try {
//				// parse remaining params
//				String username = params[1];
//				String chatID = params[2];
//				String sender = params[3];
//
//				ArrayList<ArrayList<String>> results = sqlDB.getChatUserDB(username, Integer.parseInt(chatID), sender);
//				String msgs = "";
//
//				// check if empty
//				if (results.size() == 0) {
//					log("sql returned no msgs!!!!");
//					out.println(msgs);
//					out.flush();
//				} else {
//					for (int i = 0; i < results.size(); i++) {
//						msgs += ("#" + results.get(i).get(2) + "."  +results.get(i).get(0) + ": " + results.get(i).get(1));
//					}
//					out.println(msgs.substring(1));
//					out.flush();
//				}
//				Arrays.fill(params, null);
//				break;
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		// send a chat message
//		case (4):
//			try {
//				// parse remaining params
//				String sender = params[1];
//				String receiver = params[2];
//				String message = params[3];
//
//				sqlDB.sendChatDB(sender, receiver, message);
//
//				out.println("Chat sent!");
//				out.flush();
//
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// get list of all registered users
//		case (5):
//			try {
//				// parse remaining params
//				String username = params[1];
//				String[] users;
//				
//				users = sqlDB.getUsersDB(username);
//
//				String msg = "";
//				
//				if (users.length > 0) {
//					log("There is an online user for: " + username);
//					for (int i = 0; i < users.length; i++) {
//						msg += "#" + users[i];
//					}
//					out.println(msg.substring(1));
//				} else {
//					// empty case
//					out.println(msg);
//				}
//
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// logout
//		case (6):
//			try {
//				String username = params[1];
//
//				// log the user out
//				setOffline(username);
//				
//				// flush gameRequests
//				sqlDB.deleteGameRequests(username);
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e){
//				e.printStackTrace();
//			}
//		// global chat
//		case(7):
//			try {
//
//				ArrayList<ArrayList<String>> results = sqlDB.getChatAllDB();
//				String msgs = "";
//
//				// check if empty
//				if (results.size() == 0) {
//					log("sql returned no msgs!!!!");
//					out.println(msgs);
//					out.flush();
//				} else {
//					for (int i = 0; i < results.size(); i++) {
//						msgs += ("#" + results.get(i).get(0) + "."  +results.get(i).get(0) + ": " + results.get(i).get(1));
//					}
//					out.println(msgs.substring(1));
//					out.flush();
//				}
//				Arrays.fill(params, null);
//				break;
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		// get users available to add as a friend
//		case (8):
//			try {
//				// parse remaining params
//				String username = params[1];
//				String[] users;
//				
//				users = sqlDB.getUsersFriendsDB(username);
//
//				String msg = "";
//				
//				if (users.length > 0) {
//					for (int i = 0; i < users.length; i++) {
//						msg += "#" + users[i];
//					}
//					out.println(msg.substring(1));
//				} else {
//					// empty case
//					out.println(msg);
//				}
//
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// add friend
//		case (9):
//			try {
//				// parse remaining params
//				String user1 = params[1];
//				String user2 = params[2];
//
//				sqlDB.addFriendDB(user1,user2);
//
//				out.println("Friendship successful!");
//				out.flush();
//
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// get friends of a user
//		case (10):
//			try {
//				// parse remaining params
//				String username = params[1];
//				String[] users;
//				
//				users = sqlDB.getFriendsDB(username);
//
//				String msg = "";
//				
//				if (users.length > 0) {
//					for (int i = 0; i < users.length; i++) {
//						msg += "#" + users[i];
//					}
//					out.println(msg.substring(1));
//				} else {
//					// empty case
//					out.println(msg);
//				}
//
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		case (11):
//			try {
//				// parse remaining params
//				String user1 = params[1];
//				String user2 = params[2];
//
//				sqlDB.removeFriendDB(user1,user2);
//
//				out.println("Friendship deleted!");
//				out.flush();
//
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		case(12):
//			try {
//				String[] games;
//				games = sqlDB.getAvailableGames();
//
//				String msg = "";
//				
//				if (games.length > 0) {
//					log("There are available games!");
//					for (int i = 0; i < games.length; i++) {
//						msg += "#" + games[i];
//					}
//					out.println(msg.substring(1));
//				} else {
//					// empty case
//					out.println(msg);
//				}
//
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// send game request
//		case(13):
//			try {
//				// parse remaining params
//				String player = params[1];
//				String game = params[2];
//				String againstWho = params[3];
//				
//				String output =  sqlDB.sendGameRequest(player, game, againstWho);
//				
//				out.println(output);
//				out.flush();
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				out.println("error");
//				break;
//				//e.printStackTrace();
//			}
//		// get game requests from server
//		case(14):
//			try {
//				// parse remaining params
//				String player = params[1];
//				
//				ArrayList<ArrayList<String>> results = sqlDB.getGameRequests(player);
//				String msgs = "";
//
//				// check if empty
//				if (results.size() == 0) {
//					log("SQL returned no game requests!");
//					out.println(msgs);
//					out.flush();
//				} else {
//					for (int i = 0; i < results.size(); i++) {
//						msgs += ("#" + results.get(i).get(0) + " - " + results.get(i).get(1));
//					}
//					log(msgs);
//					out.println(msgs.substring(1));
//					out.flush();
//				}
//
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// get online friends of a user
//		case (15):
//			try {
//				// parse remaining params
//				String username = params[1];
//				String[] users;
//				
//				users = sqlDB.getOnlineFriendsDB(username);
//
//				String msg = "";
//				
//				if (users.length > 0) {
//					for (int i = 0; i < users.length; i++) {
//						msg += "#" + users[i];
//					}
//					out.println(msg.substring(1));
//				} else {
//					// empty case
//					out.println(msg);
//				}
//
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// decline a game request
//		case (16):
//			try {
//				// parse remaining params
//				String toPlayer = params[1];
//				String fromPlayer = params[2];
//				
//				sqlDB.declineGameRequest(toPlayer, fromPlayer);
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// accept a game request
//		case (17):
//			try {
//				// parse remaining params
//				String toPlayer = params[1];
//				String fromPlayer = params[2];
//				
//				sqlDB.acceptGameRequest(toPlayer, fromPlayer);
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// grab accepted games for a user
//		case (18):
//			try {
//				// parse remaining params
//				String user = params[1];
//				String result;
//				
//				result = sqlDB.findAcceptedRequests(user);
//				
//				// respond to client
//				out.println(result);
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// grab moves from opponent
//		case (19):
//			try {
//				// parse remaining params
//				String opponent = params[1];
//				String result;
//				
//				result = sqlDB.findMovesOpponent(opponent);
//				
//				// respond to client
//				out.println(result);
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		//put move into DB
//		case (20):
//			try {
//				// parse remaining params
//				String user = params[1];
//				String coordinate = params[2];
//				
//				sqlDB.putMove(user,coordinate);
//				
//
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// increase score of user
//		case (21):
//			try {
//				// parse remaining params
//				String user = params[1];
//				String game = params[2];
//				
//				sqlDB.increaseScore(user, game);
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// get scores
//		case (22):
//			try {
//				// parse remaining params
//				String game = params[1];
//				
//				String [] users = sqlDB.getScores(game);
//				
//				String msg = "";
//				
//				if (users.length > 0) {
//					for (int i = 0; i < users.length; i++) {
//						msg += "#" + users[i];
//					}
//					out.println(msg.substring(1));
//				} else {
//					// empty case
//					out.println(msg);
//				}
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// save to history
//		case (23):
//			try {
//				// parse remaining params
//				String loser = params[1];
//				String winner = params[2];
//				String game = params[3];
//				
//				sqlDB.saveToHistory(loser, winner, game);
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		// get hisotry of games of particular user
//		case (24):
//			try {
//				// parse remaining params
//				String user = params[1];
//				
//				String [] users = sqlDB.getHistory(user);
//				
//				String msg = "";
//				
//				if (users.length > 0) {
//					for (int i = 0; i < users.length; i++) {
//						msg += "#" + users[i];
//					}
//					out.println(msg.substring(1));
//				} else {
//					// empty case
//					out.println(msg);
//				}
//				
//				Arrays.fill(params, null);
//				break;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		default:
			break;
		}
	}
}
