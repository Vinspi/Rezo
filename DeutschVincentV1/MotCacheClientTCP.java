package Projet;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class MotCacheClientTCP extends MotCacheClient {


	private Socket socket;
	private BufferedWriter out;
	private BufferedReader in;



	/* Création du client, qui se connecte alors au serveur. */

	public MotCacheClientTCP(String hostname, int port) throws IOException{

		socket = new Socket(hostname,port);

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public String responseFromServer() throws IOException{
		return in.readLine();
	}

	public void tenteMot(String mot) throws IOException{
		out.write(mot);
		out.newLine();
		out.flush();
	}



	public void jeu() throws IOException{


		Scanner sc = new Scanner(System.in);

		String tentative,reponse;
		System.out.println(in.readLine());
		reponse = "";
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
		socket.close();
	}


	public static void main(String[] args) {
		try {
			MotCacheClientTCP client = new MotCacheClientTCP("localhost", 5001);
			client.jeu();

		}catch (IOException e){
			e.printStackTrace();
		}
	}

}
