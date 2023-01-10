package QQ.serve;

import java.util.HashMap;

public class ManageServerThread {
    private static HashMap<String, ServerThread> h = new HashMap<>();

    public static HashMap<String, ServerThread> getH() {
        return h;
    }

    public static void addServerThread(String id, ServerThread serverThread){
        h.put(id, serverThread);
    }

    public static void moveServerThread(String id){
        h.remove(id);
    }

    public static ServerThread getServerThread(String id){
        return h.get(id);
    }

    public static String getOnlineUser(){
        String users = "";
        for (String i : h.keySet()){
            users += i + " ";
        }
        return users;
    }
}
