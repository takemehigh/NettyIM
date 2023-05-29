package wg.command;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/12 1:15 下午
 */
@Slf4j
@Data
@Component
public class ChatConsoleCommand implements Command{

    public static final String KEY = "2";

    private String in;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public void exec(Scanner scanner) {

        System.out.print("请输入内容：目标用户id@内容：");


        in = scanner.nextLine();

    }

    @Override
    public String getTip() {
        return "聊天";
    }


}
