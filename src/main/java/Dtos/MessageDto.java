package Dtos;

public class MessageDto {

    public int posted_by;
    
    public String message_text;
    
    public long time_posted_epoch;

    public MessageDto(){}

    public MessageDto(int posted_by,String message,long time_posted_epoch){
        this.posted_by = posted_by;
        this.message_text = message;
        this.time_posted_epoch = time_posted_epoch;
    }
    public int getPosted_by() {
        return posted_by;
    }
    /**
     * Properly named getters and setters are necessary for Jackson ObjectMapper to work. You may use them as well.
     * @param posted_by
     */
    public void setPosted_by(int posted_by) {
        this.posted_by = posted_by;
    }
    /**
     * Properly named getters and setters are necessary for Jackson ObjectMapper to work. You may use them as well.
     * @return message_text
     */
    public String getMessage_text() {
        return message_text;
    }
    /**
     * Properly named getters and setters are necessary for Jackson ObjectMapper to work. You may use them as well.
     * @param message_text
     */
    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }
    /**
     * Properly named getters and setters are necessary for Jackson ObjectMapper to work. You may use them as well.
     * @return time_posted_epoch
     */
    public long getTime_posted_epoch() {
        return time_posted_epoch;
    }
    /**
     * Properly named getters and setters are necessary for Jackson ObjectMapper to work. You may use them as well.
     * @param time_posted_epoch
     */
    public void setTime_posted_epoch(long time_posted_epoch) {
        this.time_posted_epoch = time_posted_epoch;
    }
}
