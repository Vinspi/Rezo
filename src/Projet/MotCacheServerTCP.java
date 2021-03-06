package Projet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by vinspi on 23/11/17.
 */
public class MotCacheServerTCP extends MotCacheServer {

    private ServerSocket service;
    private Socket sock;
    private Dictionnaire dictionnaire;


    public MotCacheServerTCP(int port) throws IOException {
        this.port = port;
        this.service = new ServerSocket(5001);
        this.nbEssai = 27;
        dictionnaire = new Dictionnaire("Mots.txt");

    }

    public void startServer() throws IOException{
        /* ici on va attendre une connection sur la socket de service */
        /* comme on ne gere qu'un seul client on va attendre et accepter une connexion uniquement si la socket est libre */


        System.out.println("En attente de connexion ("+port+")");
        sock = service.accept();
        /* ici on va créer un thread qui va gérer la connection avec le client */
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    gereConnexion();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        t.start();

        startServer();
    }

    public void gereConnexion() throws IOException{
            Socket socket = sock; /* ici on créé une copie du pointeur de la socket car celle-ci est propre à chaque thread */
            System.out.println("Un client se connecte.");
            /* dans cette fonction on va gerer la connexion avec la socket du client */
            /* c'est donc ici que va se dérouler la logique du jeu */

            /* dans un premier temps on va sélectionner un mot dans le dictionnaire pour le faire deviner au client */

            String mot_a_deviner = dictionnaire.getFromKey((int) (Math.random()*dictionnaire.getMaxKey()));
            System.out.println(mot_a_deviner);
            int NB_ESSAI = 2*mot_a_deviner.length();
            /* ici on récupère les flux d'entrée et de sortie de la socket du client */

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            char c;
            String tentative;
            String lettres_decouvertes;
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<mot_a_deviner.length();i++) {
                sb.append('_');
            }

            int lettre_a_decouvrir = mot_a_deviner.length();
            boolean fin = false;
            do{

                /* on envoie le mot */

                sb.append(" essai(s) restant(s) "+NB_ESSAI);

                bw.write(sb.toString());
                bw.newLine();
                bw.flush();
                lettres_decouvertes = sb.toString();

                /* on attends que le client nous envoie quelques chose (donc une lettre) */


                try {
                    /* si la tentative de lecture échoue c'est que le clinet a fermé la connection */
                    tentative = br.readLine().toUpperCase();
                }catch (NullPointerException e){
                    System.out.println("Connection fermé par le client, arrêt du thread.");
                    break;
                }
                if(tentative.length() > 1){
                    System.out.println("mot reçu : "+tentative);
                    /* si tentative > 1 on considère que c'est une tentative de découvrir le mot */
                    if(tentative.equals(mot_a_deviner)){
                        /* si c'est le bon mot le joueur gagne */
                        bw.write("GG");

                    }
                    else {
                        /* sinon il perd */
                        bw.write("GameOver");
                    }
                    bw.newLine();
                    bw.flush();

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

                    /* si toutes les lettres on été découvertes on a gagné */
                    if(sb.toString().equals(mot_a_deviner)){
                        bw.write("GG");
                        bw.newLine();
                        bw.flush();
                    }
                }

                NB_ESSAI--;
                if(NB_ESSAI == 0) {
                    bw.write("GameOver");
                }
            }while ((NB_ESSAI > 0) && !fin);
            bw.close();
            br.close();
            socket.close();
    }


    public static void main(String[] args) {
        try {
            MotCacheServerTCP server = new MotCacheServerTCP(5001);
            server.startServer();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
