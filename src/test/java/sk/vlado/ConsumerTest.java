package sk.vlado;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

class ConsumerTest {

    @Test
    void testConsumerTakingActionsFromQueue() {
        List<Action> actionList = List.of(
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.PRINT_ALL, null),
                new Action(ActionType.PRINT_ALL, null)
        );

        BlockingQueue<Action> q = new ArrayBlockingQueue<>(10);
        q.addAll(actionList);

        new Thread(new Consumer(q)).start();

        Instant waitingSince = Instant.now();
        while(!q.isEmpty() && Duration.between(waitingSince, Instant.now()).getSeconds() < 5) {
            // Ok, if the consumer doesn't take at least 1 action in 5 seconds, something ain't working...
        }

        assertThat(q.size(), is(lessThan(actionList.size())));
    }
}