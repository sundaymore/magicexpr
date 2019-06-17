# magicexpr

## introduce
magicexpr is an code generation engine for any format code, by using a group of command executor.

## expression 

Actually, an expression in magicexpr is just a command that promise to return a string result. and there is a group of engine inner commands as basic command set, and you can expand the command set if necessary.  

let's see a simple expression: 
  ```{const helloworld}```    
output: 
  ```helloworld```  

the word "const" is command name, "helloworld" is first param of "const". the function of const command is just return the first param  

## principle
### three base rule
There is three base rules that defines the workflow of code generation engine and make it completeness  

- 1. the code engine can only receive one command as input expression
- 2. the command contain several params, maybe one, maybe zero, or maybe more
- 3. a param can either be as a string type, or command type.

#### for example
the third rule means that we can construct a expression like  
  ```{cmd1 {cmd2 helloworld}}```  

the inner expression ```{cmd2 helloworld}``` is param of the “cmd1” command.  

#### go further 
we can make it go further, to show a more complex expression:  
  ```{const {const hello}{const world}} ```  


additionally, if there is no outermost brace, such like   

  ```{const o}{const k}```  


the engine will wrap it by const command  
  ```{const {const o}{const k}}```



## get start


### use example
```
MagicExpression magicExpression = MagicExpression.builder().build();
magicExpression.execute("{const A}")
```  

### inner command
#### const

const command return the value of its first param  
{const helloworld}  >> helloworld

#### fix

fix command can fix a string lenth to specified size  
format {fix $str $size $fill}  

{fix 1 5 0}  >> 00001  
{fix kfc 10 fuck}  >> uckfuckkfc

#### env
env command can visit magicexpr context. there are two context type, localcontext and globalcontext.  
localcontext lifecycle is valid only at current expression runtime and globalcontext is always valid since MagicExpression created.  

##### local context example

```
Map context = new HashMap();
context.put("name", "sundaymore");
MagicExpression magicExpression = MagicExpression.builder().build();
magicExpression.execute("{env name}", context);
```
output >> sundaymore


##### global context example
  
```
MagicExpression magicExpression = MagicExpression.builder().registerGlobalContext("name","sundaymore").build();
magicExpression.execute("{env name}")
```
output >> sundaymore  


#### other command

....



### custom command
define a custom command has two step
- 1.implement the interface CommandExecutor
- 2.register the executor to the MagicExpression while building
 
```
MagicExpression magicExpression = MagicExpression.builder().registerExecutor("seq", SeqExecutor.class).build()

```

## see more
```MagicExpressionTest.class``` 
