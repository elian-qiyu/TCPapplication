package QQ.common;

public interface MessageType {
    String LOGIN_SUCCEED = "1";
    String LOGIN_DEFEAT = "2";

    String COMMON_MES = "3";//普通信息包

    String GET_ONLINE = "4";//请求返回用户

    String RETURN_ONLINE = "5";//返回在线用户

    String CLIENT_EXIT = "6";//客户端请求退出

    String TOALL_MES = "7";//群发的消息

    String FILE_MES = "8";

}
