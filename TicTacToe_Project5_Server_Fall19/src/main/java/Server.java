import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.*;


import javafx.application.Platform;
import javafx.scene.control.ListView;


public class Server{

    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;
    int port;
    ServerSocket mySocket;
    FindNextMove findNextMove;

    Server(Consumer<Serializable> call){

        callback = call;
        server = new TheServer();
        server.start();
        findNextMove = new FindNextMove();
    }

    //constructor for testing
    Server(int port){
        System.out.println("Testing constructor");
        this.port = port;
        callback = new Consumer<Serializable>() {
            @Override
            public void accept(Serializable serializable) {

            }
        };
        server = new TheServer();
        server.start();
        findNextMove = new FindNextMove();
    }


    public class TheServer extends Thread{

        public void run() {
            try(ServerSocket mysocket = new ServerSocket(port);){
                callback.accept("Server is waiting for a client!");
                System.out.println("Server is waiting for a client!");
                mySocket = mysocket;

                while(true) {
                    System.out.println("==================================== Test1");

                    ClientThread c = new ClientThread(mySocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c);
                    c.start();
                    System.out.println("A new client #" + count + " connected to the server");
                    count++;

                } //end of while
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }

        } //end run
    } //end TheServer


    class ClientThread extends Thread{


        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;
        Game game;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
            game = new Game();
            game.info.myCount = this.count;
        }

        public synchronized void run(){
            System.out.println("==================================== Test2");
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            //first time
            updateThisClient();

            while(true) {
                try {
                    //read stream
                    GameInfo data = (GameInfo) in.readObject();
                    System.out.println("** Server read stream: GameInfo from client #" + count);
                    data.printElems();

                    /*
                        Cases to deal with
                        1. challenge request
                        2. PlayAgain request
                        3. In-game
                     */

                    //if challenge request
                    if(data.challengeRequest) {
                        //set difficulty
                        game.info.difficulty = data.difficulty;

                        //switch isInGame;
                        game.isInGame = true;

                        //send info
                        updateThisClient();
                    }
                    //if playAgain request
                    else if(data.playAgainRequest) {
                        //switch isInGame;
                        game.isInGame = false;

                        //send info
                        updateThisClient();
                    }
                    //if play move request
                    else if (data.playMoveRequest) {
                        //get move
                        int clientMove = data.move;

                        //validate move
                        if (game.isValidMove(clientMove)) {
                            //update board
                            game.info.board.set(clientMove, 0);

                            //check if the game is won
                            game.info.gameWon = game.isGameWon();

                            if(game.info.gameWon) {
                                //send info
                                updateThisClient();
                                System.out.println("Server1: game.info.gameWon" + game.info.gameWon);

                            } else {
                                //get a server move
                                System.out.println("=========== getServerMove() is called in run");
                                int serverMove = findNextMove.getServerMove(game.info.board, game.info.difficulty);

                                //update board
                                game.info.board.set(serverMove, 1);

                                //check if game is won
                                game.info.gameWon = game.isGameWon();

                                //send info
                                updateThisClient();

                                System.out.println("Server2: game.info.gameWon" + game.info.gameWon);
                            }

                            //if game won
                            if(game.info.gameWon) {
                                System.out.println("update all executes");
                                //update all clients with new score
                                updateTopScores();

                                //reset game info
                                game.info.reset();
                            }

                        } else {
                            //do nothing
                            //send info
                            updateThisClient();
                        }
                    }
                } //end try
                catch(Exception e) {
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    try {
                        this.in.close();
                        this.out.close();
                        this.connection.close();
                        System.out.println("client #" + count + " socket and I/O streams closed.");
                    } catch (Exception e2) {
                        System.out.println("client #" + count + " socket and I/O streams weren't closed.");
                    }
                    clients.remove(this);
                    break;
                } //end catch
            } //end while
        }//end of run

        //updates only a single client
        public void updateThisClient() {
            //set Game Info's instance to have top scores
            this.game.info.scores = getTopScores();

            //debugging
            System.out.println("** Server updateThisClient(): GameInfo from client #" + count);
            game.info.printElems();

            try {
                this.out.reset();
                this.out.writeObject(game.info);
                this.out.flush();
            } catch (Exception e) {
                System.out.println("Couldn't updateThisClient() to client #" + this.count);
            }
        }

        //updates all clients that aren't in a game
        public void updateTopScores() {
            System.out.println("update all executes1");
            //get top three scores
            ArrayList<Integer> topScores = getTopScores();

            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                System.out.println("update all executes2: client " + t.count + " isInGame? " + t.game.isInGame);
                //if isn't in game
                if(!t.game.isInGame) {
                    //set Game Info's instance to have top scores
                    t.game.info.scores = topScores;

                    //debugging
                    System.out.println("** Server updateAllClients(): GameInfo from client #" + count);
                    game.info.printElems();

                    //send attempt
                    try {
                        t.out.reset();
                        t.out.writeObject(t.game.info);
                        t.out.flush();
                    }
                    catch(Exception e) {
                        System.out.println("Couldn't updateTopScores() to client #" + this.count);
                    }
                }
            }
        }

    }//end of client thread

    /*
        Game class: contains game board and game logic. Uses FindNextMove for next move.

        Board:
        empty = -1
        O = 0
        X = 1
     */
    class Game {
        GameInfo info;
        boolean isInGame;

        public Game() {
            info = new GameInfo();
            isInGame = false;
        }

        //checks if the requested move is valid
        public boolean isValidMove(int move) {
            if(info.board.get(move) == -1) {
                return true;
            }
            return false;
        }

        public boolean isGameWon() {
            ArrayList<Integer> board = info.board;

            ArrayList<Integer> horz1, horz2, horz3, vert1, vert2, vert3, diag1, diag2;
            horz1 = new ArrayList<>();
            horz2 = new ArrayList<>();
            horz3 = new ArrayList<>();
            vert1 = new ArrayList<>();
            vert2 = new ArrayList<>();
            vert3 = new ArrayList<>();
            diag1 = new ArrayList<>();
            diag2 = new ArrayList<>();
            Collections.addAll(horz1, 0, 1, 2);
            Collections.addAll(horz2, 3, 4, 5);
            Collections.addAll(horz3, 6, 7, 8);
            Collections.addAll(vert1, 0, 3, 6);
            Collections.addAll(vert2, 1, 4, 7);
            Collections.addAll(vert3, 2, 5, 8);
            Collections.addAll(diag1, 0, 4, 8);
            Collections.addAll(diag2, 2, 4, 6);


            boolean isWon = false;
            String winner;
            int p1count = 0;
            int p2count = 0;
            //check horizontal --------------------
            // horz1
            for (int i=0; i<horz1.size(); i++){
                if (board.get(horz1.get(i)) == 0){
                    p1count++;
                }
                if (board.get(horz1.get(i)) == 1){
                    p2count++;
                }
            }

            // horz2
            if (p1count != 3 && p2count != 3){
                p1count = 0;
                p2count = 0;
                for (int i=0; i<horz2.size(); i++){
                    if (board.get(horz2.get(i)) == 0){
                        p1count++;
                    }
                    if (board.get(horz2.get(i)) == 1){
                        p2count++;
                    }
                }
            }

            // horz3
            if (p1count != 3 && p2count != 3){
                p1count = 0;
                p2count = 0;
                for (int i=0; i<horz3.size(); i++){
                    if (board.get(horz3.get(i)) == 0){
                        p1count++;
                    }
                    if (board.get(horz3.get(i)) == 1){
                        p2count++;
                    }
                }
            }


            //check vertical -------------------------------
            //vert1
            if (p1count != 3 && p2count != 3){
                p1count = 0;
                p2count = 0;
                for (int i=0; i<vert1.size(); i++){
                    if (board.get(vert1.get(i)) == 0){
                        p1count++;
                    }
                    if (board.get(vert1.get(i)) == 1){
                        p2count++;
                    }
                }
            }

            //vert2
            if (p1count != 3 && p2count != 3){
                p1count = 0;
                p2count = 0;
                for (int i=0; i<vert2.size(); i++){
                    if (board.get(vert2.get(i)) == 0){
                        p1count++;
                    }
                    if (board.get(vert2.get(i)) == 1){
                        p2count++;
                    }
                }
            }

            // vert3
            if (p1count != 3 && p2count != 3){
                p1count = 0;
                p2count = 0;
                for (int i=0; i<vert3.size(); i++){
                    if (board.get(vert3.get(i)) == 0){
                        p1count++;
                    }
                    if (board.get(vert3.get(i)) == 1){
                        p2count++;
                    }
                }
            }

            //check diagonal-------------------------------
            // diag1
            if (p1count != 3 && p2count != 3){
                p1count = 0;
                p2count = 0;
                for (int i=0; i<diag1.size(); i++){
                    if (board.get(diag1.get(i)) == 0){
                        p1count++;
                    }
                    if (board.get(diag1.get(i)) == 1){
                        p2count++;
                    }
                }
            }

            //diag2
            if (p1count != 3 && p2count != 3){
                p1count = 0;
                p2count = 0;
                for (int i=0; i<diag2.size(); i++){
                    if (board.get(diag2.get(i)) == 0){
                        p1count++;
                    }
                    if (board.get(diag2.get(i)) == 1){
                        p2count++;
                    }
                }
            }

            if (p1count == 3){
                winner = "Player";
                info.score++;
                info.winner = winner;
                callback.accept("client #" + info.myCount + " WON and has " + info.score + " points");
                return true;
            }
            if (p2count == 3){
                winner = "Server";
                info.winner = winner;
                callback.accept("client #" + info.myCount + " Lost and has " + info.score + " points");
                //callback.accept("Client: " +  clientNumber + " Lost and has " + info.score + " points");
                return true;
            }

            //identify a draw
            int count = 0;
            for(int i = 0; i < board.size(); i++) {
                if(board.get(i) != -1) {
                    count++;
                }
            }

            if(count == 9){
                winner = "Draw";
                info.winner = winner;
                callback.accept("client #" + info.myCount + " has drawn against the server. Current score is " + info.score);
                return true;
            }

            return false;
        }
    }

    /*
        Finds the next move for the server using Minimax Algorithm. Assumes that the server always plays X, and client always plays Y
     */
    class FindNextMove {
        AI_MinMax minMax;
        ArrayList<Node> movesList;
        ArrayList<Node> movesListWinning;
        ArrayList<Node> movesListDraw;
        ArrayList<Node> movesListLosing;

        FindNextMove () {
            movesList = new ArrayList<Node>();
            movesListWinning = new ArrayList<Node>();
            movesListDraw = new ArrayList<Node>();
            movesListLosing = new ArrayList<Node>();
        }

        public void getAndSortMovesLists(ArrayList<Integer> board) {
            //clear all lists
            movesList.clear();
            movesListWinning.clear();
            movesListDraw.clear();
            movesListLosing.clear();

            //convert board into the compatible format
            String[] newBoard = convertBoard(board);

            //init minMax with compatible board
            minMax = new AI_MinMax(newBoard);

            //get list of moves
            movesList = minMax.getMovesList();

            //get winning moves
            for(int i = 0; i < movesList.size(); i++) {
                if(movesList.get(i).getMinMax() == 10) {
                    movesListWinning.add(movesList.get(i));
                }
            }

            //get draw moves
            for(int i = 0; i < movesList.size(); i++) {
                if(movesList.get(i).getMinMax() == 0) {
                    movesListDraw.add(movesList.get(i));
                }
            }

            //get losing moves
            for(int i = 0; i < movesList.size(); i++) {
                if(movesList.get(i).getMinMax() == -10) {
                    movesListLosing.add(movesList.get(i));
                }
            }

        }

        //Gets the next server move based on an input of board and the difficulty
        public int getServerMove(ArrayList<Integer> board, String difficulty) {
            getAndSortMovesLists(board);
            int move = 0;
            //if expert
            if (difficulty.equalsIgnoreCase("expert")) {
                //debugging
                System.out.println("==========getServerMove(): " + difficulty);
                printMovesList();

                // get first winning move
                if (movesListWinning.size() != 0){
                    move = movesListWinning.get(0).getMovedTo();
                }
                // if not get draw
                else if (movesListDraw.size() != 0){
                    move = movesListDraw.get(0).getMovedTo();
                }
                //else lose.

                else{
                    move = movesListLosing.get(0).getMovedTo();
                }
            }
            //if medium
            else if (difficulty.equalsIgnoreCase("medium")) {
                //debugging
                System.out.println("==========getServerMove(): " + difficulty);
                printMovesList();
                //expert but 50 percent random
                // if not .5 then random from moveslist
                if (Math.random() < 0.5){
                    ArrayList<Node> copyList = movesList;
                    Collections.shuffle(copyList);
                    move = copyList.get(0).getMovedTo();
                }
                else {
                    if (movesListWinning.size() != 0){
                        move = movesListWinning.get(0).getMovedTo();
                    }
                    // if not get draw
                    else if (movesListDraw.size() != 0){
                        move = movesListDraw.get(0).getMovedTo();
                    }
                    //else lose
                    else{
                        move = movesListLosing.get(0).getMovedTo();
                    }
                }
            }
            //if easy
            else{
                //debugging
                System.out.println("==========getServerMove(): " + difficulty);
                printMovesList();

                //100% random
                ArrayList<Node> copyList = movesList;
                Collections.shuffle(copyList);
                move = copyList.get(0).getMovedTo();
            }

            //debugging
            System.out.println("==========getServerMove(): returned " + (move-1));
            return move-1;

        }

        //prints moves list if exists
        public void printMovesList() {

            if(movesList != null) {
                System.out.print("Server.findNextMove: movesList:");
                for(int i = 0; i < movesList.size(); i++) {
                    System.out.print((movesList.get(i).getMovedTo()-1) + " ");
                }
                System.out.println("");
                System.out.print("Server.findNextMove:   minimax:");
                for(int i = 0; i < movesList.size(); i++) {
                    System.out.print(movesList.get(i).getMinMax() + " ");
                }
                System.out.println("");
            }
            else {
                System.out.println("Server.findNextMove: movesList is empty");
            }

            if(movesListWinning != null) {
                System.out.print("Server.findNextMove: movesListWinning:");
                for(int i = 0; i < movesListWinning.size(); i++) {
                    System.out.print((movesListWinning.get(i).getMovedTo()-1) + " ");
                }
                System.out.println("");
                System.out.print("Server.findNextMove:   minimax:");
                for(int i = 0; i < movesListWinning.size(); i++) {
                    System.out.print(movesListWinning.get(i).getMinMax() + " ");
                }
                System.out.println("");
            }
            else {
                System.out.println("Server.findNextMove: movesListWinning is empty");
            }

            if(movesListDraw != null) {
                System.out.print("Server.findNextMove: movesListDraw:");
                for(int i = 0; i < movesListDraw.size(); i++) {
                    System.out.print((movesListDraw.get(i).getMovedTo()-1) + " ");
                }
                System.out.println("");
                System.out.print("Server.findNextMove:   minimax:");
                for(int i = 0; i < movesListDraw.size(); i++) {
                    System.out.print(movesListDraw.get(i).getMinMax() + " ");
                }
                System.out.println("");
            }
            else {
                System.out.println("Server.findNextMove: movesListDraw is empty");
            }

            if(movesListLosing != null) {
                System.out.print("Server.findNextMove: movesListLosing:");
                for(int i = 0; i < movesListLosing.size(); i++) {
                    System.out.print((movesListLosing.get(i).getMovedTo()-1) + " ");
                }
                System.out.println("");
                System.out.print("Server.findNextMove:   minimax:");
                for(int i = 0; i < movesListLosing.size(); i++) {
                    System.out.print(movesListLosing.get(i).getMinMax() + " ");
                }
                System.out.println("");
            }
            else {
                System.out.println("Server.findNextMove: movesListLosing is empty");
            }
        }
        public String[] convertBoard(ArrayList<Integer> oldBoard) {
            //init String[]
            String[] newBoard = new String[9];

            //loop through old board, and create a new board
            for(int i = 0; i < oldBoard.size(); i++) {
                if(oldBoard.get(i) == -1) {
                    newBoard[i] = "b";
                }
                else if(oldBoard.get(i) == 0) {
                    newBoard[i] = "O";
                }
                else if(oldBoard.get(i) == 1) {
                    newBoard[i] = "X";
                }
            }

            //return newBoard
            return newBoard;
        }
    }

    //Returns an ArrayList<Integer> of top 3 scores
    private ArrayList<Integer> getTopScores() {
        ArrayList<Integer> allScores = new ArrayList<Integer>();

        //get all scores
        for(int i = 0; i < clients.size(); i++) {
                allScores.add(clients.get(i).game.info.score);
        }

        //sort all scores
        Collections.sort(allScores);

        ArrayList<Integer> topScores = new ArrayList<Integer>();
        topScores.add(0);
        topScores.add(0);
        topScores.add(0);
        
        //Debugging
        //System.out.println("Printing all scores: ");
        //printArrayListScores(allScores);

        //get top 3
        int j =0;
        for(int i = allScores.size()-1; i > allScores.size()-4 && i >= 0; i--, j++) {
            topScores.set(j, allScores.get(i));
        }

        //Debugging
        //System.out.println("Printing top scores: ");
        //printArrayListScores(topScores);

        return topScores;
    }

    private void printArrayListScores(ArrayList<Integer> scores) {
        for(int i = 0; i < scores.size(); i++) {
            System.out.print(scores.get(i) + " ");
        }
        System.out.println(".");

    }
}