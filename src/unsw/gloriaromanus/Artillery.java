package unsw.gloriaromanus;

import java.io.IOException;

public class Artillery extends Unit {
    public Artillery(String name) throws IOException{
        super(name);
        setMovementPoints(4);
    }
}
