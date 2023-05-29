package wg.command;

import java.util.Scanner;

/**
 * @author gw
 * @version 1.0
 * @description: 负责输出操作提示
 * @date 2023/4/12 1:07 下午
 */
public interface Command {
    String getKey();

    //处理控制台输入
    void exec(Scanner scanner);
    //菜单说明
    String getTip();
}
