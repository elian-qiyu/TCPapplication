package QQ.serve;

import java.util.HashMap;

public class ManageClientThread {
    //管理线程
    private static HashMap<String, ClientThread> hashMap = new HashMap<>();

    public static void addClientThread(String id, ClientThread clientThread){
        hashMap.put(id, clientThread);
    }

    public static ClientThread getClientThread(String id) {
        return hashMap.get(id);
    }
}
