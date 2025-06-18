package common.managers.auth;

import common.data.auth.AuthCredentials;

public interface UserManager {
    Integer authenticate(AuthCredentials auth);
    Integer register(AuthCredentials auth);
    String getUsernameById(int userId);
}
