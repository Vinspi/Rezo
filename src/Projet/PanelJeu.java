package Projet;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * Created by vinspi on 24/11/17.
 */
public class PanelJeu extends JPanel implements KeyListener{

    private JLabel reponse;
    private JTextField tentative;
    private JButton tenter;
    private MotCacheClientTCP client;
    private JLabel essai_restant;


    private static String parseReponse(String str){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<str.length();i++){
            if(str.charAt(i) == ' ')
                break;
            sb.append(str.charAt(i));
            sb.append(' ');
        }
        return sb.toString();
    }


    private static String parseEssai(String str){
        StringBuilder sb = new StringBuilder();
        int i;
        for (i=0;i<str.length();i++){
            if(str.charAt(i) == ' ')
                break;
        }
        for (int j = i+1;j<str.length();j++){
            sb.append(str.charAt(j));
        }
        return sb.toString();
    }

    /* ce panel contiendra tous les widgets nécéssaire au jeu */
    public PanelJeu() {

            essai_restant = new JLabel("");
            reponse = new JLabel("Tentative de connexion.");
            reponse.setFont(new Font(reponse.getFont().getName(), Font.BOLD,25));

            reponse.setAlignmentX(Component.CENTER_ALIGNMENT);

            tentative = new JTextField(20);
            tentative.addKeyListener(this);
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
            this.add(essai_restant,BorderLayout.NORTH);




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
        essai_restant.setText(parseEssai(rep));
        tentative.setText("");
    }

    public void connect(String hostname, int port){

        try {
            client = new MotCacheClientTCP(hostname,port);
            String rep = client.responseFromServer();
            reponse.setText(parseReponse(rep));
            essai_restant.setText(parseEssai(rep));

        } catch (IOException e){
                /* si il y a une exception c'est que le serveur est injoignable */
            System.out.println("Erreur : serveur injoignable.");

            e.printStackTrace();
        }
    }

    public JButton getTenter() {
        return tenter;
    }



    @Override
    public void keyPressed(KeyEvent keyEvent) {

        System.out.println(keyEvent.getKeyCode());

        if (keyEvent.getKeyCode() == 10){
            if (!tentative.getText().isEmpty())
                try {
                    tenteMot(tentative.getText());
                    tentative.setText("");
                }catch (IOException e){
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
