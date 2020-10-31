package unsw.gloriaromanus;

import java.io.IOException;

public class HorseArchers extends Unit {
    
    public HorseArchers(String name) throws IOException{
        super(name);
        setMovementPoints(15);
    }
}
