//Vinitha Gadiraju 

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
   private ServerSocket m_server_socket;
   public Server (int port) throws IOException {
      m_server_socket = new ServerSocket(port);
      // Timeout is set to 100 seconds
      m_server_socket.setSoTimeout(100000);
   }

   public void run () {
      while(true) {
         try {
            //Create server socket and set it to listen mode 
            System.out.println("Waiting for Client connection on port " + m_server_socket.getLocalPort() + "...");
            Socket socket = m_server_socket.accept();
            System.out.println("Connected to " + socket.getRemoteSocketAddress());
            
            //Receive data from the Client 
            DataInputStream server_input_stream = new DataInputStream(socket.getInputStream());
            String input_string = server_input_stream.readUTF();
            System.out.println("Client sent " + input_string);
            
            //Process the Client input
            String output_string = "";
            if (!(input_string.equals("shutdown"))) {
               String [] string_arr = getTokens(input_string);
               for (String s: string_arr) {
                  if (isPrime(s)) {
                     output_string += s + ", ";
                  }
               }
               output_string = output_string.replaceAll(", $", "");
               output_string = "[" + output_string + "]";
               System.out.println("Sending to Client " + output_string);
            }
            // Extra Credit
            else {
               System.out.println("Shutting down Server...");
               return;
            }
            
            //Send data to the Client
            DataOutputStream server_output_stream = new DataOutputStream(socket.getOutputStream());
            server_output_stream.writeUTF(output_string);
            
            socket.close();
         } 
         catch (SocketTimeoutException e) {
            System.out.println("No Client Requests, Server timed out .... ;) ");
            return;
         }
         catch (Exception e) {
            System.out.println("Exception in Server run: " + e);
         }
      }
   }
   
   public static boolean isPrime (String s) {
      //based on http://www.mkyong.com/java/how-to-determine-a-prime-number-in-java/
      try {
         int n = Integer.parseInt(s);
         //check if n is a multiple of 2
          if (n%2==0) return false;
          //if not, then just check the odds
          for(int i=3;i*i<=n;i+=2) {
              if(n%i==0)
                  return false;
          } 
          return true;
      }
      catch (Exception e) {
         return false;
      }
   }
   
   public static String[] getTokens (String s) {
      if (s != null && s.length() > 0 && s.charAt(s.length()-1) == ']') {
         s = s.substring(0, s.length()-1);
      }
      if (s != null && s.length() > 0 && s.charAt(0) == '[') {
         s = s.substring(1, s.length());
      }
      return s.split(", ");
   }
   
   public static void main(String[] args) {
      int port;
      // Checking input
      try {
         port = Integer.parseInt(args[0]);
      }
      catch (Exception e) {
         System.out.println("Enter port as an int");
         System.out.println("Syntax: java Server <port_num>");
         return;
      }
      
      try {
         //Create a server object and set it to listen mode
         Server server = new Server(port);
         server.run();
      }
      catch (Exception e) { 
         System.out.println("Error in Server object");
      }
   }

}

