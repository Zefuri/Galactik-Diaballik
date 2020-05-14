package patterns;

import java.util.ArrayList;

public interface Observable {

    void addObserver(Observer observer);

    void notify(Object object);
}
