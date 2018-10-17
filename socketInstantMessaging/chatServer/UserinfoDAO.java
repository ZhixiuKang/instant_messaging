package chatServer;

import java.util.*;

public class UserinfoDAO {
	private static ArrayList<Userinfo> userlist = new ArrayList<Userinfo>();

	public UserinfoDAO() {

	}

	public static ArrayList<Userinfo> getUserlist() {
		return userlist;
	}

	public static void setUserlist(){
		userlist.add(new Userinfo("Tom", "Tom"));
		userlist.add(new Userinfo("Jack", "Jack"));
		userlist.add(new Userinfo("Lucy", "Lucy"));
		userlist.add(new Userinfo("Lily", "Lily"));
	}

	public static boolean validateUser(String name, String pass) {
		for (Userinfo userinfo : userlist) {
			if (name.equals(userinfo.getName())) {
				if (pass.equals(userinfo.getPass())) {
					if (userinfo.isOnline() == false) {
						userinfo.setOnline(true);
						return true;
					}

				}
			}
		}
		return false;

	}

	public static boolean getonline(String name) {
		for (Userinfo userinfo : userlist) {
			if (name.equals(userinfo.getName())) {
				if (userinfo.isOnline()) {
					return true;
				}
			}
		}
		return false;

	}

	public String[] users() {
		String[] list = new String[userlist.size()];
		for (int i = 0; i < userlist.size(); i++) {
			list[i] = userlist.get(i).getName();
		}
		return list;
	}

}
