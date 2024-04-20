package sk.vlado;


import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmbeddedDatabaseTest {
    private EmbeddedDatabase db;

    @BeforeAll
    void beforeAll() {
        db = new EmbeddedDatabase();
    }

    @Test
    @Order(1)
    void testAddUser() {
        boolean result = db.addUser(new User(1, "a1", "Robert"));
        assertThat(result, is(true));
    }

    @Test
    @Order(2)
    void testGetAllUsers() {
        List<User> users = db.getAllUsers();
        assertThat(users.size(), is(1));
        assertThat(users.get(0).id(), is(1));
    }

    @Test
    @Order(3)
    void testDeleteAllUsers() {
        int deleted = db.deleteAllUsers();
        assertThat(deleted, is(1));
    }

    @Test
    @Order(4)
    void testDeleteAllUsers2() {
        int deleted = db.deleteAllUsers();
        assertThat(deleted, is(0));
    }

    @Test
    @Order(5)
    void testGetAllUserIsEmpty() {
        List<User> users = db.getAllUsers();
        assertThat(users.isEmpty(), is(true));
    }

}