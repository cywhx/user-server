package whxcy.userserver.model.domain.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 6492166894000736944L;
    private  String userAccount;
    private String userPassword;
}
