
//chess
package client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
public class chess extends JFrame implements ActionListener {

    public static final Color bgColor = new Color(222, 197, 144);
    public static final Color focusbg = new Color(242, 242, 242);
    public static final Color focuschar = new Color(222, 197, 160);
    public static final Color color1 = new Color(255, 0, 0);
    public static final Color color2 = Color.black;
    private Socket socket;
    private DatagramSocket datagramSocket;
    JLabel jlHost = new JLabel("Host");
    JLabel jlPort = new JLabel("Port");
    JLabel jlNickName = new JLabel("NickName");

    JTextField jtfHost = new JTextField("127.0.0.1");
    JTextField jtfPort = new JTextField("8787");
    JTextField jtfNickName = new JTextField("Player");

    JButton jbConnect = new JButton("Connect");
    JButton jbDisconnect = new JButton("Disconnect");
    JButton jbFail = new JButton("Fail");
    JButton jbChallenge = new JButton("Challenge");

    JComboBox jcbNickList = new JComboBox();
    JButton jbYChallenge = new JButton("Accept the challenge");
    JButton jbNChallenge = new JButton("Reject the challenge");
    JTextArea showMessage = new JTextArea(10,50);
    JScrollPane sp = new JScrollPane(showMessage);
    JButton btnNewButton = new JButton("傳送");
    JLabel jPort = new JLabel("傳送目標");
    JLabel jInputM= new JLabel("輸入訊息");

    int width = 60;

    
    JTextArea inputMessage = new JTextArea(10,50);
    chessman[][] move_ = new chessman[9][10];
    checkerboard jpz = new checkerboard(move_, width, this);
    JPanel jpy = new JPanel();
    JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpz, jpy);
    
    JTextArea MessagePort = new JTextArea(10,50);
    
    boolean caiPan = false;
    int color = 0;//0 代表紅棋，1代表白棋

    Socket sc;

    ClientThread cat;

    public chess() {

        this.initialComponent();

        this.addListener();

        this.initialState();

        this.initialchessman();

        this.initialFrame();
        initSocket();
    }

    public void initialComponent() {

        jpy.setLayout(null);

        this.jlHost.setBounds(10, 10, 50, 20);
        jpy.add(this.jlHost);

        this.jtfHost.setBounds(70, 10, 80, 20);
        jpy.add(this.jtfHost);

        this.jlPort.setBounds(10, 40, 50, 20);
        jpy.add(this.jlPort);

        this.jtfPort.setBounds(70, 40, 80, 20);
        jpy.add(this.jtfPort);

        this.jlNickName.setBounds(10, 70, 50, 20);
        jpy.add(this.jlNickName);

        this.jtfNickName.setBounds(70, 70, 80, 20);
        jpy.add(this.jtfNickName);

        this.jbConnect.setBounds(10, 100, 80, 20);
        jpy.add(this.jbConnect);

        this.jbDisconnect.setBounds(100, 100, 80, 20);
        jpy.add(this.jbDisconnect);

        this.jcbNickList.setBounds(20, 130, 130, 20);
        jpy.add(this.jcbNickList);

        this.jbChallenge.setBounds(10, 160, 80, 20);
        jpy.add(this.jbChallenge);

        this.jbFail.setBounds(100, 160, 80, 20);
        jpy.add(this.jbFail);

        this.jbYChallenge.setBounds(5, 190, 86, 20);
        jpy.add(this.jbYChallenge);

        this.jbNChallenge.setBounds(100, 190, 86, 20);
        jpy.add(this.jbNChallenge);

        this.sp.setBounds(5,210,170,250);
        jpy.add(this.sp);
        
        this.jInputM.setBounds(5, 460, 170, 20);
        jpy.add(this.jInputM);
        
        this.inputMessage.setBounds(5,480,170,80);
        jpy.add(this.inputMessage);

        this.btnNewButton.setBounds(50,630,80,20);
        jpy.add(this.btnNewButton);
        
        this.jPort.setBounds(5, 560, 170, 20);
        jpy.add(this.jPort);
        
        this.MessagePort.setBounds(5,580,170,40);
        jpy.add(this.MessagePort);

        jpz.setLayout(null);
        jpz.setBounds(0, 0, 700, 700);
    }

    public void addListener() {

        this.jbConnect.addActionListener(this);
        this.jbDisconnect.addActionListener(this);
        this.jbChallenge.addActionListener(this);
        this.jbFail.addActionListener(this);
        this.jbYChallenge.addActionListener(this);
        this.jbNChallenge.addActionListener(this);
        this.btnNewButton.addActionListener(this);
    }

    public void initialState() {

        this.jbDisconnect.setEnabled(false);
        this.jbChallenge.setEnabled(false);
        this.jbYChallenge.setEnabled(false);
        this.jbNChallenge.setEnabled(false);
        this.jbFail.setEnabled(false);
    }

    public void initialchessman() {

        move_[0][0] = new chessman(color1, "車", 0, 0);
        move_[1][0] = new chessman(color1, "馬", 1, 0);
        move_[2][0] = new chessman(color1, "相", 2, 0);
        move_[3][0] = new chessman(color1, "仕", 3, 0);
        move_[4][0] = new chessman(color1, "帥", 4, 0);
        move_[5][0] = new chessman(color1, "仕", 5, 0);
        move_[6][0] = new chessman(color1, "相", 6, 0);
        move_[7][0] = new chessman(color1, "馬", 7, 0);
        move_[8][0] = new chessman(color1, "車", 8, 0);
        move_[1][2] = new chessman(color1, "炮", 1, 2);
        move_[7][2] = new chessman(color1, "炮", 7, 2);
        for(int i = 0 ; i < 9 ; i = i +2)
        {
            move_[i][3] = new chessman(color1, "兵", i, 3);
            move_[i][6] = new chessman(color2, "卒", i, 6);
        }
        move_[0][9] = new chessman(color2, "車", 0, 9);
        move_[1][9] = new chessman(color2, "馬", 1, 9);
        move_[2][9] = new chessman(color2, "象", 2, 9);
        move_[3][9] = new chessman(color2, "士", 3, 9);
        move_[4][9] = new chessman(color2, "將", 4, 9);
        move_[5][9] = new chessman(color2, "士", 5, 9);
        move_[6][9] = new chessman(color2, "象", 6, 9);
        move_[7][9] = new chessman(color2, "馬", 7, 9);
        move_[8][9] = new chessman(color2, "車", 8, 9);
        move_[1][7] = new chessman(color2, "炮", 1, 7);
        move_[7][7] = new chessman(color2, "炮", 7, 7);


    }

    public void initialFrame() {

        this.setTitle("象棋");
        Image image = new ImageIcon("ico.gif").getImage();
        this.setIconImage(image);
        this.add(this.jsp);

        jsp.setDividerLocation(730);
        jsp.setDividerSize(4);

        this.setBounds(30, 30, 930, 730);
        this.setVisible(true);
        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        if (cat == null)
                        {
                            System.exit(0);
                            return;
                        }
                        try {
                            if (cat.tiaoZhanZhe != null)
                            {
                                try {

                                    cat.dout.writeUTF("<#SURRENDER#>" + cat.tiaoZhanZhe);
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                            cat.dout.writeUTF("<#CLIENT_LEAVE#>");
                            cat.flag = false;
                            cat = null;

                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        System.exit(0);
                    }

                }
        );
    }
    
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.jbConnect) {
            this.jbConnect_event();
        } else if (e.getSource() == this.jbDisconnect) {
            this.jbDisconnect_event();
        } else if (e.getSource() == this.jbChallenge) {
            this.jbChallenge_event();
        } else if (e.getSource() == this.jbYChallenge) {
            this.jbYChallenge_event();
        } else if (e.getSource() == this.jbNChallenge) {
            this.jbNChallenge_event();
        } else if (e.getSource() == this.jbFail) {
            this.jbFail_event();
        } else if (e.getSource() == this.btnNewButton){
            this.btn();
        }
    }
    public void btn() {
        final String ipAddress = jtfHost.getText();
        final String port = MessagePort.getText();

        String sendContent =inputMessage.getText();
        byte[] buff = sendContent.getBytes();
        try 
        {
            showMessage.append("我對" + jcbNickList.getSelectedItem()+ "說：\n" + inputMessage.getText() + "\n\n");
            showMessage.setCaretPosition(inputMessage.getText().length());
            datagramSocket.send(new DatagramPacket(buff, buff.length,InetAddress.getByName(ipAddress), Integer.parseInt(port)));
            inputMessage.setText("");
        } 
        catch (Exception el) 
        {
            JOptionPane.showMessageDialog(this,"出错了，不成功");
            el.printStackTrace();
        }
    }
    public void jbConnect_event() {
        int port = 0;

        try {
            port = Integer.parseInt(this.jtfPort.getText().trim());
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "Port只能是整数", "錯誤",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (port > 65535 || port < 0) {
            JOptionPane.showMessageDialog(this, "Port只能是0-65535的整数", "錯誤",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = this.jtfNickName.getText().trim();

        if (name.length() == 0) {
            JOptionPane.showMessageDialog(this, "玩家姓名不能是空的", "錯誤",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            sc = new Socket(this.jtfHost.getText().trim(), port);
            cat = new ClientThread(this);
            cat.start();

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

            JOptionPane.showMessageDialog(this, "已連接到伺服器", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, "連接伺服器失敗", "錯誤",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    public void initSocket() {
        while (true) {
            //先关闭
            if (datagramSocket != null && !datagramSocket.isClosed()) {
                datagramSocket.close();
            }
            int port = Integer.parseInt(JOptionPane.showInputDialog(this,
                    "輸入您的Port", "Port", JOptionPane.QUESTION_MESSAGE));
            if (port <= 1024 || port > 65535) {
                JOptionPane.showMessageDialog(this, "超出Port範圍");
                continue;
            }

            try {
                datagramSocket = new DatagramSocket(port);
                startListen();
             
                break;
            } catch (SocketException e) {
                JOptionPane.showMessageDialog(this, "Port已被使用，請重新設定Port");
            }
        }
    }
    private void startListen() {
        new Thread() {
            private DatagramPacket packet;

            @Override
            public void run() {
                byte[] buff = new byte[64 * 1024];
                packet = new DatagramPacket(buff, buff.length);
                while (!datagramSocket.isClosed()) {
                    try {
                        datagramSocket.receive(packet); 
                        showMessage.append(packet.getAddress().getHostAddress()
                                + ":" + packet.getSocketAddress() + "對我說：\n"
                                + new String(packet.getData(), 0, packet.getLength())
                                + "\n\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public void jbDisconnect_event() {
        try {
            this.cat.dout.writeUTF("<#CLIENT_LEAVE#>");
            this.cat.flag = false;
            this.cat = null;
            this.jtfHost.setEnabled(!false);
            this.jtfPort.setEnabled(!false);
            this.jtfNickName.setEnabled(!false);
            this.jbConnect.setEnabled(!false);
            this.jbDisconnect.setEnabled(!true);
            this.jbChallenge.setEnabled(!true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbChallenge_event() {

        Object o = this.jcbNickList.getSelectedItem();

        if (o == null || ((String) o).equals("")) {
            JOptionPane.showMessageDialog(this, "請選擇對手名稱", "錯誤",
                    JOptionPane.ERROR_MESSAGE);//未選到，跳出錯誤訊息
        } else {

            String name2 = (String) this.jcbNickList.getSelectedItem();

            try {
                this.jtfHost.setEnabled(false);
                this.jtfPort.setEnabled(false);
                this.jtfNickName.setEnabled(false);
                this.jbConnect.setEnabled(false);
                this.jbDisconnect.setEnabled(!true);
                this.jbChallenge.setEnabled(!true);
                this.jbYChallenge.setEnabled(false);
                this.jbNChallenge.setEnabled(false);
                this.jbFail.setEnabled(false);
                this.cat.tiaoZhanZhe = name2;
                this.caiPan = true;
                this.color = 0;

                this.cat.dout.writeUTF("<#Challenge#>" + name2);
                JOptionPane.showMessageDialog(this, "已發出挑戰,請等待回復...", "提示",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    public void jbYChallenge_event() {

        try {

            this.cat.dout.writeUTF("<#AGREE#>" + this.cat.tiaoZhanZhe);
            this.caiPan = false;
            this.color = 1;

            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(!true);
            this.jbChallenge.setEnabled(!true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(!false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbNChallenge_event() {

        try {
            this.cat.dout.writeUTF("<#DESAGREE#>" + this.cat.tiaoZhanZhe);
            this.cat.tiaoZhanZhe = null;
            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void jbFail_event() {

        try {
            this.cat.dout.writeUTF("<#SURRENDER#>" + this.cat.tiaoZhanZhe);
            this.cat.tiaoZhanZhe = null;
            this.color = 0;
            this.caiPan = false;
            this.next();
            this.jtfHost.setEnabled(false);
            this.jtfPort.setEnabled(false);
            this.jtfNickName.setEnabled(false);
            this.jbConnect.setEnabled(false);
            this.jbDisconnect.setEnabled(true);
            this.jbChallenge.setEnabled(true);
            this.jbYChallenge.setEnabled(false);
            this.jbNChallenge.setEnabled(false);
            this.jbFail.setEnabled(false);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void next() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                this.move_[i][j] = null;
            }
        }

        this.caiPan = false;
        this.initialchessman();
        this.repaint();
    }

    public static void main(String args[]) {
        new chess();
    }
    
}
