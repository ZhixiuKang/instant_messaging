package chatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StartServer {
	public static ServerSocket filess = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket ss = null;
		UserinfoDAO.setUserlist();
		try {
			ss = new ServerSocket(4001);
			filess = new ServerSocket(5001);
			while (true) {
				Socket s = ss.accept();
				UserThread userThread = new UserThread(s);
				userThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
