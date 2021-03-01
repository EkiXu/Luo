package xyz.eki.luo.event;

import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.eki.luo.command.CommandManager;
import xyz.eki.luo.command.EverywhereCommandInterface;
import xyz.eki.luo.utils.StringUtils;

import java.util.ArrayList;

@Component
public class MessageEvents extends SimpleListenerHost {

    private static final Logger logger = LoggerFactory.getLogger(MessageEvents.class);

    @Autowired
    private CommandManager commandConfig;


    /**
     * 从消息体中获得 用空格分割的参数
     *
     * @param msg 消息
     * @return 分割出来的参数
     */
    private ArrayList<String> getArgs(String msg) {
        String[] args = msg.trim().split(" ");
        ArrayList<String> list = new ArrayList<>();
        for (String arg : args) {
            if (StringUtils.isNotEmpty(arg)) list.add(arg);
        }
        list.remove(0);
        return list;
    }

    /**
     * 所有消息处理
     *
     * @param event 消息事件
     * @return 监听状态 详见 ListeningStatus
     * @throws Exception 可以抛出任何异常, 将在 handleException 处理
     */
    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull MessageEvent event) throws Exception {
        User sender = event.getSender();
        String oriMsg = event.getMessage().contentToString();

        logger.info("{接收到其他消息} userId:{},userNick:{},msg:{}", sender.getId(), sender.getNick(), event.getMessage().toString());

        //是否指令模式
        if (!commandConfig.isCommand(oriMsg)) {
            return ListeningStatus.LISTENING;
        }
        EverywhereCommandInterface command = (EverywhereCommandInterface) commandConfig.getCommand(oriMsg, CommandManager.everywhereCommands);

        if (command == null) {
            return ListeningStatus.LISTENING;
        }
        //执行指令并回复结果
        Message result = command.execute(sender, getArgs(oriMsg), event.getMessage(), event.getSubject());
        if (result != null) {
            event.getSubject().sendMessage(result);
        }

        return ListeningStatus.LISTENING; // 表示继续监听事件
    }
}
