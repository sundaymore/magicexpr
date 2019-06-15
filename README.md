# magicexpr: is an code generate engine for any string like XXXXYYYY which has no space..


## expression 

在项目中经常会碰到关于编码格式的需求，编码格式不同场景有不同的要求
比如:前缀+业务编号(如仓库id)+日期+自增序号 
magicexpr是为了解决这种具有灵活规则的编码生成问题。
magicexpr可以拼接出任意格式的编码，提供了一组内置的编码生成命令，并且可以实现自定义指令生成特殊编码段。



## 案例
在magicexpr，编码模版的模版是一个表达式，在前缀+业务编号(如仓库id)+日期+自增序号 场景中假定前缀为BC，业务编码使用门店id，则表达式的形式类似于:
```
{const BC}{time yyyyMMdd}{env shopid}{fix {seq xxx} 5}
```
上面的表达式中 const、time、env、fix、seq都是对应一个执行命令，```{const BC}```中BC是const命令的输入参数,
理论上一个指令可以有任意个参数（取决于指令的实现）。



## 表达式格式 

一个指令的格式：{指令 参数1 参数2...}，满足如下规则
- 1.每个指令都承诺返回一个字符串
- 2.指令的参数可以是另外一个指令
- 3.邻接的指令合并成一个参数*

备注：const是内置的基础指令，表达式{const ABC} 输出ABC字符串

那么对于类似表达式{const {const A}{const B}}是如何执行的。推导过程如下：
- 1.{const A} -> A 
- 2.{const B} -> B
- 3.{const A}{const B} -> AB  *这里两个命令执行结果合并成了一个参数*
- 4.{const {const A}{const B}} -> {const AB} -> AB  

对于像{cmd1 param1}{cmd2 param2}..的表达式，执行器内部会加一个const包装命令，
- {const {cmd1 param1}{cmd2 param2}}

## 内置指令

### const

常量输出指令

### time

日期格式化输出指令, 如{time yyyyMMdd}、{time yyyyMMddHHmmss}，注意该指令日期格式中间不支持带空格

### fix

按指定长度输出指令，接收3个参数，第一个参数待fix的字符串，第二个参数是指定长度，第三个参数是填充字符，默认0
- {fix ABC 5 0}，输出 00ABC
- {fix 1 5}, 输出 00001， 第三个参数可以为空

### rand_n

{rand_n 5}，输出5位的随机数字串，可能是00001，也可能是99999

### env
获取环境参数 , {env warehouseId local} or {env name global} 文末解释env



## 使用代码示例


```
MagicExpression magicExpression = MagicExpression.builder()
            .registerExecutor("seq", SeqExecutor.class).build();
magicExpression.execute("{const {const A}{const B}}")
```

## 自定义指令

指令执行器接口定义如下

```
public interface CommandExecutor {

    Result exec(Command command);

    void validate(Command command);

    void init(Environment environment);
}
```
- exec 是指令要执行的接口，Result.output是输出的内容
- validate 是校验接口，参数校验逻辑放在这里，校验不通过指令不会执行
- init，接受上下文参数


自定义指令需要实现CommandExecutor接口， 并注册到MagicExpression里面去。注册需要调用registerExecutor方法
```
MagicExpression magicExpression = MagicExpression.builder()
            .registerExecutor("seq", SeqExecutor.class).build();
magicExpression.execute("{const {const A}{const B}}")
```


## 上下文信息

magicexpr提供两种上下文环境，一个是表达式当次执行的上下文，上下文信息仅在当次执行生效，另一个是全局上下文，需要在初始化时注册信息。
全局上下文注册
```
Map<String,String> contextInfo = new HashMap();

contextInfo.put("param1","value");

MagicExpression magicExpression = MagicExpression.builder()
            .registerGlobalContextInfos(contextInfo).build();
```

LocalThread上下文在执行时作为入参传递

```
Map<String,String> contextInfo = new HashMap();
contextInfo.put("param1","value");
magicExpression.execute("{env param1}", contextInfo);
```


env指令是获取上下文信息的指令 
- 基本格式： {env $paramname $contexttype} , $contexttype有local和global两种，local是局部上下文，global是全局上下文
示例：

- {env param1 local}表示从局部上下文获取名为param1的变量， 
- {env param1 global}表示从全局上下文获取


