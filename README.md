# Architecture-Simulator
### Concordia project for computer architecture class

### Put the code you want to compile in code.mod7, and run the main.java file.

## The ALU implements the following instructions, with the following behavior and limitations:

1. **LDA reg1 reg2/var/const :**
Load register reg1 with the contents of either the contents of reg2, or the memory var or a constant
const. Memory regions loads (load into a variable, for instance) are NOT ALLOWED.
  
2. **STR var reg/const :**
Store in the memory position referred by var the value of register reg or a constant const.
Register stores (store into register t0, for instance) are NOT ALLOWED.
  
3. ~~**PUSH reg/var/const :**
Push to the top of the stack the contents of reg or var or a constant const~~
        *(Doesn't work since there is no implementation of the stack)*
  
4. ~~**POP reg :**
Pop from the top of the stack and store the value on reg. Storing in a memory region is NOT
ALLOWED.~~ *(Also doesn't work for the same reasons)*
  
5. **AND reg1 reg2/var/const :**
Performs a logical AND operation between reg1 and a register reg2, a variable var or a constant
const, and store the result on register reg1. Memory regions stores (store result into a variable, for
instance) are NOT ALLOWED.
  
6. **OR reg1 reg2/var/const :**
Performs a logical OR operation between reg1 and a register reg2, a variable var or a constant
const, and store the result on register reg1. Memory regions stores (store result into a variable, for
instance) are NOT ALLOWED.
  
7. **NOT reg :**
Performs a logical NOT operation on register reg and store the result on register reg. Memory
regions stores (store result into a variable, for instance) are NOT ALLOWED.
  
8. **ADD reg1 reg2/var/const :**
Performs the addition operation of reg1 and a register reg2, a variable var or a constant const,
and store the result on register reg1. Memory regions stores (store result into a variable, for
instance) are NOT ALLOWED.
  
9. **SUB reg1 reg2/var/const :**
Performs the subtraction operation of reg1 and a register reg2, a variable var or a constant const,
and store the result on register reg1. The operation is given by second argument minus the first
argument (i.e., reg2 – reg1). Memory regions stores (store result into a variable, for instance) are
NOT ALLOWED.
  
10. **DIV reg1 reg2/var/const :**
Performs the integer division operation of reg1 and a register reg2, a variable var or a constant
const, and store the result on register reg1. The operation is given by second argument divided
by the first argument (i.e., reg2 / reg1). Memory regions stores (store result into a variable, for
instance) are NOT ALLOWED.
  
11. **MUL reg1 reg2/var/const :**
Performs the integer multiplication operation of reg1 and a register reg2, a variable var or a
constant const, and store the result on register reg1. Memory regions stores (store result into a
variable, for instance) are NOT ALLOWED.
  
12. **MOD  reg1 reg2/var/const :**
Performs the integer modulo operation of reg1 and a register reg2, a variable var or a constant
const, and store the result on register reg1. The operation is given by second argument modulo the
first argument (i.e., reg2 mod reg1). Memory regions stores (store result into a variable, for
instance) are NOT ALLOWED.
  
13. **INC reg :**
Increments the value of a register reg. Memory increments (incrementing a variable, for instance)
are NOT ALLOWED.
  
14. **DEC reg :**
Decrements the value of a register reg. Memory increments (decrementing a variable, for instance)
are NOT ALLOWED.
  
15. **BEQ reg1/var1/const1 reg2/var2/const2 LABEL :**
Performs a comparison between two values, given by registers, variables or constants. Any
combination is permitted. If they are equal, jump to the address defined by the label
LABEL
  
16. **BNE reg1/var1/const1 reg2/var2/const2 LABEL :**
Performs a comparison between two values, given by registers, variables or constants. Any
combination is permitted. If they are different, jump to the address defined by the label
LABEL
  
17. **BBG reg1/var1/const1 reg2/var2/const2 LABEL :**
Performs a comparison between two values, given by registers, variables or constants. Any
combination is permitted. If the first parameter is bigger than the second parameter, jump to the
address defined by the label LABEL
  
18. **BSM reg1/var1/const1 reg2/var2/const2 LABEL :**
Performs a comparison between two values, given by registers, variables or constants. Any
combination is permitted. If the first parameter is smaller than the second parameter, jump to the
address defined by the label LABEL
  
19. **JMP LABEL :**
Jump to the address defined by the label LABEL
  
20. **HLT :**
End the program execution.
