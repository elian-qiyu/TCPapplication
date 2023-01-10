package QQ.serve;

import QQ.common.Message;
import QQ.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class SendNews implements Runnable{
    private Scanner sc = new Scanner(System.in);
    @Override
    public void run() {
        while (true) {
            System.out.println("输入推送的新闻");
            String news = sc.next();
            Message message = new Message();
            message.setType(MessageType.TOALL_MES);
            message.setContent(news);
            message.setSender("服务器");
            message.setTime(new Date().toString());
            System.out.println("服务器推送消息给所有人");

            HashMap<String, ServerThread> h = ManageServerThread.getH();
            for (String s : h.keySet()) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(h.get(s).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
