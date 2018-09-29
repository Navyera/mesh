package com.linkedin.backend.entities.conversation;

import org.springframework.data.repository.CrudRepository;

public interface ConversationRepository extends CrudRepository<Conversation, Integer> {
    Conversation findBySmallerUser_IdAndLargerUser_Id(Integer smallerId, Integer largerId);
}
