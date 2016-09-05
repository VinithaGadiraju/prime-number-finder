//Vinitha Gadiraju
 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
   public static void main(String[] args) {
      String server_ip = "";
      int port;
      try {
         server_ip = args[0];
         port = Integer.parseInt(args[1]);
      }
      catch (Exception e) {
         System.out.println("Enter server IP as a string and port as an int");
         System.out.println("Syntax: java Client <ip-address or localhost> <port_num>");
         return;
      }
      
      //Get Input from User
      Scanner scanner = new Scanner(System.in);
      String input_arr = "";
      boolean is_shutdown = false;
      while (true) {
         System.out.println("Enter a number, blank line or enter 'shutdown' to quit: ");
         String input = scanner.nextLine();
         input = input.trim();
         if (input.equals("shutdown")) {
            is_shutdown = true;
            break;
         }
         else if (input.isEmpty()) {
            break;
         }
         if (isNumber(input)) {
            input_arr += input + ", ";
         }
         else {
            System.out.println("Error: you can only enter integers");
         }
      }
      
      input_arr = input_arr.replaceAll(", $", "");
      input_arr = "[" + input_arr + "]";
      System.out.println("Sending " + input_arr + " to Server");
      
      try {
         // Connecting to the server
         System.out.println("Connecting to " + server_ip + " on port " + port);
         Socket socket = new Socket(server_ip, port);
         
         // Setting up outstream and writing data to the server 
         OutputStream out_server = socket.getOutputStream();
         DataOutputStream data_out_server = new DataOutputStream(out_server);
         data_out_server.writeUTF(input_arr);
         
         // Reading data from server 
         InputStream in_server = socket.getInputStream();
         DataInputStream data_in_server = new DataInputStream(in_server);
         System.out.println("Server replied " + data_in_server.readUTF());
         
         socket.close(); 

         // Extra Credit
         // Send shutdown signal to the server if necessary
         if (is_shutdown) {
            // Create a socket to send the message
            Socket socket1 = new Socket(server_ip, port);

            // Setup output data stream to send the shutdown message
            OutputStream out_server1 = socket1.getOutputStream();
            DataOutputStream data_out_server1 = new DataOutputStream(out_server1);

            // Write the Shutdown message to server
            data_out_server1.writeUTF("shutdown");

            System.out.println("shutdown message sent to Server");


            socket1.close();
         } 
      }
      catch (Exception e) {
         System.out.println("An error occurred during connection");
      }
      scanner.close();
   }
   
   public static boolean isNumber (String s) {
      try {
         Integer.parseInt(s);
      }
      catch (Exception e) {
         return false; 
      }
      return true;
   }

}

