package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message save(Message msg) {
        return messageRepository.save(msg);
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Optional<Message> findById(Integer id) {
        return messageRepository.findById(id);
    }

    public boolean existsAccount(Integer accountId) {
        return accountRepository.existsById(accountId);
    }

    public Optional<Message> updateMessage(Integer id, String newText) {
        Optional<Message> msgOpt = messageRepository.findById(id);
        if (msgOpt.isPresent()) {
            Message msg = msgOpt.get();
            msg.setMessageText(newText);
            return Optional.of(messageRepository.save(msg));
        }
        return Optional.empty();
    }

    public boolean deleteMessageReturningIfExists(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Message> findByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
