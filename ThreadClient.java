import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ThreadClient extends Thread{
    Socket socket;

    public ThreadClient (Socket socket) {
        this.socket = socket;
    }

    public static void drawCircle(StringBuilder toClient,int r){
        // Consider a rectangle of size N*N
        int N = (2*r+1);

        int x, y; // Coordinates inside the rectangle

        // Draw a square of size N*N.
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                // Start from the left most corner point
                x = i-r;
                y = j-r;

                // If this point is inside the circle, print it
                if (x*x + y*y <= r*r+1 ){
                    toClient.append(".");
                }
                else{
                    // If outside the circle, print space
                    toClient.append(" ");
                }
                toClient.append(" ");
            }
            toClient.append("\n");
        }
        toClient.append("q");
        toClient.append("\n");
    }

    public void drawRectangle(StringBuilder toClient, int chieuRong, int chieuDai) {
        // vẽ chiều rộng
        for (int i = 1; i <= chieuRong; i++) {
            // vẽ chiều dài
            for (int j = 1; j <= chieuDai; j++) {
                if ((j == 1) || (j == chieuDai) || (i == 1) || (i == chieuRong)) {
                    toClient.append("* ");
                } else {
                    toClient.append("  ");
                }
            }
            toClient.append("\n");
        }
        toClient.append("q");
        toClient.append("\n");
    }

    public void drawSquare(StringBuilder toClient, int canh) {
        // vẽ chiều rộng
        for (int i = 1; i <= canh; i++) {
            // vẽ chiều dài
            for (int j = 1; j <= canh; j++) {
                if ((j == 1) || (j == canh) || (i == 1) || (i == canh)) {
                    toClient.append("* ");
                } else {
                    toClient.append("  ");
                }
            }
            toClient.append("\n");
        }
        toClient.append("q");
        toClient.append("\n");
    }

    @Override
    public void run() {
        try{
            while (true){
                DataInputStream is = new DataInputStream(socket.getInputStream());
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                String message = "";
                message = is.readUTF();
                System.out.println("Client: " + message);

                StringTokenizer stringTokenizer = new StringTokenizer(message, " ");
                ArrayList<String> arrayList = new ArrayList<>();
                while (stringTokenizer.hasMoreTokens()) {
                    String token = stringTokenizer.nextToken();
                    arrayList.add(token);
                }

                StringBuilder toClient = new StringBuilder();
                if(arrayList.get(0).equalsIgnoreCase("chunhat")){
                    drawRectangle(toClient, Integer.parseInt(arrayList.get(1)), Integer.parseInt(arrayList.get(2)));
                    os.flush();
                }

                if(arrayList.get(0).equalsIgnoreCase("vuong")){
                    drawSquare(toClient, Integer.parseInt(arrayList.get(1)));
                    os.flush();
                }

                if(arrayList.get(0).equalsIgnoreCase("tron")){
                    drawCircle(toClient, Integer.parseInt(arrayList.get(1)));
                    os.flush();
                }

                System.out.println(toClient);
                os.writeUTF(String.valueOf(toClient));
            }
        }catch (Exception e) {
            System.out.println("Erro from: " + socket.getInetAddress().getHostAddress());
        }
    }
}