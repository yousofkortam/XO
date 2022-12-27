/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyXOGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author ikort
 */


class GameMove {
    
    private int x;
    private int y;
    public GameMove(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}

public class MyXOGame {
    
    JButton btn1 = new JButton();
    JButton btn2 = new JButton();
    JButton btn3 = new JButton();
    JButton btn4 = new JButton();
    JButton btn5 = new JButton();
    JButton btn6 = new JButton();
    JButton btn7 = new JButton();
    JButton btn8 = new JButton();
    JButton btn9 = new JButton();
    
    
    public static int cnt = 0;
    JFrame frame = new JFrame();
    JPanel pane1 = new JPanel();
    JPanel pane2 = new JPanel();
    JLabel text = new JLabel();
    public static JButton gameBoard[][] = new JButton[3][3];
    public static String ans[][] = new String[3][3];
    private ArrayList<GameMove> availableGameMoves;
    private static final Scanner scanner = new Scanner(System.in);
    private GameMove computersGameMove;
    Random random = new Random();
    boolean player1_turn = false;
    
    public MyXOGame() {
        if (cnt++ == 0) {
            frame.setVisible(true);
            frame.setSize(500, 500);
            frame.getContentPane().setBackground(Color.LIGHT_GRAY);
            frame.setLayout(new BorderLayout());

            text.setBackground(new Color(25, 25, 25));
            text.setForeground(Color.BLACK);
            text.setHorizontalAlignment(JLabel.CENTER);
            text.setText("XO Game");
            text.setFont(new Font("Ink Free", Font.BOLD, 65));

            pane1.setLayout(new BorderLayout());
            pane1.setBounds(0, 0, 500, 100);
            pane1.add(text);

            pane2.setLayout(new GridLayout(3, 3));
            pane2.setBackground(Color.LIGHT_GRAY);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    final int x = i, y = j;
                    JButton button = new JButton();
                    button.setFont(new Font("MV Boli", Font.BOLD, 120));
                    gameBoard[i][j] = button;
                    pane2.add(button);
                    
                }
            }
            frame.add(pane1, BorderLayout.NORTH);
            frame.add(pane2);
        }
        
        LOOP();
        
    }
    
    public void LOOP() {
        Random random = new Random();
//        System.out.println("");
        System.out.println("Computer will use X, You will use O\n");
        makeGameMove(new GameMove(random.nextInt(3), random.nextInt(3)), "X");
        displayGameBoard();

        while (isGameOver() == false) {
            System.out.print("Your Move: ");
            int userx = scanner.nextInt(), usery = scanner.nextInt();
            if (!gameBoard[userx][usery].getText().equals("")) {
                System.out.println("Please Enter a valid position");
                continue;
            }
            makeGameMove(new GameMove(userx, usery), "O");
//            displayGameBoard();

            if (isGameOver()) break;

            minimax(0, 1);

            makeGameMove(computersGameMove, "X");
            displayGameBoard();

        }

        if (has1Won())
            JOptionPane.showMessageDialog(null, "Opps, You Lose..!");
        else if (has2Won())
            JOptionPane.showMessageDialog(null, "Woow, You Win..!");
        else
            JOptionPane.showMessageDialog(null, "Draw Match..!");
    }
   
    
    public ArrayList<GameMove> getAvailableGameMoves() {

        availableGameMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (ans[i][j].equals("0"))
                    availableGameMoves.add(new GameMove(i, j));
            }
        }
        return availableGameMoves;
    }

    public void displayGameBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!ans[i][j].equals("0")) {
                    gameBoard[i][j].setText(ans[i][j]);
                }
            }
        }
    }

    private boolean isGameOver() {
        return (has1Won() || has2Won() || getAvailableGameMoves().isEmpty());
    }

    private boolean has1Won() {

        if ((ans[0][0] == ans[1][1] && ans[0][0] == ans[2][2] && ans[0][0] == "X")
         || (ans[0][2] == ans[1][1] && ans[0][2] == ans[2][0] && ans[0][2] == "X")) {
            return true;
	}

        for (int i = 0; i < 3; ++i) {
            if (((ans[i][0] == ans[i][1] && ans[i][0] == ans[i][2] && ans[i][0] == "X") 
              || (ans[0][i] == ans[1][i] && ans[0][i] == ans[2][i] && ans[0][i] == "X"))) {
                    return true;
            }
        }
        return false;

    }

    private boolean has2Won() {

        if ((ans[0][0] == ans[1][1] && ans[0][0] == ans[2][2] && ans[0][0] == "O")
         || (ans[0][2] == ans[1][1] && ans[0][2] == ans[2][0] && ans[0][2] == "O")) {
            return true;
	}

        for (int i = 0; i < 3; ++i) {
            if (((ans[i][0] == ans[i][1] && ans[i][0] == ans[i][2] && ans[i][0] == "O") 
              || (ans[0][i] == ans[1][i] && ans[0][i] == ans[2][i] && ans[0][i] == "O"))) {
                    return true;
            }
        }
        return false;
    }

    // the minimax algorithm
    public int minimax(int depth, int turn) {

        if (has1Won())
            return +1;
        if (has2Won())
            return -1;

        ArrayList<GameMove> gameMovesAvailable = getAvailableGameMoves();
        if (gameMovesAvailable.isEmpty())
            return 0;

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

        for (int i = 0; i < gameMovesAvailable.size(); ++i) {

            GameMove gameMove = gameMovesAvailable.get(i);

            if (turn == 1) { // computer
                makeGameMove(gameMove, "X");
                int currentScore = minimax(depth + 1, 2); // recursion to call player
                max = Math.max(currentScore, max);

                if (depth == 0)
                    System.out.println("info: score for position (" + gameMove.getX() + "," + gameMove.getY() + ") : " + currentScore);
                if (currentScore >= 0) {
                    if (depth == 0)
                        computersGameMove = gameMove;
                } else if (currentScore == 1) {
                    resetGameMove(gameMove);
                    break;
                }
                if (i == availableGameMoves.size() - 1 && max > 0) {
                    if (depth == 0)
                        computersGameMove = gameMove;
                }
            } else if (turn == 2) {
                makeGameMove(gameMove, "O");
                int currentScore = minimax(depth + 1, 1);
                min = Math.min(currentScore, min);
                if (min == -1) {
                    resetGameMove(gameMove);
                    break;
                }
            }
            resetGameMove(gameMove);
        }
        return (turn == 1) ? max : min;
    }

    public void resetGameMove(GameMove move) {
        ans[move.getX()][move.getY()] = "0";
    }

    public void makeGameMove(GameMove move, String player) {
        ans[move.getX()][move.getY()] = player;
        player1_turn = !player1_turn;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("done");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                MyXOGame.ans[i][j] = "0";
            }
        }
        new MyXOGame();
    }
    
}