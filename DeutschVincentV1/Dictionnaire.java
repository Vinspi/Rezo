package Projet;

import java.io.*;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by vinspi on 24/11/17.
 */
public class Dictionnaire {
    private Hashtable<Integer,String> dico;
    private int maxKey;

    public int getMaxKey() {
        return maxKey;
    }

    public Dictionnaire(String filename) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            dico = new Hashtable<>();
            String tmp;
            int key = 0;
            while ((tmp=in.readLine()) != null){
                dico.put(key,tmp);
                key++;
            }

            this.maxKey = key;

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getFromKey(int key){
        return dico.get(key);
    }
}
