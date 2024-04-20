package sk.vlado;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

class ProducerTest {

    @Test
    void testProducerAddingActionsToQueue() {
        List<Action> actionList = List.of(
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.PRINT_ALL, null)
        );

        BlockingQueue<Action> q = new ArrayBlockingQueue<>(10);
        q.addAll(actionList);

        new Thread(new Producer(q, actionList)).start();

        Instant waitingSince = Instant.now();
        while(q.size() < actionList.size() && Duration.between(waitingSince, Instant.now()).getSeconds() < 5) {
            // Ok, if the producer doesn't add at least 1 action in 5 seconds, something ain't working...
        }

        assertThat(q.isEmpty(), is(not(true)));
    }
}