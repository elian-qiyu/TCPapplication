package QQ.client.view;

import QQ.serve.ClientService;

import java.util.Scanner;

public class View {
    //显示主菜单
    private boolean flag = true;
    private String key = null;
    private ClientService clientService = new ClientService();
    Scanner sc = new Scanner(System.in);

     public void mainMenu(){

        while (flag){

            System.out.println("=======欢迎登录宇哥通信系统=======");
            System.out.println("\t\t 1 上号上号");
            System.out.println("\t\t 9 我滚蛋");
            System.out.print("麻溜的给老子选：");
            key = sc.next();
            switch (key){
                case "1":
                    System.out.print("名字叫啥：");
                    String id = sc.next();
                    System.out.print("密码拿来：");
                    String pass = sc.next();

                    if (clientService.checkUser(id, pass)){
                        System.out.println("=======欢迎用户" + id + "=======");
                        while (flag){
                            System.out.println("=======二级菜单(用户 "+ id+ ")=======");
                            System.out.println("\t\t 1 显示在线用户");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 我滚蛋");
                            System.out.print("麻溜的给老子选：");
                            key = sc.next();
                            switch (key){
                                case "1":
                                    clientService.onlineList();
                                    break;
                                case "2":
                                    System.out.print("输入聊天内容：");
                                    String s = sc.next();
                                    clientService.sendMessageAll(s, id);
                                    break;
                                case "3":
                                    System.out.print("输入想聊天的用户(在线)：");
                                    String name = sc.next();
                                    System.out.print("输入聊天内容：");
                                    String content = sc.next();
                                    clientService.sendMessage(content, id, name);
                                    break;
                                case "4":
                                    System.out.print("给谁发文件");
                                    String receiver = sc.next();
                                    System.out.print("文件在哪");
                                    String src = sc.next();
                                    System.out.println("发到哪里");
                                    String dest = sc.next();
                                    clientService.sendFile(src, dest,id, receiver);
                                    break;
                                case "9":
                                    clientService.logOut();
                                    flag = false;
                                    break;
                            }
                        }
                    }else {
                        System.out.println("登录失败");
                    }
                    break;
                case "9"://直接退出什么都没干
                    flag = false;
                    break;
            }
        }
        //退出循环
         System.out.println("您已经滚蛋！");

    }
}
