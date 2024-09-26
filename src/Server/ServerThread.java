//ServerThread
package Server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
  Server father;
  ServerSocket ss;
  boolean flag = true;
  public ServerThread(Server father) {
      this.father = father;
      ss = father.ss;
  }
  public void run() {
      while (flag) {
          try {

              Socket sc = ss.accept();//等待其他Port連接
              ServerAgentThread sat = new ServerAgentThread(father, sc);
              sat.start();//創建並啟動伺服器Thread

          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  }
}