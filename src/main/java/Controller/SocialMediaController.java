package Controller;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Dtos.AccountDto;
import Dtos.MessageDto;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register",this::registerAccount);
        app.post("login", this::login);
        app.get("messages", this::getAllMessages);
        app.post("messages", this::createMessage);
        app.get("messages/{message_id}", this::getMessageById);
        app.delete("messages/{message_id}", this::deleteMessage);
        app.get("accounts/{account_id}/messages", this::getUserMessages);
        app.patch("messages/{message_id}", this::updateMessage);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerAccount(Context ctx) {
        
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Map request body to AccountDto
            AccountDto accountDto = mapper.readValue(ctx.body(), AccountDto.class);
            // Enforce business rules
            if(accountDto.getUsername() == null || accountDto.getPassword() == null){
                ctx.status(400);
            }
            else if((accountDto.getUsername().length() == 0 || accountDto.getPassword().length() < 4) ||  accountService.accountExists(accountDto.getUsername())){
                ctx.status(400);
            }
            else {
                // Create the account
                Account newAccount = accountService.registerAccount(accountDto);
                // send the account object as json
                ctx.json(mapper.writeValueAsString(newAccount));
            }
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private void login(Context ctx) {
        try {

            ObjectMapper mapper = new ObjectMapper();

            // Map the request body to Account Dto
            AccountDto accountDto = mapper.readValue(ctx.body(), AccountDto.class);
            // Attempt the login
            Account loggedInAccount = accountService.login(accountDto);
            // if not null the login was successful
            if(loggedInAccount != null){
                ctx.json(mapper.writeValueAsString(loggedInAccount));
            }
            else {
                // The login was unsuccessful
                ctx.status(401);
            }
                
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createMessage(Context ctx){

        try {

            ObjectMapper mapper = new ObjectMapper();

            // Mapp the request body to MessageDto
            MessageDto messageDto = mapper.readValue(ctx.body(), MessageDto.class);
            
            // endforcing business rules
            if(messageDto.getMessage_text().equals("")){
                ctx.status(400);
            }
            else if(messageDto.getMessage_text().length() > 255){
                ctx.status(400);
            }
            else if(!this.accountService.accountExists(messageDto.getPosted_by())){
                ctx.status(400);
            }
            else {
                // Create the message
                Message newMessage = this.messageService.createMessage(messageDto);
                // send the created message
                ctx.json(mapper.writeValueAsString(newMessage));
            }
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getAllMessages(Context ctx){

        ctx.json(this.messageService.getAllMessages());
    }

    private void getMessageById(Context ctx){

        ObjectMapper mapper = new ObjectMapper();

        // Convert path param to int
        int id = Integer.parseInt(ctx.pathParam("message_id"));


        Message dbMessage = this.messageService.getMessageById(id);
        
        // If null means that the message was not found
        if(dbMessage == null){
            ctx.status(200);
        }
        else {

            try {
                // send the found message
                ctx.json(mapper.writeValueAsString(dbMessage));
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage()); 
            }        
        }
    }

    private void updateMessage(Context ctx){

        ObjectMapper mapper = new ObjectMapper();
       
        try {
             // Convert path param to int
            int id = Integer.parseInt(ctx.pathParam("message_id"));

            // Map the request body to message Dto
            MessageDto messageDto = mapper.readValue(ctx.body(), MessageDto.class);

            // Enforce Business rules
            if(messageDto.getMessage_text().length() == 0 || messageDto.getMessage_text().length() > 255){
                ctx.status(400);
            }
            /* 
                The update message returns the row count of the affected rows so if it is greater than one 
                the update was successful
            */ 
            else if (this.messageService.updateMessage(id,messageDto) > 0){

                // Get the updated message
                Message updatedMessage = this.messageService.getMessageById(id);

                // send the message update as json
                ctx.json(mapper.writeValueAsString(updatedMessage));
            }
            else {
                // The update was not successful
                ctx.status(400);
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteMessage(Context ctx){

        ObjectMapper mapper = new ObjectMapper();

        // Convert the path parameter to an integer
        int id = Integer.parseInt(ctx.pathParam("message_id"));

        /*
            Since the Delete in sql does not return the object we will
            get the object first so we can send it on our response
        */ 
        Message dbMessage = this.messageService.getMessageById(id);

        // if there are no messages send 200 ok like the requirements said
        if(dbMessage == null){
            ctx.status(200);
        }else{
            try {

                // call the delete message method
                this.messageService.deleteMessage(id);

                // return the message we found earlier
                ctx.json(mapper.writeValueAsString(dbMessage));

            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());   
            }        
        }
    }
    
    private void getUserMessages(Context ctx){

        ObjectMapper mapper = new ObjectMapper();
        List<Message> accountMessages;
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        try{
            if(this.accountService.accountExists(id)){
                // The account exists so we can get it's messages
                accountMessages = this.messageService.getAccountMessages(id);

                // if the List<Message> has a size of 0 return the empty list
               if(accountMessages.size() == 0){
                    ctx.json(mapper.writeValueAsString(accountMessages));
               }
               else{
                   // The list of messages has 1 or more values so will send the messages
                   ctx.json(mapper.writeValueAsString(accountMessages));
               }
           }
           else{
               // return an empty list since the account does not exist
               accountMessages = new ArrayList<>();
               ctx.json(mapper.writeValueAsString(accountMessages));
           }
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
    }

}