package message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class loginmessage extends Message {
    private String username;
    private String passworld;
    private String nickname;
    @Override
    public int getmessageType() {
        return LoginRequestMessage;
    }
}
