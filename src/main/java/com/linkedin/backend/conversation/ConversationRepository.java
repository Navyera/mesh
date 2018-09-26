package com.linkedin.backend.conversation;

import org.springframework.data.repository.CrudRepository;

public interface ConversationRepository extends CrudRepository<Conversation, Integer> {
    public Conversation findBySmallerUser_IdAndLargerUser_Id(Integer smallerId, Integer largerId);
}
