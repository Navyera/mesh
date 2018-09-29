package com.linkedin.backend.conversation;

import com.linkedin.backend.connection.ConnectionService;
import com.linkedin.backend.handlers.exception.NotFriendsException;
import com.linkedin.backend.handlers.exception.ConversationNotFoundException;
import com.linkedin.backend.message.Message;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConnectionService connectionService;
    private final AppUserService appUserService;

    public ConversationService(ConversationRepository conversationRepository, ConnectionService connectionService, AppUserService appUserService) {
        this.conversationRepository = conversationRepository;
        this.connectionService = connectionService;
        this.appUserService = appUserService;
    }

    public Conversation getConversation(Integer userIdA, Integer userIdB) {
        return userIdA < userIdB ? conversationRepository.findBySmallerUser_IdAndLargerUser_Id(userIdA, userIdB) :
                                   conversationRepository.findBySmallerUser_IdAndLargerUser_Id(userIdB, userIdA);
    }

    public Conversation findConversationById(Integer id) throws ConversationNotFoundException {
        return conversationRepository.findById(id).orElseThrow(() -> new ConversationNotFoundException(id));
    }

    public void addMessage(Integer conversationId, Integer userId, String body) throws ConversationNotFoundException, UserNotFoundException {
        Conversation conversation = findConversationById(conversationId);
        AppUser sender = appUserService.findUserById(userId);

        if (!conversation.getSmallerUser().getId().equals(userId) && !conversation.getLargerUser().getId().equals(userId))
            throw new ConversationNotFoundException(userId);

        Message message = new Message();
        message.setBody(body);
        message.setSender(sender);
        message.setConversation(conversation);

        conversation.setLastUpdated(message.getTime());
        conversation.getMessages().add(message);

        conversationRepository.save(conversation);
    }

    public Conversation createConversation(Integer userIdA, Integer userIdB) throws UserNotFoundException, NotFriendsException {
        AppUser userA = appUserService.findUserById(userIdA);
        AppUser userB = appUserService.findUserById(userIdB);

        if (!connectionService.friends(userIdA, userIdB))
            throw new NotFriendsException(userIdA, userIdB);

        Conversation conversation = userIdA < userIdB ? new Conversation(userA, userB) : new Conversation(userB, userA);

        return conversationRepository.save(conversation);
    }
}
