package xyz.eki.luo.sys;

import lombok.Getter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import xyz.eki.luo.command.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AnnotateScanner implements ApplicationListener<ContextRefreshedEvent> {
    @Getter
    List<Command> commandList = new ArrayList<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 根容器为Spring容器
        //自动扫描注解为@Command的
        if (event.getApplicationContext().getParent() == null) {
            Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(xyz.eki.luo.sys.annotate.Command.class);
            for (Object bean : beans.values()) {
                if(bean instanceof Command){
                    commandList.add((Command)bean);
                }
            }
        }
    }
}
