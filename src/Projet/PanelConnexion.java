package Projet;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vinspi on 24/11/17.
 */
public class PanelConnexion extends JPanel{

    private JButton connexion;
    private JTextField adresse;
    private JTextField port;

    /* ce panel contiendra tous les widgets nécéssaire à la connexion au serveur de jeu */
    public PanelConnexion() {

        connexion = new JButton("connexion");
        connexion.setName("connect");
        adresse = new JTextField(20);
        port = new JTextField(10);

        JPanel panelInferieur = new JPanel();
        panelInferieur.setLayout(new BoxLayout(panelInferieur,BoxLayout.X_AXIS));
        panelInferieur.add(adresse);
        panelInferieur.add(new JLabel(" : "));
        panelInferieur.add(port);
        panelInferieur.add(connexion);


        JPanel panelSuperieur = new JPanel();
        panelSuperieur.setLayout(new BoxLayout(panelSuperieur,BoxLayout.Y_AXIS));
        panelSuperieur.add(Box.createVerticalGlue());
        JLabel  lab1 = new JLabel("Veuillez renseigner l'adresse");
        JLabel lab2 = new JLabel(" et le port du serveur à joindre.");
        lab1.setAlignmentX(Component.CENTER_ALIGNMENT);
        lab2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperieur.add(lab1);
        panelSuperieur.add(lab2);
        panelSuperieur.add(Box.createVerticalGlue());

        this.setLayout(new BorderLayout());

        this.add(panelSuperieur,BorderLayout.CENTER);
        this.add(panelInferieur, BorderLayout.SOUTH);

    }

    public JButton getConnexion() {
        return connexion;
    }

    public JTextField getAdresse() {
        return adresse;
    }

    public JTextField getPort() {
        return port;
    }
}
