package xyz.eki.luo.command;


import xyz.eki.luo.entity.CommandProperties;

/**
 * 指令接口
 * 当接收到 指令(properties) 执行动作
 */
public interface Command {
    /**
     * 构造指令名称以及别称
     * 不区分大小写
     * @return 指令名称对象
     */
    CommandProperties properties();
}
