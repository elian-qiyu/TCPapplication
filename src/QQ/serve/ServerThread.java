package QQ.serve;

import QQ.common.Message;
import QQ.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread{

    private Socket socket;

    private String id;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public ServerThread(Socket socket, String id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {

        while (true){
            //System.out.println("服务器等待客户端的数据");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Message message =(Message) ois.readObject();

                if (message.getType().equals(MessageType.GET_ONLINE)){
                    //这句话为什么不能放 if 外面？？？？
                    //如果放在外面 则客户端获取的则是这个输出流 与私聊消息的输出流不符
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                    String onlineUser = ManageServerThread.getOnlineUser();
                    Message message1 = new Message();
                    message1.setType(MessageType.RETURN_ONLINE);
                    message1.setContent(onlineUser);
                    message1.setReceiver(message.getSender());
                    //返回给客户端
                    oos.writeObject(message1);

                } else if (message.getType().equals(MessageType.CLIENT_EXIT)) {

                    System.out.println(message.getSender() + "要退出系统");
                    ManageServerThread.moveServerThread(message.getSender());
                    socket.close();
                    break;//退出循环

                } else if (message.getType().equals(MessageType.COMMON_MES)) {
                    ObjectOutputStream oos1 = new ObjectOutputStream(ManageServerThread.getServerThread(message.getReceiver()).socket.getOutputStream());
                    oos1.writeObject(message);
                } else if (message.getType().equals(MessageType.TOALL_MES)) {
                    HashMap<String, ServerThread> h = ManageServerThread.getH();
                    for (String s : h.keySet()){
                        if (!s.equals(message.getSender())){
                            ObjectOutputStream oos = new ObjectOutputStream(h.get(s).socket.getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                } else if (message.getType().equals(MessageType.FILE_MES)) {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerThread.getServerThread(message.getReceiver()).socket.getOutputStream());
                    oos.writeObject(message);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
