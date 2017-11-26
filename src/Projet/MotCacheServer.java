package Projet;

import java.io.IOException;

/**
 * Created by vinspi on 26/11/17.
 */
public abstract class MotCacheServer {

    protected int port;
    protected int nbEssai;

    public abstract void startServer() throws IOException;
    public abstract void gereConnexion() throws IOException;

}
