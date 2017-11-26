package Projet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by vinspi on 26/11/17.
 */
public class MotCacheServerUDP extends MotCacheServer {

    private DatagramSocket server;
    private Dictionnaire dictionnaire;

    public MotCacheServerUDP(int port) {
        this.port = port;
        dictionnaire = new Dictionnaire("Mots.txt");
        this.nbEssai = 27;

        try {
            server = new DatagramSocket(port);

        }catch (SocketException e){
            e.printStackTrace();
        }

    }

    @Override
    public void startServer() throws IOException {
        gereConnexion();
    }

    @Override
    public void gereConnexion() throws IOException {


        /* dans un premier temps on va sélectionner un mot dans le dictionnaire pour le faire deviner au client */

        String mot_a_deviner = dictionnaire.getFromKey((int) (Math.random()*dictionnaire.getMaxKey()));
        System.out.println(mot_a_deviner);

        /* on attends un message de la part du client pour qu'il nous signale qu'il est prêt et donner son adresse */
        System.out.println("en attente de connection (port "+port+")");
        DatagramPacket packet = new DatagramPacket(new byte[2048],2048);
        server.receive(packet);
        System.out.println("client connecté "+packet.getAddress().toString());
        /* ce packet servira à l'envoie de données au client */
        DatagramPacket packet2;


        String tentative;
        String lettres_decouvertes;
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2;
        for (int i=0;i<mot_a_deviner.length();i++) {
            sb.append('_');

        }


        boolean fin = false;
        do{
            /* on envoie le mot */

            packet2 = new DatagramPacket(sb.toString().getBytes(),sb.toString().length(),packet.getAddress(),packet.getPort());
            server.send(packet2);

            lettres_decouvertes = sb.toString();
            /* on attends que le client nous envoie quelques chose (donc une lettre) */

            server.receive(packet);

            sb2 = new StringBuilder();
            for (int i=0;i<packet.getLength();i++){
                sb2.append((char) packet.getData()[i]);
            }
            tentative = sb2.toString();

            if(tentative.length() > 1){
                System.out.println("mot reçu : "+tentative);
                    /* si tentative > 1 on considère que c'est une tentative de découvrir le mot */
                if(tentative.equals(mot_a_deviner)){
                        /* si c'est le bon mot le joueur gagne */

                    packet2 = new DatagramPacket(new String("GG").getBytes(),2,packet.getAddress(),packet.getPort());
                    server.send(packet2);

                }
                else {
                    /* sinon il perd */
                    packet2 = new DatagramPacket(new String("GameOver").getBytes(),8,packet.getAddress(),packet.getPort());
                    server.send(packet2);


                }

                fin = true;
            }
            else {
                    /* si tentative est juste une lettre on va voir si elle se trouve dans le mot à trouver */
                System.out.println("lettre proposé : "+tentative);
                sb = new StringBuilder();
                for (int i=0;i<mot_a_deviner.length();i++){
                    if(!(lettres_decouvertes.charAt(i) == mot_a_deviner.charAt(i)) && (mot_a_deviner.charAt(i) == tentative.charAt(0))){
                            /* si cette lettre n'est pas déja découverte */
                        sb.append(tentative.charAt(0));
                    }
                    else if(lettres_decouvertes.charAt(i) == mot_a_deviner.charAt(i)){
                            /* si cette lettre était déja découverte */
                        sb.append(lettres_decouvertes.charAt(i));
                    }
                    else {
                        sb.append('_');
                    }
                }
            }

            nbEssai--;
        }while ((nbEssai > 0) && !fin);
    }

    public static void main(String[] args) {
        try {
            MotCacheServerUDP server = new MotCacheServerUDP(5001);
            server.startServer();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
