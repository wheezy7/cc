package message;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Message implements Serializable{

    public static Class<?> getMessageclass(int messagetype){
        return messageclasses.get(messagetype);
    }
    private int sequenceId;
    private int messageType;
    public abstract int getmessageType();
    public static final int LoginRequestMessage=1;
    private static final Map<Integer,Class<?>> messageclasses=new HashMap<>();
    static {

    }


}
