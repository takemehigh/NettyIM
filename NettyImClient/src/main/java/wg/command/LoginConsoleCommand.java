package wg.command;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/20 10:26 上午
 */
@Slf4j
@Data
@Component
public class LoginConsoleCommand implements Command {

    private String in;

    public final static String KEY="1";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public void exec(Scanner scanner) {
        System.out.print("请输入内容：用户名@密码：");


        in = scanner.nextLine();
    }

    @Override
    public String getTip() {
        return "登录";
    }
}
