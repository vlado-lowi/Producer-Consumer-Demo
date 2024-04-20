package sk.vlado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static final int Q_SIZE = 10;

    public static void main( String[] args ) {
        new App().runExample();
    }

    public void runExample() {
        logger.debug("Running Indra Avitech example");

        BlockingQueue<Action> q = new ArrayBlockingQueue<>(Q_SIZE);

        List<Action> actionList = List.of(
                new Action(ActionType.ADD, new User(1, "a1", "Robert")),
                new Action(ActionType.ADD, new User(2, "a2", "Martin")),
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.DELETE_ALL, null),
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.SHUTDOWN, null)
        );

        var producer = new Thread(new Producer(q, actionList));
        var consumer = new Thread(new Consumer(q));

        logger.debug("Starting producer thread");
        producer.start();
        logger.debug("Starting consumer thread");
        consumer.start();

        try {
            consumer.join();
            logger.debug("Consumer thread has shut down, exiting program");
        } catch (InterruptedException e) {
            logger.error(e);
            Thread.currentThread().interrupt();
        }
    }
}
