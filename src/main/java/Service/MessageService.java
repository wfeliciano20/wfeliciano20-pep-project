package Service;
import Model.Message;

import java.util.List;

import DAO.MessageDao;
import Dtos.MessageDto;

public class MessageService {
    private MessageDao messageDao;

    public MessageService(){
        this.messageDao = new MessageDao();
    }

    public Message createMessage(MessageDto messageDto){
        return this.messageDao.createMessage(messageDto);
    }

    public List<Message> getAllMessages(){
        return this.messageDao.getAllMessages();
    }

    public Message getMessageById(int id){
        return this.messageDao.getMessageById(id);
    }

    public int deleteMessage(int id){
        return this.messageDao.DeleteMessage(id);
    }

    public List<Message> getAccountMessages(int id) {
        
        return this.messageDao.getAccountMessages(id);
    }

    public int updateMessage(int id, MessageDto messageDto) {
       return this.messageDao.updateMessage(id,messageDto);
    }
}
