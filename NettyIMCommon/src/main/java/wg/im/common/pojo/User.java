package wg.im.common.pojo;

import lombok.Data;
import wg.im.common.pojo.msg.MsgProtos;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class User {
    private final static AtomicInteger AUTO_INCREA_NO = new AtomicInteger(1);
    //用户id
    private String uid = String.valueOf(AUTO_INCREA_NO.getAndIncrement());
    //用户名称
    private String userName;

    private String passWord;

    private String deviceId;

    private Integer platform;

    private String appVersion;

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", platform=" + platform +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }

    public User fromMsg(MsgProtos.LoginRequest request){
        User user = new User();
        user.setUid(request.getUid());
        user.setUserName(request.getUserName());
        user.setDeviceId(request.getDeviceId());
        user.setPassWord(request.getPassWord());

        return user;
    }
}
