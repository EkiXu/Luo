package xyz.eki.luo.bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.utils.BotConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.eki.luo.command.CommandManager;
import xyz.eki.luo.event.GroupEvents;
import xyz.eki.luo.event.MessageEvents;
import xyz.eki.luo.sys.AnnotateScanner;

import java.util.Arrays;
import java.util.List;

@Component
public class Luo {
    private static final Logger logger = LoggerFactory.getLogger(Luo.class);

    //账号
    @Value("${bot.account}")
    private Long botAccount;
    //密码
    @Value("${bot.pwd}")
    private String botPwd;
    //设备认证信息文件
    private static final String deviceInfo = "./cache/deviceInfo.json";

    private static Bot bot;
    public static Bot getBot() {
        return bot;
    }

    //指令相关
    @Autowired
    private CommandManager commandManager;
    @Autowired
    private AnnotateScanner annotateScanner;

    @Autowired
    private GroupEvents groupEvents;
    @Autowired
    private MessageEvents messageEvents;




    public void startBot(){
        if (null == botAccount || null == botPwd) {
            System.err.println("Empty Account");
            logger.warn("Empty Account");
        }

        bot = BotFactory.INSTANCE.newBot(botAccount, botPwd, new BotConfiguration() {
            {
                fileBasedDeviceInfo(deviceInfo);
            }
        });
        bot.login();

        commandManager.registerCommandPrefix();
        commandManager.registerCommands(annotateScanner.getCommandList());

        List<ListenerHost> events = Arrays.asList(
                messageEvents,
                groupEvents
        );
        for (ListenerHost event : events) {
            GlobalEventChannel.INSTANCE.registerListenerHost(event);
        }

    }
}
