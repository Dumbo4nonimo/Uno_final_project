package org.example.eiscuno.model.observer;

/**
 * Interface for an observable object that allows observers to register, unregister,
 * and notify them of changes.
 */
public interface Observable {

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to be removed.
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all registered observers about changes in the observable object.
     */
    void notifyObservers();
}
