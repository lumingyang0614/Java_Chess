
//ServerAgentThread
package Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread {

    Server father;
    Socket sc;

    DataInputStream din;
    DataOutputStream dout;

    boolean flag = true;

    public ServerAgentThread(Server father, Socket sc) {

        this.father = father;
        this.sc = sc;

        try {

            din = new DataInputStream(sc.getInputStream());
            dout = new DataOutputStream(sc.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while (flag) {
            try {
                String msg = din.readUTF().trim();//接收客戶端傳來的訊息
                if (msg.startsWith("<#NICK_NAME#>"))//收到[用戶的訊息]
                {
                    this.nick_name(msg);
                } else if (msg.startsWith("<#CLIENT_LEAVE#>")) {
                    this.client_leave(msg);
                } else if (msg.startsWith("<#Challenge#>")) {
                    this.tiao_zhan(msg);
                } else if (msg.startsWith("<#AGREE#>")) {
                    this.tong_yi(msg);
                } else if (msg.startsWith("<#DESAGREE#>")) {
                    this.butong_yi(msg);
                } else if (msg.startsWith("<#BUSY#>")) {
                    this.busy(msg);
                } else if (msg.startsWith("<#MOVE#>")) {
                    this.move(msg);
                } else if (msg.startsWith("<#SURRENDER#>")) {
                    this.renshu(msg);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void nick_name(String msg) {
        try {

            String name = msg.substring(13);//獲得用戶名稱
            this.setName(name);//用該名稱給Thread取名

            Vector v = father.onlineList;//獲得在線用戶列表
            boolean isChongMing = false;

            int size = v.size();

            for (int i = 0; i < size; i++) {
                ServerAgentThread tempSat = (ServerAgentThread) v.get(i);

                if (tempSat.getName().equals(name)) {
                    isChongMing = true;
                    break;
                }
            }
            if (isChongMing == true) {
                dout.writeUTF("<#NAME_CHONGMING#>");
                din.close();
                dout.close();
                sc.close();

                flag = false;
            } else {
                v.add(this);
                father.refreshList();
                String nickListMsg = "";
                size = v.size();

                for (int i = 0; i < size; i++) {
                    ServerAgentThread tempSat = (ServerAgentThread) v.get(i);
                    nickListMsg = nickListMsg + "|" + tempSat.getName();
                }
                nickListMsg = "<#NICK_LIST#>" + nickListMsg;
                Vector tempv = father.onlineList;
                size = tempv.size();

                for (int i = 0; i < size; i++) {
                    ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);
                    satTemp.dout.writeUTF(nickListMsg);

                    if (satTemp != this) {
                        satTemp.dout.writeUTF("<#MSG#>" + this.getName() + "上线了...");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void client_leave(String msg) {
        try {

            Vector tempv = father.onlineList;
            tempv.remove(this);
            int size = tempv.size();
            String nl = "<#NICK_LIST#>";

            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);

                satTemp.dout.writeUTF("<#MSG#>" + this.getName() + "离线了...");

                nl = nl + "|" + satTemp.getName();
            }
            for (int i = 0; i < size; i++) {

                ServerAgentThread satTemp = (ServerAgentThread) tempv.get(i);
                satTemp.dout.writeUTF(nl);
            }
            this.flag = false;
            father.refreshList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tiao_zhan(String msg) {
        try {
            String name1 = this.getName();//獲得挑戰者的名字
            String name2 = msg.substring(13);//獲得被挑戰的用戶名

            Vector v = father.onlineList;
            int size = v.size();
            for (int i = 0; i < size; i++) {//遍歷列表，搜索被挑戰的人
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);

                if (satTemp.getName().equals(name2)) {//向該用戶發送挑戰資訊與名稱
                    satTemp.dout.writeUTF("<#Challenge#>" + name1);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tong_yi(String msg) {
        try {

            String name = msg.substring(11);//獲得提出挑戰的用戶名稱
            Vector v = father.onlineList;
            int size = v.size();
            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);
                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF("<#AGREE#>");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void butong_yi(String msg) {
        try {
            String name = msg.substring(13);
            Vector v = father.onlineList;

            int size = v.size();
            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);

                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF("<#DESAGREE#>");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void busy(String msg) {
        try {
            String name = msg.substring(8);
            Vector v = father.onlineList;

            int size = v.size();
            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);

                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF("<#BUSY#>");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move(String msg) {
        try {
            String name = msg.substring(8, msg.length() - 4);
            Vector v = father.onlineList;

            int size = v.size();
            for (int i = 0; i < size; i++) {

                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);
                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF(msg);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renshu(String msg) {
        try {
            String name = msg.substring(10);
            Vector v = father.onlineList;

            int size = v.size();
            for (int i = 0; i < size; i++) {
                ServerAgentThread satTemp = (ServerAgentThread) v.get(i);

                if (satTemp.getName().equals(name)) {
                    satTemp.dout.writeUTF(msg);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}