package Projet;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

/**
 * Created by vinspi on 24/11/17.
 */
public class PanelJeu extends JPanel{

    private JLabel reponse;
    private JTextField tentative;
    private JButton tenter;
    private MotCacheClientTCP client;


    private static String parseReponse(String str){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<str.length();i++){
            sb.append(str.charAt(i));
            sb.append(' ');
        }
        return sb.toString();
    }
    /* ce panel contiendra tous les widgets nécéssaire au jeu */
    public PanelJeu() {



            reponse = new JLabel("Tentative de connexion.");
            reponse.setFont(new Font(reponse.getFont().getName(), Font.BOLD,25));

            reponse.setAlignmentX(Component.CENTER_ALIGNMENT);

            tentative = new JTextField(20);

            tenter = new JButton("tenter !");

            JPanel panelMot = new JPanel();
            panelMot.setLayout(new BoxLayout(panelMot,BoxLayout.Y_AXIS));
            panelMot.add(Box.createVerticalGlue());
            panelMot.add(reponse);
            panelMot.add(Box.createVerticalGlue());

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));




            panel.add(tentative);
            panel.add(tenter);

            this.setLayout(new BorderLayout());
            this.add(panelMot,BorderLayout.CENTER);
            this.add(panel,BorderLayout.SOUTH);






    }

    public MotCacheClientTCP getClient() {
        return client;
    }

    public JTextField getTentative() {
        return tentative;
    }

    public void tenteMot(String mot) throws IOException{
        client.tenteMot(mot);
        String rep = client.responseFromServer();
        reponse.setText(parseReponse(rep));
        tentative.setText("");
    }

    public void connect(String hostname, int port){

        try {
            client = new MotCacheClientTCP(hostname,port);
            reponse.setText(parseReponse(client.responseFromServer()));

        } catch (IOException e){
                /* si il y a une exception c'est que le serveur est injoignable */
            System.out.println("Erreur : serveur injoignable.");

            e.printStackTrace();
        }
    }

    public JButton getTenter() {
        return tenter;
    }
}
