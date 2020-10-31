package unsw.gloriaromanus;

import java.io.IOException;

public class Spearmen extends Unit {
    
    public Spearmen(String name) throws IOException{
        super(name);
        setMovementPoints(10);
    }
}
