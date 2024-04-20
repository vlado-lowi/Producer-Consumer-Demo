package sk.vlado.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sk.vlado.model.Action;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Producer.class);
    private static final int BREAK_TIME = 250;
    private final BlockingQueue<Action> q;
    private final List<Action> actionList;


    public Producer(BlockingQueue<Action> q, List<Action> actionList) {
        this.q = q;
        this.actionList = actionList;
    }

    @Override
    public void run() {
        logger.debug("Producer thread has been started, attempting to add {} actions into the queue, taking {}ms cooldown",
                actionList.size(), BREAK_TIME);

        for (Action action : actionList) {
            try {
                q.put(action);
                logger.debug("{} action added to q", action.type().name());
                // let's take a short break from producing
                Thread.sleep(BREAK_TIME);
            } catch (InterruptedException e) {
                logger.error(e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
