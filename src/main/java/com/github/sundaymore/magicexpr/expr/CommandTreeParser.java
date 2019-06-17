package com.github.sundaymore.magicexpr.expr;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author chaofan
 */
public class CommandTreeParser {
    /**
     * 左括号
     */
    private static final Character leftBrace = '{';
    /**
     * 右括号
     */
    private static final Character rightBrace = '}';

    /**
     * 默认链接字符串
     */
    private static String JOIN_MARK = "";

    public static CommandNode parseExpr(String expr){
        validateTemplate(expr);
        return parse(expr);
    }

    private static void validateTemplate(String template){
        Validate.isTrue(template.startsWith("{"), "模版格式错误");
        Validate.isTrue(template.endsWith("}"), "模版格式错误");
        Stack<Character> st = new Stack();
        for(Character c : template.toCharArray()){
            if(st.isEmpty()){
                Validate.isTrue(leftBrace.equals(c), "模版格式错误");
            }
            if(leftBrace.equals(c)){
                st.push(c);
            }else if(rightBrace.equals(c)){
                Validate.isTrue(!st.isEmpty(), "模版格式错误");
                st.pop();
            }else {
                Validate.isTrue(!st.empty(), "模版格式错误");
            }
        }
        Validate.isTrue(st.isEmpty(), "模版格式错误");
    }

    private static CommandNode parse(String expr){
        return recursiveParse(expr);
    }

    private static CommandNode recursiveParse(String expr){
        //remove outermost layer brace
        expr = expr.substring(1, expr.length() - 1).trim();
        if(StringUtils.isBlank(expr)){
            return null;
        }
        Validate.isTrue(!expr.startsWith("{"), "模版格式错误, 命令名称不能是命令");
        CommandNode commandNode = new CommandNode(CommandNode.NodeType.command);
        Pair<String,List<String>> pair = splitCmdAndParam(expr);
        commandNode.setValue(pair.getLeft());
        List<String> params = pair.getRight();
        List<CommandNode> children = params.stream().map(
                part -> part.startsWith(
                        leftBrace.toString()) ? recursiveParse(part) :
                        isJoinMark(part) ? CommandNode.joinNode() :
                        CommandNode.fromParam(part)
        ).collect(Collectors.toList());
        commandNode.setChildren(children);
        return commandNode;
    }

    private static Pair<String, List<String>> splitCmdAndParam(String expr){
        List<String> parts = new ArrayList<>();
        Stack<Character> st = new Stack<>();
        StringBuilder tmp = new StringBuilder();
        Character lastCharacter = null;
        for(Character c : expr.toCharArray()){
            if(leftBrace.equals(c)){
                if(st.isEmpty()) {
                    String tmpValue = tmp.toString();
                    if(StringUtils.isNotBlank(tmpValue)) {
                        parts.add(tmpValue);
                        //add join mark
                        parts.add(JOIN_MARK);
                        tmp = new StringBuilder(String.valueOf(c));
                    }else {
                        if(rightBrace.equals(lastCharacter)){
                            //add join mark
                            parts.add(JOIN_MARK);
                        }
                        tmp.append(c);
                    }
                }else{
                    tmp.append(c);
                }
                st.push(c);
            }else if(rightBrace.equals(c)){
                tmp.append(c);
                st.pop();
                if(st.isEmpty()){
                    String tmpValue = tmp.toString();
                    parts.add(tmpValue);
                    tmp = new StringBuilder();
                }
            }else if(Character.isSpaceChar(c) && st.isEmpty()){
                String tmpValue = tmp.toString();
                if(StringUtils.isNotBlank(tmpValue)) {
                    parts.add(tmp.toString());
                    if(rightBrace.equals(lastCharacter)){
                        //add join mark
                        parts.add(JOIN_MARK);
                    }
                    tmp = new StringBuilder();
                }
            }else {
                tmp.append(c);
            }
            lastCharacter = c;
        }
        String tmpValue = tmp.toString();
        if(StringUtils.isNotBlank(tmpValue)){
            parts.add(tmpValue);
        }
        return Pair.of(parts.get(0), parts.subList(1, parts.size()));
    }

    private static boolean isJoinMark(String part){
        return JOIN_MARK.equals(part);
    }

}
