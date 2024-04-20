# Producer - Consumer demo app
Simple application that can process commands from FIFO queue using Producer –
Consumer pattern. In this demo, the commands are actions on top of embedded H2 database.

## Producer
Is initialized with list of actions. When started, it attempts to add new actions into the queue.<br>
**Supported commands are the following:**
- Add  - adds a user into a database
- PrintAll – prints all users into standard output
- DeleteAll – deletes all users from database
- Shutdown - stops consumer
  
## Consumer
When started, it attempts to take action from the front of the queue and process it. The consumer will run until it is interrupted, or until action of type `ActionType.SHUTDOWN` is processed.

## Output examples
Let's take a look at example output (Std Out), when the program is started & producer is initialized with the following action sequence:
```
Add (1, "a1", "Robert")
Add (2, "a2", "Martin")
PrintAll
DeleteAll
PrintAll
Shutdown
```
output when logging is set to **DEBUG**:
```
[main] DEBUG sk.vlado.App - Running Indra Avitech example
[main] DEBUG sk.vlado.App - Starting producer thread
[main] DEBUG sk.vlado.App - Starting consumer thread
[Thread-1] DEBUG sk.vlado.runnable.Consumer - Consumer thread has been started, attempting to process actions, taking 500ms cooldown
[Thread-0] DEBUG sk.vlado.runnable.Producer - Producer thread has been started, attempting to add 6 actions into the queue, taking 250ms cooldown
[Thread-0] DEBUG sk.vlado.runnable.Producer - ADD action added to q
[Thread-1] DEBUG sk.vlado.service.EmbeddedDatabase - Inserted User{id=1, uuid='a1', name='Robert'} into database
[Thread-0] DEBUG sk.vlado.runnable.Producer - ADD action added to q
[Thread-0] DEBUG sk.vlado.runnable.Producer - PRINT_ALL action added to q
[Thread-1] DEBUG sk.vlado.service.EmbeddedDatabase - Inserted User{id=2, uuid='a2', name='Martin'} into database
[Thread-0] DEBUG sk.vlado.runnable.Producer - DELETE_ALL action added to q
[Thread-0] DEBUG sk.vlado.runnable.Producer - PRINT_ALL action added to q
[Thread-1] DEBUG sk.vlado.service.EmbeddedDatabase - Retrieved 2 users from database
[Thread-1] INFO  sk.vlado.runnable.Consumer - PRINT_ALL: User{id=1, uuid='a1', name='Robert'}, User{id=2, uuid='a2', name='Martin'}
[Thread-0] DEBUG sk.vlado.runnable.Producer - SHUTDOWN action added to q
[Thread-1] DEBUG sk.vlado.service.EmbeddedDatabase - Deleted all 2 users from database
[Thread-1] DEBUG sk.vlado.service.EmbeddedDatabase - Retrieved 0 users from database
[Thread-1] INFO  sk.vlado.runnable.Consumer - PRINT_ALL:
[Thread-1] DEBUG sk.vlado.runnable.Consumer - Consumer thread shutting down
[main] DEBUG sk.vlado.App - Consumer thread has shut down, exiting program
```
output when logging is set to **INFO**:
```
[Thread-1] INFO  sk.vlado.runnable.Consumer - PRINT_ALL: User{id=1, uuid='a1', name='Robert'}, User{id=2, uuid='a2', name='Martin'}
[Thread-1] INFO  sk.vlado.runnable.Consumer - PRINT_ALL:
```
  
