package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
public class UserRequestDbTests {

    @MockBean
    private UserRepository uRep;

    @Test
    void testFindAll() {
        User user1 = new User(1, "name1", "name1@mail.com");
        User user2 = new User(2, "name2", "name2@mail.com");
        List<User> users = Arrays.asList(user1, user2);

        when(uRep.findAll()).thenReturn(users);

        List<User> result = uRep.findAll();
        assertEquals(users, result);

        verify(uRep, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Integer userId = 1;
        User user = new User(userId, "name1", "name1@mail.com");

        when(uRep.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = uRep.findById(userId);
        assertEquals(Optional.of(user), result);

        verify(uRep, times(1)).findById(userId);
    }

    @Test
    void testSave() {
        User user = new User(1, "name1", "name1@mail.com");

        when(uRep.save(user)).thenReturn(user);

        User result = uRep.save(user);
        assertEquals(user, result);

        verify(uRep, times(1)).save(user);
    }

    @Test
    void testDelete() {
        User user = new User(1, "name1", "name1@mail.com");

        uRep.delete(user);

        verify(uRep, times(1)).delete(user);
    }
}
