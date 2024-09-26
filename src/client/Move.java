
//Move
package client;
public class Move {

  chessman[][] move_;
  boolean canMove = false;

  int i;
  int j;

  public Move(chessman[][] move_) {
      this.move_ = move_;
  }

  public boolean canMove(int startI, int startJ, int endI, int endJ, String name) {
      int maxI;
      int minI;
      int maxJ;
      int minJ;
      canMove = true;
      if (startI >= endI)
      {
          maxI = startI;
          minI = endI;
      } 
      else
      {
          maxI = endI;
          minI = startI;
      }
      if (startJ >= endJ)
      {
          maxJ = startJ;
          minJ = endJ;
      } 
      else {
          maxJ = endJ;
          minJ = startJ;
      }
      if (name.equals("帥") || name.equals("將"))this.leader(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
      else if (name.equals("士") || name.equals("仕"))this.shi(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
      else if (name.equals("相"))this.elephant1(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
      else if (name.equals("象"))this.elephant2(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
      else if (name.equals("車"))this.car(maxI, minI, maxJ, minJ);
      else if (name.equals("馬"))this.ma(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
      else if (name.equals("炮"))this.cannon(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
      else if (name.equals("兵"))this.bing_quan(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
      else if (name.equals("卒"))this.bing_zu(maxI, minI, maxJ, minJ, startI, startJ, endI, endJ);
      return canMove;
  }

  public void car(int maxI, int minI, int maxJ, int minJ) {
      if (maxI == minI)
      {
          for (j = minJ + 1; j < maxJ; j++) {
              if (move_[maxI][j] != null)
              {
                  canMove = false;
                  break;
              }
          }
      } 
      else if (maxJ == minJ)
      {
          for (i = minJ + 1; i < maxJ; i++) {
              if (move_[i][maxJ] != null)
              {
                  canMove = false;
                  break;
              }
          }
      } 
      else if (maxI != minI && maxJ != minJ)canMove = false;
  }

  public void ma(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

      int a = maxI - minI;
      int b = maxJ - minJ;

      if (a == 1 && b == 2)
      {
          if (startJ > endJ)
          {
              if (move_[startI][startJ - 1] != null)canMove = false;
          } else {//如果是從上往下走
              if (move_[startI][startJ + 1] != null)canMove = false;
          }
      }
      else if (a == 2 && b == 1)
      {
          if (startI > endI)
          {
              if (move_[startI - 1][startJ] != null)canMove = false;
          } 
          else 
          {
              if (move_[startI + 1][startJ] != null)canMove = false;
          }
      } 
      else if (!((a == 2 && b == 1) || (a == 1 && b == 2)))canMove = false;
  }

  public void elephant1(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
      int a = maxI - minI;
      int b = maxJ - minJ;
      if (a == 2 && b == 2)
      {
          if (endJ > 4)canMove = false;//不可以走
          if (move_[(maxI + minI) / 2][(maxJ + minJ) / 2] != null)canMove = false;
      } 
      else canMove = false;
  }

  public void elephant2(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
      int a = maxI - minI;
      int b = maxJ - minJ;
      if (a == 2 && b == 2)
      {
          if (endJ < 5) canMove = false;
          if (move_[(maxI + minI) / 2][(maxJ + minJ) / 2] != null)canMove = false;
      } 
      else canMove = false;
  }

  public void shi(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

      int a = maxI - minI;
      int b = maxJ - minJ;

      if (a == 1 && b == 1)
      {
          if (startJ > 4)
          {
              if (endJ < 7) canMove = false;
          } 
          else 
          {
              if (endJ > 2) canMove = false;
          }
          if (endI > 5 || endI < 3)canMove = false;
      } 
      else canMove = false;
  }

  public void leader(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
      int a = maxI - minI;
      int b = maxJ - minJ;
      if ((a == 1 && b == 0) || (a == 0 && b == 1)) {
          if (startJ > 4)
          {
              if (endJ < 7)canMove = false;
          } else
          {
              if (endJ > 2)canMove = false;
          }
          if (endI > 5 || endI < 3)canMove = false;
      } else canMove = false;
  }

  public void cannon(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {

      if (maxI == minI)
      {
          if (move_[endI][endJ] != null)
          {
              int count = 0;
              for (j = minJ + 1; j < maxJ; j++) {
                  if (move_[minI][j] != null)count++;
              }
              if (count != 1)canMove = false;
          } else if (move_[endI][endJ] == null)
          {
              for (j = minJ + 1; j < maxJ; j++) {
                  if (move_[minI][j] != null)
                  {
                      canMove = false;
                      break;
                  }
              }
          }
      } else if (maxJ == minJ)
      {
          if (move_[endI][endJ] != null)
          {
              int count = 0;
              for (i = minI + 1; i < maxI; i++) {
                  if (move_[i][minJ] != null)count++;
              }
              if (count != 1) canMove = false;
          } else if (move_[endI][endJ] == null)
          {
              for (i = minI + 1; i < maxI; i++) {
                  if (move_[i][minJ] != null)
                  {
                      canMove = false;
                      break;
                  }
              }
          }
      } else if (maxJ != minJ && maxI != minI) canMove = false;
  }

  public void bing_quan(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
      if (startJ < 5)//如果還沒有過河
      {
          if (startI != endI) canMove = false;
          if (endJ - startJ != 1) canMove = false;
      } 
      else 
      {
          if (startI == endI) {
              if (endJ - startJ != 1) canMove = false;
          } 
          else if (startJ == endJ) 
          {
              if (maxI - minI != 1) canMove = false;
          } 
          else if (startI != endI && startJ != endJ) canMove = false;
      }
  }

  public void bing_zu(int maxI, int minI, int maxJ, int minJ, int startI, int startJ, int endI, int endJ) {
      if (startJ > 4) 
      {//如果還沒有過河
          if (startI != endI) canMove = false;
          if (endJ - startJ != -1) canMove = false;
      } 
      else 
      {
          if (startI == endI) {
              if (endJ - startJ != -1) canMove = false;
          } 
          else if (startJ == endJ) 
          {
              if (maxI - minI != 1) canMove = false;
          } 
          else if (startI != endI && startJ != endJ) canMove = false;

      }
    }
}