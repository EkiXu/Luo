package xyz.eki.luo.command.everywhere;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import xyz.eki.luo.entity.CommandProperties;
import xyz.eki.luo.sys.annotate.Command;

import java.util.ArrayList;

@Command
public class Echo extends BaseEverywhereCommand{

    @Override
    public CommandProperties properties() {
        return new CommandProperties("Echo", "echo");
    }

    @Override
    public Message execute(User sender, ArrayList<String> args, MessageChain messageChain, Contact subject) {
        String msg ="";


        if (null == args || args.size() == 0) {
            msg = "echo";
        }else {
            StringBuilder inputStr = new StringBuilder();
            for (int i = 0; i < args.size(); i++) {
                inputStr.append(args.get(i)).append(" ");
            }
            msg = inputStr.toString();
        }


        return new PlainText(msg);
    }
}
