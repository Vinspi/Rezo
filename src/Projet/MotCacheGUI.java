package Projet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by vinspi on 23/11/17.
 */
public class MotCacheGUI implements ActionListener {


    private PanelConnexion panelConnexion;
    private PanelJeu panelJeu;
    private JFrame myFrame;
    private CardLayout cardLayout;
    private JPanel panel;

    public MotCacheGUI() throws HeadlessException {

        panel = new JPanel();
        panel.setLayout(new CardLayout());
        myFrame = new JFrame();
        panelConnexion = new PanelConnexion();
        panelConnexion.getConnexion().addActionListener(this);

        cardLayout = new CardLayout();

        myFrame.setLayout(new BorderLayout());

        panel.add(panelConnexion,"connexion");



        panelJeu = new PanelJeu();
        panelJeu.getTenter().addActionListener(this);
        panel.add(panelJeu,"jeu");



        myFrame.add(panel);
        myFrame.setTitle("Client Mot Cache");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(500, 400);
        myFrame.setVisible(true);


    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getSource().equals(panelConnexion.getConnexion())){
            String adress = panelConnexion.getAdresse().getText();
            int port = Integer.parseInt(panelConnexion.getPort().getText());


            ((CardLayout) panel.getLayout()).show(panel,"jeu");
            panelJeu.connect(adress,port);

        }
        if(actionEvent.getSource().equals(panelJeu.getTenter())){

            /* on va envoyer une tentative au serveur */
            String tentative = panelJeu.getTentative().getText();
            try {
                panelJeu.tenteMot(tentative);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MotCacheGUI gui = new MotCacheGUI();
    }
}
