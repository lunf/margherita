package lunf.queen.margherita.entity;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class JmMessage {
    private long id;
    private String senderId;
    private String recipientId;
    private String text;
    private Direction direction;
    private Status status;

    public JmMessage(Direction direction, Status status) {
        this.direction = direction;
        this.status = status;
    }

    public enum Direction {
        IN_BOUND,
        OUT_BOUND;
    }

    public enum Status {
        RECEIVED,
        PROCESSING,
        PROCESSED,
        SENDING,
        SENT;
    }
}
