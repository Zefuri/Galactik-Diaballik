package patterns;

import java.util.ArrayList;

public interface Observable {
    ArrayList<Observer> observers = new ArrayList<>();

    void addObserver(Observer observer);

    void notify(Object object);
}
