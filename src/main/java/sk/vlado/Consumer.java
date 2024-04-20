package sk.vlado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Consumer.class);
    private static final int BREAK_TIME = 500;
    private final BlockingQueue<Action> q;

    public Consumer(BlockingQueue<Action> q) {
        this.q = q;
    }

    @Override
    public void run() {
        logger.debug("Consumer thread has been started, attempting to process actions, taking {}ms cooldown", BREAK_TIME);

        EmbeddedDatabase db = new EmbeddedDatabase();

        while (true) {
            try {
                Action action = q.take();
                switch (action.type()) {
                    case ADD -> db.addUser(action.user());
                    case PRINT_ALL -> {
                        String users = String.join(", ", db.getAllUsers().stream().map(User::toString).toList());
                        logger.info("PRINT_ALL: {}", users);
                    }
                    case DELETE_ALL -> db.deleteAllUsers();
                    default -> {
                        logger.debug("Consumer thread shutting down");
                        return;
                    }
                }
                // Break time! :)
                Thread.sleep(BREAK_TIME);
            } catch (InterruptedException e) {
                logger.error(e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
