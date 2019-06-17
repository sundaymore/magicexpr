package com.github.sundaymore.magicexpr.expr;


import java.util.ArrayList;
import java.util.List;

/**
 * @author chaofan
 */
public class CommandNode {

    /**
     * 命令 or 参数值
     */
    private String value;
    /**
     * 孩子结点
     */
    private List<CommandNode> children = new ArrayList<>();
    /**
     * 类型
     */
    private NodeType type;

    public CommandNode(NodeType type){
        this.type = type;
    }

    public List<CommandNode> getChildren() {
        return children;
    }

    public void setChildren(List<CommandNode> children) {
        if(!isCommand()){
            throw new IllegalStateException("只有command类型结点能添加子结点");
        }
        this.children = children;
    }

    public boolean isCommand(){
        return NodeType.command.equals(type);
    }

    public boolean isJoin(){
        return NodeType.join.equals(type);
    }

    public boolean isParam(){
        return NodeType.param.equals(type);
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static CommandNode fromParam(String param){
        CommandNode node = new CommandNode(NodeType.param);
        node.setValue(param);
        return node;
    }

    public static CommandNode joinNode(){
        CommandNode node = new CommandNode(NodeType.join);
        node.setValue("");
        return node;
    }

    enum NodeType{
        //结点类型
        command,
        param,
        join
    }
}
