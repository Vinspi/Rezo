package Projet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Created by vinspi on 26/11/17.
 */
public class MotCacheClientUDP extends MotCacheClient {

    private DatagramSocket client;

    public MotCacheClientUDP(String hostname, int port) throws SocketException {
        this.port = port;
        this.hostname = hostname;
        client = new DatagramSocket();
    }

    @Override
    public String responseFromServer() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[2048],2048,InetAddress.getByName(hostname),port);
        client.receive(packet);
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<packet.getLength();i++){
            sb.append((char) packet.getData()[i]);
        }
        return sb.toString();
    }

    @Override
    public void tenteMot(String mot) throws IOException {
        DatagramPacket packet = new DatagramPacket(mot.getBytes(),mot.length(),InetAddress.getByName(hostname),port);
        client.send(packet);
    }

    public void jeu() throws IOException{

        Scanner sc = new Scanner(System.in);

        String tentative,reponse;


        /* on va se manifester au serveur sous la forme d'un message de bienvenu */
        System.out.println("en attente du serveur ...");
        DatagramPacket packet = new DatagramPacket(new String("coucou").getBytes(),6,InetAddress.getByName(hostname),port);
        client.send(packet);

        /* on attends un réponse du serveur pour savoir si on peux commencer à jouer */
        reponse = responseFromServer();
        System.out.println("le mot à découvrir est : "+reponse);
        tentative = "";
        do{
            tentative = sc.nextLine();
            if (tentative.isEmpty()) {
                System.out.println("tentative null");
                continue;
            }
            System.out.println("vous tentez : "+tentative);


			/* on fait une tentative de découverte d'une lettre ou du mot */
            tenteMot(tentative);
			/* ensuite on récupère la réponse */
            reponse = responseFromServer();
			/* on affiche la réponse */
            System.out.println(reponse);

        } while(!(reponse.equals(GAME_OVER)) && !(reponse.equals(GAME_WIN)));

		/* Fin de la partie on ferme la socket */
        client.close();
    }

    public static void main(String[] args) {
        try {
            MotCacheClientUDP client = new MotCacheClientUDP("localhost",5001);
            client.jeu();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
