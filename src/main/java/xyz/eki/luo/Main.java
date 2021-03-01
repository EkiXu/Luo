package xyz.eki.luo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xyz.eki.luo.bot.Luo;


public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        Luo luo= context.getBean(Luo.class);
        luo.startBot();
    }


}
