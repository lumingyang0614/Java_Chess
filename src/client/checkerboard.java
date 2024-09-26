//checkerboard
package client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class checkerboard extends JPanel implements MouseListener {
  private int width;
  boolean focus = false;
  int leader1_i = 4;
  int leader1_j = 0;
  int leader2_i = 4;
  int leader2_j = 9;
  int startI = -1;
  int startJ = -1;
  int endI = -1;
  int endJ = -1;
  public chessman chessmans_[][];
  chess chess_ = null;
  Move move;
  public checkerboard(chessman chessmans_[][], int width, chess chess_) {
      this.chess_ = chess_;
      this.chessmans_ = chessmans_;
      this.width = width;
      move = new Move(chessmans_);
      this.addMouseListener(this);
      this.setBounds(0, 0, 700, 700);//棋盤大小
      this.setLayout(null);
  }

  public void paint(Graphics g1) {

      Graphics2D g = (Graphics2D) g1;
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      Color c = g.getColor();
      g.setColor(chess.bgColor);
      g.fill3DRect(60, 30, 580, 630, false);
      g.setColor(chess.focuschar);
      for (int i = 80; i <= 620; i = i + 60) g.drawLine(110, i, 590, i);
      for (int i = 170; i <= 530; i = i + 60) {
          g.drawLine(i, 80, i, 320);
          g.drawLine(i, 380, i, 620);
      }
      g.drawLine(110, 80, 110, 620);
      g.drawLine(590, 80, 590, 620);
      g.drawLine(290, 80, 410, 200);
      g.drawLine(290, 200, 410, 80);
      g.drawLine(290, 500, 410, 620);
      g.drawLine(290, 620, 410, 500);
      for (int i = 0; i <= 9;i = i +2){
        this.boardLine(g,i,3);
        this.boardLine(g,i,6);
      }
      this.boardLine(g, 1, 2);
      this.boardLine(g, 7, 2);
      this.boardLine(g, 1, 7);
      this.boardLine(g, 7, 7);

      g.setColor(chess.focuschar);
      Font font1 = new Font("標楷體", Font.BOLD, 50);
      g.setFont(font1);
      g.drawString("楚 河", 170, 365);
      g.drawString("漢 界", 400, 365);
      Font font = new Font("標楷體", Font.BOLD, 30);
      g.setFont(font);
      for (int i = 0; i < 9; i++) {
          for (int j = 0; j < 10; j++) {//畫棋子
              if (chessmans_[i][j] != null) {

                  if (this.chessmans_[i][j].getFocus() != false) {//是否選到
                      g.setColor(chess.focusbg);//選到的背景色
                      g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);
                      g.setColor(chess.focuschar);

                  } else {
                      g.fillOval(110 + i * 60 - 25, 80 + j * 60 - 25, 50, 50);
                      g.setColor(chessmans_[i][j].getColor());
                  }
                  g.drawString(chessmans_[i][j].getName(), 110 + i * 60 - 15, 80 + j * 60 + 10);
                  g.setColor(chess.focuschar);
              }
          }
      }
      g.setColor(c);
  }

  public void mouseClicked(MouseEvent e) {
      if (this.chess_.caiPan == true) {
          int i = -1, j = -1;
          int[] chospos = getPos(e);
          i = chospos[0];
          j = chospos[1];
          if (i >= 0 && i <= 8 && j >= 0 && j <= 9) {
              if (focus == false) this.noFocus(i, j);
              else 
              {

                  if (chessmans_[i][j] != null) 
                  {//如果該處有棋子
                      if (chessmans_[i][j].getColor() == chessmans_[startI][startJ].getColor()) 
                      {//如果是自己的棋子
                          chessmans_[startI][startJ].setFocus(false);
                          chessmans_[i][j].setFocus(true);//更改選的對象
                          startI = i;
                          startJ = j;
                      } 
                      else 
                      {//如果是對方的棋子
                          endI = i;//保存該點
                          endJ = j;
                          String name = chessmans_[startI][startJ].getName();//獲得棋子名稱
                          //看是否可以移動
                          boolean canMove = move.canMove(startI, startJ, endI, endJ, name);
                          if (canMove)
                          {
                              try {
                                  this.chess_.cat.dout.writeUTF("<#MOVE#>" +this.chess_.cat.tiaoZhanZhe + startI + startJ + endI + endJ);
                                  this.chess_.caiPan = false;
                                  if (chessmans_[endI][endJ].getName().equals("帥") ||chessmans_[endI][endJ].getName().equals("將")) this.success();
                                  else this.noJiang();
                              } 
                              catch (Exception ee) {
                                  ee.printStackTrace();
                              }
                          }
                      }
                  } 
                  else 
                  {
                      endI = i;
                      endJ = j;
                      String name = chessmans_[startI][startJ].getName();
                      boolean canMove = move.canMove(startI, startJ, endI, endJ, name);//判斷是否是可以走的
                      if (canMove) this.nochessman();
                  }
              }
          }
          this.chess_.repaint();
      }
  }

  public int[] getPos(MouseEvent e) {
      int[] chospos = new int[2];
      chospos[0] = -1;
      chospos[1] = -1;
      Point p = e.getPoint();
      double x = p.getX();
      double y = p.getY();
      if (Math.abs((x - 110) / 1 % 60) <= 25) chospos[0] = Math.round((float) (x - 110)) / 60;
      else if (Math.abs((x - 110) / 1 % 60) >= 35) chospos[0] = Math.round((float) (x - 110)) / 60 + 1;
      if (Math.abs((y - 80) / 1 % 60) <= 25)   chospos[1] = Math.round((float) (y - 80)) / 60;
      else if (Math.abs((y - 80) / 1 % 60) >= 35) chospos[1] = Math.round((float) (y - 80)) / 60 + 1;
      return chospos;
  }

  public void noFocus(int i, int j) {

      if (this.chessmans_[i][j] != null)//如果該處有棋子
      {
          if (this.chess_.color == 0)//如果是红方
          {
              if (this.chessmans_[i][j].getColor().equals(chess.color1))//如果棋子是红色
              {
                  this.chessmans_[i][j].setFocus(true);//將該棋設定為選中狀態
                  focus = true;
                  startI = i;
                  startJ = j;
              }
          } else//如果是白方
          {
              if (this.chessmans_[i][j].getColor().equals(chess.color2))//如果棋子是白色的
              {
                  this.chessmans_[i][j].setFocus(true);//將該棋設定為選中狀態
                  focus = true;
                  startI = i;
                  startJ = j;
              }
          }
      }
  }
  public void reset_setting() 
  {
    this.chess_.cat.tiaoZhanZhe = null;
    this.chess_.color = 0;
    this.chess_.caiPan = false;
    this.chess_.next();
    this.chess_.jtfHost.setEnabled(false);
    this.chess_.jtfPort.setEnabled(false);
    this.chess_.jtfNickName.setEnabled(false);
    this.chess_.jbConnect.setEnabled(false);
    this.chess_.jbDisconnect.setEnabled(true);
    this.chess_.jbChallenge.setEnabled(true);
    this.chess_.jbYChallenge.setEnabled(false);
    this.chess_.jbNChallenge.setEnabled(false);
    this.chess_.jbFail.setEnabled(false);
    leader1_i = 4;
    leader1_j = 0;
    leader2_i = 4;
    leader2_j = 9;

  }
  public void dset(){
    startI = -1;
    startJ = -1;
    endI = -1;
    endJ = -1;
    focus = false;
  }
  public void success() {

      chessmans_[endI][endJ] = chessmans_[startI][startJ];//吃掉棋子
      chessmans_[startI][startJ] = null;//將原來設定為空的
      this.chess_.repaint();//重绘
      JOptionPane.showMessageDialog(this.chess_, "恭喜您，您贏了", "提示",JOptionPane.INFORMATION_MESSAGE);
      reset_setting();
      dset();
  }

  public void noJiang() 
  {
      chessmans_[endI][endJ] = chessmans_[startI][startJ];
      chessmans_[startI][startJ] = null;
      chessmans_[endI][endJ].setFocus(false);
      this.chess_.repaint();
      if (chessmans_[endI][endJ].getName().equals("帥")) updateleader1place();
      else if (chessmans_[endI][endJ].getName().equals("將")) updateleader2place();
      dset();
  }
  public void updateleader1place(){
    leader1_i = endI;
    leader1_j = endJ;
  }
  public void updateleader2place(){
    leader2_i = endI;
    leader2_j = endJ;
  }
  public void nochessman() 
  {
      try {
          this.chess_.cat.dout.writeUTF("<#MOVE#>" + this.chess_.cat.tiaoZhanZhe + startI + startJ + endI + endJ);
          this.chess_.caiPan = false;
          chessmans_[endI][endJ] = chessmans_[startI][startJ];
          chessmans_[startI][startJ] = null;//下棋
          chessmans_[endI][endJ].setFocus(false);//設定為沒有被選中的狀態
          this.chess_.repaint();
          if (chessmans_[endI][endJ].getName().equals("帥"))updateleader1place();
          else if (chessmans_[endI][endJ].getName().equals("將")) updateleader2place();
          if (leader1_i == leader2_i)
          {
              int count = 0;
              for (int leader_j = leader1_j + 1; leader_j < leader2_j; leader_j++) 
              {
                  if (chessmans_[leader1_i][leader_j] != null) {
                      count++;
                      break;
                  }
              }
              if (count == 0) 
              {
                JOptionPane.showMessageDialog(this.chess_, "你輸了！！！", "提示",JOptionPane.INFORMATION_MESSAGE);
                reset_setting();
              }
          }
          dset();

      } catch (Exception ee) {
          ee.printStackTrace();
      }
  }
  
  public void move(int startI, int startJ, int endI, int endJ) {
      if (chessmans_[endI][endJ] != null && (chessmans_[endI][endJ].getName().equals("帥") ||chessmans_[endI][endJ].getName().equals("將"))) {//如果"將"被吃了
          chessmans_[endI][endJ] = chessmans_[startI][startJ];
          chessmans_[startI][startJ] = null;//下棋
          this.chess_.repaint();
          JOptionPane.showMessageDialog(this.chess_, "很遺憾，您輸了！！！", "提示",JOptionPane.INFORMATION_MESSAGE);
          reset_setting();
      } 
      else 
      {

          chessmans_[endI][endJ] = chessmans_[startI][startJ];
          chessmans_[startI][startJ] = null;//下棋
          this.chess_.repaint();
          if (chessmans_[endI][endJ].getName().equals("帥")) 
          {
              updateleader1place();
          } 
          else if (chessmans_[endI][endJ].getName().equals("將")) 
          {
              updateleader2place();
          }
          if (leader1_i == leader2_i) 
          {
              int count = 0;
              for (int leader_j = leader1_j + 1; leader_j < leader2_j; leader_j++) 
              {
                  if (chessmans_[leader1_i][leader_j] != null) 
                  {//有棋子
                      count++;
                      break;
                  }
              }
              if (count == 0)
              {
                  JOptionPane.showMessageDialog(this.chess_, "你贏了！！","提示", JOptionPane.INFORMATION_MESSAGE);
                  reset_setting();
              }
          }
      }
      this.chess_.repaint();
  }


 

  public void boardLine(Graphics2D g, int i, int j) {

      int x = 110 + 60 * i;
      int y = 80 + 60 * j;

      if (i > 0) {
          g.drawLine(x - 3, y - 3, x - 20, y - 3);
          g.drawLine(x - 3, y - 3, x - 3, y - 20);
      }
      if (i < 8) {
          g.drawLine(x + 3, y - 3, x + 20, y - 3);
          g.drawLine(x + 3, y - 3, x + 3, y - 20);
      }
      if (i > 0) {
          g.drawLine(x - 3, y + 3, x - 20, y + 3);
          g.drawLine(x - 3, y + 3, x - 3, y + 20);
      }
      if (i < 8) {
          g.drawLine(x + 3, y + 3, x + 20, y + 3);
          g.drawLine(x + 3, y + 3, x + 3, y + 20);
      }
  }
  public void mouseReleased(MouseEvent e) {
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }
@Override
public void mousePressed(MouseEvent e) {
  // TODO Auto-generated method stub
  
}
}