package Projet;

import java.io.IOException;

/**
 * Created by vinspi on 26/11/17.
 */
public abstract class MotCacheClient {

    protected String hostname;
    protected int port;

    public static final String GAME_OVER = "GameOver";
    public static final String GAME_WIN = "GG";

    public abstract String responseFromServer() throws IOException;
    public abstract void tenteMot(String mot) throws IOException;
    public abstract void jeu() throws IOException;

}
