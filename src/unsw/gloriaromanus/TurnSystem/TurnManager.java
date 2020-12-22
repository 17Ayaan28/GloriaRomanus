package unsw.gloriaromanus.TurnSystem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The TurnManager class is responsible for handling the turn system in Gloria Romanus.
 * It should be initialised at the start of a game, and every turn dependent object
 * should subscribe to it.
 */
public class TurnManager implements Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<TurnListener> listeners;
    private int currTurn;

    /**
     * Basic constructor for turn manager.
     */
    public TurnManager() {
        this.listeners = new ArrayList<TurnListener>();
        this.currTurn = 1;
    }

    /**
     * Method to add subscriber to list of subscribers.
     * @param l TurnListener, the listener to add to the lsit of subscribers.
     */
    public void subscribe(TurnListener l) {
        this.getListeners().add(l);
    }

    /**
     * Method to remove subscriber from list of subscribers.
     * @param l TurnListener, the listener to remove from the lsit of subscribers.
     */
    public void unsubscribe(TurnListener l) {
        this.getListeners().remove(l);
    }

    /**
     * Method to advance to the next turn in the game and notify
     * all subscribers that a turn has elapsed.
     */
    public void nextTurn() {

        this.incrementTurn(currTurn);

        for (TurnListener l : this.getListeners()) {
            l.update();
        }
    }

    // GETTERS AND SETTERS
    public ArrayList<TurnListener> getListeners() {
        return listeners;
    }

    public int getCurrTurn() {
        return currTurn;
    }

    public void incrementTurn(int currTurn) {
        this.currTurn += 1;
    }

}
