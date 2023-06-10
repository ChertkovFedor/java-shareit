package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemCreationDto itemDto, Integer ownerId) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> findItemDtoById(Integer itemId, Integer userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> update(Integer ownerId, ItemCreationDto itemDto, Integer itemId) {
        return patch("/" + itemId, ownerId, itemDto);
    }

    public void deleteItemById(Integer itemId) {
        delete("/" + itemId);
    }

    public ResponseEntity<Object> findAll() {
        return get("/all");
    }

    public ResponseEntity<Object> findAllItemDtoByUser(Integer ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> searchAllItemDtoByNameOrDescription(String text, Integer userId) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(CommentCreateDto commentDto, Integer bookerId, Integer itemId) {
        return post("/" + itemId + "/comment", bookerId, commentDto);
    }
}
