package xyz.eki.luo.command;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import xyz.eki.luo.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommandManager {

    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);

    @Autowired
    private Environment env;

    //指令头 区分正常消息 和 指令消息


    private static Set<String> commandPrefixes = new HashSet<>();

    public static Map<String, Command> everywhereCommands = new HashMap<>();
    public static Map<String, Command> friendCommands = new HashMap<>();
    public static Map<String, Command> groupCommands = new HashMap<>();


    /**
     * 注册指令头，一般用.其他的!#都可以
     */
    public void registerCommandPrefix() {
        String test = env.getProperty("command.prefixes");
        String[] prefixes = new String[]{"#","!"};
        for (String prefix : prefixes) {
            commandPrefixes.add(prefix);
        }
    }

    /**
     * 注册指令
     *
     * @param command 一个指令
     */
    private void registerCommand(Command command) {
        //让所有指令名称指向一个指令，不区分大小写
        Map<String, Command> tempCommands = new HashMap<>();
        tempCommands.put(command.properties().getName().toLowerCase(), command);
        command.properties().getAlias().forEach(alias -> tempCommands.put(alias.toLowerCase(), command));

        //根据事件类型分配指令，方便监听程序调用
        if (command instanceof FriendCommandInterface) {
            friendCommands.putAll(tempCommands);
        } else if (command instanceof GroupCommandInterface) {
            groupCommands.putAll(tempCommands);
        } else if (command instanceof EverywhereCommandInterface) {
            everywhereCommands.putAll(tempCommands);
        }  else {
            //未配置的监听，一般不会出现，除非以后腾讯又加了新花样
            logger.warn("发现未知指令类型[{}]，忽略该指令注册", command.properties().getName());
        }
    }

    /**
     * 注册指令(批量)
     */
    public void registerCommands(List<Command> commandList) {
        if (null == commandList || commandList.size() <= 0) {
            return;
        }
        commandList.forEach(this::registerCommand);
    }


    /**
     * 判断是否是否属于指令
     *
     * @param msg 消息
     * @return 是否为指令
     */
    public boolean isCommand(String msg) {
        return commandPrefixes.stream().anyMatch(prefix -> msg.startsWith(prefix));
    }

    /**
     * 根据指令名称获取对应指令
     *
     * @param msg
     * @param commandMap
     * @return
     */
    public Command getCommand(String msg, Map<String, Command> commandMap) {
        String[] temp = msg.split(" ");
        // 带头的指令名称
        String rawCommand = temp[0];

        //去除指令头，需要考虑指令头不止一个字符的情况
        List<String> temps = commandPrefixes.stream()
                .filter(prefix -> rawCommand.startsWith(prefix) && StringUtils.isNotEmpty(prefix))
                .map(prefix -> rawCommand.replaceFirst(prefix, ""))
                .collect(Collectors.toList());

        String commandStr;
        if (temps.isEmpty()) {
            commandStr = rawCommand;
        } else {
            commandStr = temps.get(0);
        }

        return commandMap.getOrDefault(commandStr.toLowerCase(), null);
    }
}
