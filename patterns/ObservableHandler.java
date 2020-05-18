package patterns;

import java.util.ArrayList;

/*
The pattern handler exists because java doesn't allow multi extends.
All classes that need to be observable have to both implement the interface and have an ObservableHandler attribute
 */
public class ObservableHandler implements Observable {

    ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notify(Object object) {
        for(Observer observer : observers) {
            observer.update(object);
        }
    }
}
