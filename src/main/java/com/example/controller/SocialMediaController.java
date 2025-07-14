package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    //user registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account acc) {
        if (acc.getUsername() == null || acc.getUsername().isBlank() || acc.getPassword().length() < 4) {
            return ResponseEntity.badRequest().build();
        }
        if (accountService.existsByUsername(acc.getUsername())) {
            return ResponseEntity.status(409).build(); //retrns a conflict
        }
        return ResponseEntity.ok(accountService.register(acc));
    }

    //user login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account acc) {
        return accountService.login(acc.getUsername(), acc.getPassword())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build()); //can not access
    }

    //create a message
    @PostMapping("/messages")
    public ResponseEntity<?> postMessage(@RequestBody Message msg) {
        if (msg.getMessageText() == null || msg.getMessageText().isBlank() ||
            msg.getMessageText().length() > 255 || !messageService.existsAccount(msg.getPostedBy())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(messageService.save(msg));
    }

    //gets all messages
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.findAll();
    }

    //get a message by ID
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer id) {
        return messageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build());
    }

    //deletes a message by ID
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer id) {
        boolean existed = messageService.deleteMessageReturningIfExists(id);
        return existed ? ResponseEntity.ok(1) : ResponseEntity.ok().build();
    }

    //patch a message
    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer id, @RequestBody Message body) {
        if (body.getMessageText() == null || body.getMessageText().isBlank() || body.getMessageText().length() > 255) {
            return ResponseEntity.badRequest().build();
        }
        return messageService.updateMessage(id, body.getMessageText())
                .map(updated -> ResponseEntity.ok(1))
                .orElse(ResponseEntity.badRequest().build());
    }

    //gets all messages by account ID
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable Integer accountId) {
        return messageService.findByAccountId(accountId);
    }
}