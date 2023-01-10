package QQ.serve;

import QQ.common.Message;
import QQ.common.MessageType;
import QQ.common.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class ClientService {
    private User user = new User();
    private Socket socket;//在checkUser里赋值了socket
    private boolean flag = false;

    //
    public boolean checkUser(String id, String pass) {
        user.setId(id);
        user.setPass(pass);
        //发送User对象
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 8888);
            //对象流
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);//发送User对象

            //读取服务器发送来的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message m = (Message) ois.readObject();

            if (m.getType().equals(MessageType.LOGIN_SUCCEED)) {
                //连接成功，创建线程类
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();
                ManageClientThread.addClientThread(id, clientThread);
                flag = true;
            } else {
                //连接失败
                socket.close();

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return flag;
    }

    //请求用户列表
    public void onlineList() {
        Message message = new Message();
        message.setType(MessageType.GET_ONLINE);
        message.setSender(user.getId());

        try {
            //获取当前用户的socket  通过当前线程对应的socket获取
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(user.getId()).getSocket().getOutputStream());
            oos.writeObject(message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //退出客户端
    public void logOut() {
        Message message = new Message();
        message.setType(MessageType.CLIENT_EXIT);
        message.setSender(user.getId());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getId() + "：退出了系统");
            System.exit(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageAll(String content, String sender) {
        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setType(MessageType.TOALL_MES);
        message.setTime(new Date().toString());
        System.out.println(sender + " 对大家说 " + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String content, String sender, String receiver) {

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setType(MessageType.COMMON_MES);
        message.setTime(new Date().toString());
        System.out.println(sender + " 对 " + receiver + " 说 " + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //发送文件
    public void sendFile(String src, String dest, String sender, String receiver) {

        Message message = new Message();
        message.setType(MessageType.FILE_MES);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSrc(src);
        message.setDest(dest);

        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int) new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        System.out.println("\n" + sender + "给" + receiver + "发送文件：" + src + "到对方目录" + dest);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
