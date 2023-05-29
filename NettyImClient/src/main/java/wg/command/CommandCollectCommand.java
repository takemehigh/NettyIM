package wg.command;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author gw
 * @version 1.0
 * @description: TODO
 * @date 2023/4/20 10:35 上午
 */
@Slf4j
@Data
@Component
public class CommandCollectCommand implements Command{

    String in;

    public static final String KEY = "1";

    Map commandMap;

    String commandShow;

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public void exec(Scanner scanner) {
        System.out.print("请输入要进行的操作："+commandShow);
        String in = scanner.next();
    }

    @Override
    public String getTip() {
        return "show all comands";
    }

    public void initComand(Map<String,Command> commandMap){
        StringBuilder commandBuilder = new StringBuilder();
        Set<Map.Entry<String,Command>> set=commandMap.entrySet();
        commandBuilder.append("菜单->");
        for (Map.Entry<String,Command> e:set){
            String key=e.getKey();
            Command command = e.getValue();
            String tip = command.getTip();
            commandBuilder.append(key+":"+tip);
            commandBuilder.append("|");
        }
        commandShow = commandBuilder.toString();
    }

}
