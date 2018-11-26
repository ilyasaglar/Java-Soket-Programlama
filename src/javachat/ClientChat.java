/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javachat;

/**
 *
 * @author YUNUS
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientChat implements Runnable {

    private static Socket clientSocket = null;
    private static PrintStream ps = null;
    private static DataInputStream dis = null;
    private static BufferedReader giris = null;
    private static boolean kapaliMi = false;

    public static void main(String[] args) {

        int portNo = 3333;           //port no 1000-65536 arasında olabilir
        String host = "localhost";   //server başka bir bilgisayar ise buraya server ip adresi yazılır

        if (args.length < 2) {      // Girilen host ve port numaraları ile Client açılması
            System.out.println("Port Numarası: " + portNo);
        } else {
            host = args[0];
            portNo = Integer.valueOf(args[1]).intValue();
        }

        
         
         
        try {     // Bağlantının başarılı şekilde gerçekleşmesi halinde mesaj yazma
            clientSocket = new Socket(host, portNo);
            giris = new BufferedReader(new InputStreamReader(System.in));
            ps = new PrintStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());
            System.out.println("Baglantı başarılı.");
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Bağlantı sağlanamadı");
        }

        
        
         
        if (clientSocket != null && ps != null && dis != null) {
            try {

                // Serverdan okuma için Thread, buradan tek kullanıcımız olsaydı thread kullanamdan yapılabilirdi
                new Thread(new ClientChat()).start();
                while (!kapaliMi) {
                    ps.println(giris.readLine().trim());
                }

                ps.close();
                dis.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    @Override
    public void run() {
        String cevap;
        try {
            while ((cevap = dis.readLine()) != null) {
                System.out.println(cevap);
                if (cevap.indexOf("Gule Gule") != -1) {
                    break;
                }
            }
            kapaliMi = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
