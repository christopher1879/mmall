package service;
import commons.ServerResponse;
import pojo.user;
public interface uservice {

    ServerResponse<user> login(String username, String password);

    ServerResponse<String> register(user user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, user user);

    ServerResponse<user> updateInformation(user user);

    ServerResponse<user> getInformation(Integer userId);

    ServerResponse checkAdminRole(user user);

}
