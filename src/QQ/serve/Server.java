package QQ.serve;

import QQ.common.Message;
import QQ.common.MessageType;
import QQ.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private ServerSocket serverSocket;
    private static HashMap<String, User> users = new HashMap<>();

    static {//静态代码块里初始话，一次
        users.put("100", new User("100", "123456"));
        users.put("200", new User("200", "123456"));
        users.put("300", new User("300", "123456"));
        users.put("400", new User("400", "123456"));
        users.put("500", new User("500", "123456"));
    }

    private boolean checkUser(String id, String pass){
        User user = users.get(id);
        if (user == null){
            return false;
        }
        if (!user.getPass().equals(pass)){
            return false;
        }
        return true;
    }

    public Server() {
        try {
            System.out.println("服务端在8888端口监听···");

            new Thread(new SendNews()).start();
            serverSocket = new ServerSocket(8888);
            while (true){//持续监听
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User u =(User) ois.readObject();

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                Message message = new Message();
                if (checkUser(u.getId(), u.getPass())){

                    message.setType(MessageType.LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //创建一个线程
                    ServerThread serverThread = new ServerThread(socket, u.getId());
                    serverThread.start();
                    //加入集合
                    ManageServerThread.addServerThread(u.getId(), serverThread);

                }else {//登录失败
                    message.setType(MessageType.LOGIN_DEFEAT);
                    oos.writeObject(message);
                    socket.close();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //退出监听
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
