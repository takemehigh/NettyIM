package wg.im.common.util;

public class LoggerUtil {

    public static void info(Object s){

        String content = null;
        if (null != s)
        {
            content = s.toString().trim();
        }
        else
        {
            content = "";
        }

        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        //System.out.println(stack.length);
        // 获得调用方法名
        //System.out.println(Arrays.deepToString(stack));

        String[] className = stack[2].getClassName().split("\\.");
        //System.out.println(Arrays.deepToString(className));
        String fullName = className[className.length - 1] + "." + stack[2].getMethodName();

        String cft = "[" + Thread.currentThread().getName() + "|" + fullName + "]";

        String out = String.format("%20s |>  %s ", cft, content);
        System.out.println(out);
    }

    public static void info(Object... args){

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            content.append(args[i]!=null?args[i].toString():"null");
            content.append(" ");
        }
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        //System.out.println(stack.length);
        // 获得调用方法名
        //System.out.println(Arrays.deepToString(stack));

        String[] className = stack[2].getClassName().split("\\.");
        //System.out.println(Arrays.deepToString(className));
        String fullName = className[className.length - 1] + "." + stack[2].getMethodName();
        String cft = "[" + Thread.currentThread().getName() + "|" + fullName+ "]";

        String out = String.format("%20s |>  %s ", cft, content.toString());
        System.out.println(out);
    }
   /* public static void main(String[] args) {
        log.info("123123");
    }*/
}
