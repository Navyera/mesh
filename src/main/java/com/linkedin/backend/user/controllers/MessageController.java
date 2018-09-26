package com.linkedin.backend.user.controllers;

import com.linkedin.backend.connection.NotFriendsException;
import com.linkedin.backend.conversation.Conversation;
import com.linkedin.backend.conversation.ConversationNotFound;
import com.linkedin.backend.conversation.ConversationService;
import com.linkedin.backend.dto.ActiveConversationDTO;
import com.linkedin.backend.dto.MessageDTO;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.handlers.UserNotFoundException;
import com.linkedin.backend.utils.JSONReturn;
import com.linkedin.backend.utils.JSONStatus;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/messaging")
public class MessageController {
    final private AppUserService appUserService;
    final private ConversationService conversationService;

    public MessageController(AppUserService appUserService, ConversationService conversationService) {
        this.appUserService = appUserService;
        this.conversationService = conversationService;
    }

    @GetMapping("/{id}")
    public List<MessageDTO> getConversation(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer id) throws ConversationNotFound {
        JWTUtils token = new JWTUtils(auth);
        Integer myId = token.getUserID();

        Conversation conversation = conversationService.findConversationById(id);

        if (!conversation.getSmallerUser().getId().equals(myId) && !conversation.getLargerUser().getId().equals(myId))
            throw new ConversationNotFound(id);

        return conversation.getMessages().stream().map(m -> new MessageDTO(m, myId)).collect(Collectors.toList());
    }

    @PostMapping("/{id}")
    public JSONStatus postMessage(@RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer id, @Valid @RequestBody MessageDTO message)
                                                                                     throws ConversationNotFound, UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        Integer myId = token.getUserID();

        conversationService.addMessage(id, myId, message.getBody());

        return new JSONStatus("Message committed to conversation");
    }

    @GetMapping("/initiate/{id}")
    public JSONReturn<Integer> initConversation(@RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer id)
                                                                                                   throws UserNotFoundException, NotFriendsException{
        JWTUtils token = new JWTUtils(auth);
        Integer myId = token.getUserID();

        Conversation conversation = conversationService.getConversation(myId, id);

        if (conversation == null)
            conversation = conversationService.createConversation(myId, id);

        return new JSONReturn<>(conversation.getConversationId());
    }

    @GetMapping("/list")
    public List<ActiveConversationDTO> getActiveConversations(@RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        List<ActiveConversationDTO> conversations = user.getActiveConversations();

        conversations.sort(Comparator.comparing(ActiveConversationDTO::getDate));

        return conversations;
    }
}
