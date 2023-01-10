package QQ.serve;

import QQ.common.Message;
import QQ.common.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientThread extends Thread{

    private Socket socket;

    public ClientThread (Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //一直通讯
        while (true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //此处会阻塞
                Message message = (Message) ois.readObject();
                if (message.getType().equals(MessageType.RETURN_ONLINE)){
                    //返回在线列表
                    String[] s = message.getContent().split(" ");
                    System.out.println("\n=======当前在线用户列表=======");
                    String onlineUsers = "";
                    for (int i = 0; i < s.length; i++) {
                        //onlineUsers += s[i];
                        System.out.println("用户：" + s[i]);
                    }
                } else if (message.getType().equals(MessageType.COMMON_MES)) {

                    System.out.println("\n" + message.getTime());
                    System.out.println(message.getSender() + " 对 "+
                            message.getReceiver() + " 说" +message.getContent());
                } else if (message.getType().equals(MessageType.TOALL_MES)) {

                    System.out.println("\n" + message.getSender() + "对大家说" + message.getContent());

                } else if (message.getType().equals(MessageType.FILE_MES)) {
                    System.out.println("\n"+message.getSender()+" 给 "+message.getReceiver()+"发文件："+message.getSrc()+" 到我的电脑目录 "+message.getDest());
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("保存文件成功");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}

