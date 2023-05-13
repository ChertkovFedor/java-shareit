package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemDbTests {
    @Autowired
    private EntityManager em;
    @Autowired
    private ItemRequestService service;

    @Autowired
    private UserService uServ;
    @Autowired
    private ItemService iServ;

    @Autowired
    private ItemRepository iRep;

    UserCreationDto userCreationDto = new UserCreationDto(
            "user1",
            "user1@mail.ru");

    ItemCreationDto itemCreationDto1 = new ItemCreationDto(
            "item1",
            "description1",
            true,
            1
    );
    
    ItemCreationDto itemCreationDto2 = new ItemCreationDto(
            "item2",
            "description2",
            true,
            1
    );

    @Test
    void testCreateItem() {
        uServ.create(userCreationDto);
        iServ.create(itemCreationDto1, 1);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = 1", Item.class);
        Item item = query.getSingleResult();
        assertThat(item.getId(), equalTo(1));
        assertThat(item.getName(), equalTo(itemCreationDto1.getName()));
        assertThat(item.getDescription(), equalTo(itemCreationDto1.getDescription()));
        assertThat(item.getAvailable(), equalTo(itemCreationDto1.getAvailable()));
    }

    @Test
    void testFindItemByOwnerId() {
        uServ.create(userCreationDto);
        iServ.create(itemCreationDto1, 1);
        List<Item> item = iRep.findItemsByOwnerId(1);
        assertThat(item.size(), equalTo(1));
        assertThat(item.get(0).getId(), equalTo(1));
        assertThat(item.get(0).getName(), equalTo(itemCreationDto1.getName()));
        assertThat(item.get(0).getDescription(), equalTo(itemCreationDto1.getDescription()));
        assertThat(item.get(0).getAvailable(), equalTo(itemCreationDto1.getAvailable()));
        assertThat(item.get(0).getRequestId(), equalTo(itemCreationDto1.getRequestId()));
    }

    @Test
    void testSearch() {
        uServ.create(userCreationDto);
        iServ.create(itemCreationDto1, 1);
        iServ.create(itemCreationDto2, 1);
        List<Item> itemList = iRep.searchItemsByNameOrDescription("description");
        assertThat(itemList.size(), equalTo(2));
        itemList = iRep.searchItemsByNameOrDescription("abc");
        assertThat(itemList.size(), equalTo(0));
    }
}