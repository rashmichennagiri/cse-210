import sys
import copy
sys.tracebacklimit = 0

###############################################################################
#                                                                             #
#  1. LEXER                                                                   #
#     To turn the input of characters into a stream of tokens                 #
#                                                                             #
###############################################################################

# 3 Token types: Aexpr Bexpr Commands/Satements
# EOF token = no more input left for lexical analysis
# All token types for INT, VAR, BOOL, ARR, PLUS, MINUS, MUL, LESSTHAN, EQUAL, NOT, AND, OR, SKIP, ASSIGN, WHILE, { , }, IF, THEN, ELSE ;
class Token():

    def __init__(self, type, value):
        self.type = type        # one of the defined token types
        self.value = value      # actual token value: 0, 1, 2, '+', '(', None

    def __repr__(self):
        return 'Token({type}, {value})'.format(type = self.type, value = self.value)


class Lexer():

    def __init__(self, text):
        self.state = {}
        self.text = text
        self.pos = 0
        self.current_char = self.text[self.pos]

    def error(self):
        raise Exception("INVALID CHARACTER!")

    def update_current_character(self):
        self.pos += 1
        # check for end of user input
        if self.pos > len(self.text)-1:
            self.current_char = None
        else:
            self.current_char = self.text[self.pos]

    def num(self):
        result = ''
        while self.current_char is not None and self.current_char.isdigit():
            result = result + self.current_char
            self.update_current_character()
        return int(result)

    #integer arrays reprented by lists
    def arr(self):
        result = ''
        self.update_current_character()
        while self.current_char is not None and self.current_char != "]":
            result = result+self.current_char
            self.update_current_character()
        self.update_current_character()
        result = [int(t) for t in result.split(',')]
        return result

    def assign(self):
        result = ''
        while self.current_char is not None and self.current_char in (':', '='):
            result = result + self.current_char
            self.update_current_character()
        if result == ":=":
            return "assign"
        else:
            self.error()   

    def get_next_token(self):
    # gets next token    
        while self.current_char is not None:
            if self.current_char.isspace():
                self.update_current_character()
            if self.current_char.isdigit():
                return Token("INT", self.num())
            if self.current_char == "[":
                return Token("ARR", self.arr())
            if self.current_char == "+":
                self.update_current_character()
                return Token('PLUS', "+")
            if self.current_char == "-":
                self.update_current_character()
                return Token("MINUS", "-")
            if self.current_char == "*":
                self.update_current_character()
                return Token("MUL", "*")
            if self.current_char == ";":
                self.update_current_character()
                return Token('COMP', ";")
            if self.current_char == "=":
                self.update_current_character()
                return Token('EQUAL', "=")
            if self.current_char == "<":
                self.update_current_character()
                return Token("LESSTHAN", "<")
            if self.current_char == "¬":
                self.update_current_character()
                return Token('NOT', "¬")
            if self.current_char == "∧":
                self.update_current_character()
                return Token('AND', "∧")
            if self.current_char == "∨":
                self.update_current_character()
                return Token('OR', "∨")
            if self.current_char == "{":
                self.update_current_character()
                return Token("LEFTCURL", "{")
            if self.current_char == "}":
                self.update_current_character()
                return Token("RIGHTCURL", "}")
            if self.current_char == "(":
                self.update_current_character()
                return Token("LEFTPAR", "(")
            if self.current_char == ")":
                self.update_current_character()
                return Token("RIGHTPAR", ")")
            if self.current_char == ":":
                return Token("ASSIGN", self.assign())
            #Alphebetical inputs
            if self.current_char.isalpha():
                result = ''
                while self.current_char is not None and (self.current_char.isalpha() or self.current_char.isdigit()):
                    result = result+self.current_char
                    self.update_current_character()
                if result == "while":
                    return Token("WHILE", "while")
                elif result == "skip":
                    return Token("SKIP", "skip")
                elif result == "do":
                    return Token("DO", "do")
                elif result == "if":
                    return Token("IF", "if")
                elif result == "else":
                    return Token("ELSE", "else")
                elif result == "then":
                    return Token("THEN", "then")
                elif result == "true":
                    return Token("BOOL", True)
                elif result == "false":
                    return Token("BOOL", False)
                else:
                    return Token("VAR", result)
            self.error()
        return(Token("EOF", None))


###############################################################################
#                                                                             #
#  2. PARSER                                                                  #
#     To build AST from the tokens                                            #
#                                                                             #
###############################################################################

class IntNode():
    def __init__(self, token):
        self.value = token.value
        self.op = token.type

class ArrNode():
    def __init__(self, token):
        self.value = token.value
        self.op = token.type

class VarNode():
    def __init__(self, token):
        self.value = token.value
        self.op = token.type

class BoolNode():
    def __init__(self, token):
        self.value = token.value
        self.op = token.type

class NotNode():
    def __init__(self, node):
        self.op = "NOT"
        self.ap = node


class BinopNode():
# binary operator node 
    def __init__(self, left, right, op):
        self.left = left
        self.right = right
        self.op = op


class BoolopNode():
# boolean operator node    
    def __init__(self, left, right, op):
        self.left = left
        self.right = right
        self.op = op

class SkipNode():
    def __init__(self, token):
        self.value = token.value
        self.op = token.type

class AssignNode():
    def __init__(self, left, right, op):
        self.left = left
        self.right = right
        self.op = op

class CompNode():
    def __init__(self, left, right, op):
        self.left = left
        self.right = right
        self.op = op

class WhileNode():
    # condition: while true and while false
    def __init__(self, cond, wtrue, wfalse):
        self.cond = cond
        self.wtrue = wtrue
        self.wfalse = wfalse
        self.op = "WHILE"

class IfNode():
    def __init__(self, cond, iftrue, iffalse):
        self.cond =cond
        self.iftrue = iftrue
        self.iffalse = iffalse
        self.op = "IF"

class Parser():
# To verify that the sequence of tokens does indeed correspond to the expected sequence of tokens    
# Finds the structure in the flat stream of tokens it gets from the lexer 

    def __init__(self, lexer):
        self.lexer = lexer
        self.state = lexer.state
        self.current_token = lexer.get_next_token()

    def error(self):
        raise error("Invalid Syntax for this language")   

    def factor(self):
        # returns the next integer term / unary operator subtree
        token = self.current_token
        
        if token.type == "MINUS":
            self.current_token = self.lexer.get_next_token()
            token = self.current_token
            token.value = -token.value
            node = IntNode(token)

        elif token.type == "INT":
            node = IntNode(token)

        elif token.type == "VAR":
            node = VarNode(token)

        elif token.type == "ARR":
            node = ArrNode(token)

        elif token.type == "NOT":
            self.current_token = self.lexer.get_next_token()
            if self.current_token.type == "LEFTPAR":
                self.current_token = self.lexer.get_next_token()
                node = self.bexpr()
            elif self.current_token.type == "BOOL":
                node = BoolNode(self.current_token)
            else:
                self.error()
            node = NotNode(node)

        elif token.type == "BOOL":
            node = BoolNode(token)

        elif token.type == "LEFTPAR":
            self.current_token = self.lexer.get_next_token()
            node = self.bexpr()

        elif token.type == "RIGHTPAR":
            self.current_token = self.lexer.get_next_token()

        elif token.type == "LEFTCURL":
            self.current_token = self.lexer.get_next_token()
            node = self.cexpr()

        elif token.type == "RIGHTCURL":
            self.current_token = self.lexer.get_next_token()

        elif token.type == "SKIP":
            node = SkipNode(token)

        elif token.type == "WHILE":
            self.current_token = self.lexer.get_next_token()
            cond = self.bexpr()
            wfalse = SkipNode(Token("SKIP","skip"))
            if self.current_token.type == "DO":
                self.current_token = self.lexer.get_next_token()
                if self.current_token == "LEFTCURL":
                    wtrue = self.cexpr()
                else:
                    wtrue = self.cterm()

            return WhileNode(cond, wtrue, wfalse)

        elif token.type == "IF":
            self.current_token = self.lexer.get_next_token()
            cond = self.bexpr()
            if self.current_token.type == "THEN":
                self.current_token = self.lexer.get_next_token()
                iftrue = self.cexpr()
            if self.current_token.type == "ELSE":
                self.current_token = self.lexer.get_next_token()
                iffalse = self.cexpr()
            return IfNode(cond, iftrue, iffalse)

        else:
            self.error()

        self.current_token = self.lexer.get_next_token()      
        return node
    
    def aterm(self):
        node = self.factor()
        while self.current_token.type == 'MUL':
            ttype = self.current_token.type
            self.current_token = self.lexer.get_next_token()
            node = BinopNode(left = node, right = self.factor(), op = ttype)
        return node
        
    def aexpr(self):
        node = self.aterm()  
        while self.current_token.type in ("PLUS", "MINUS"):
            ttype = self.current_token.type
            self.current_token = self.lexer.get_next_token()
            node = BinopNode(left = node, right = self.aterm(), op = ttype)
        return node

    #this returns a node that represent Aexpr for debugging
    def aparse(self):
        return self.aexpr()

    def bterm(self):
        node = self.aexpr()
        if self.current_token.type in ("EQUAL","LESSTHAN"):
            #print(self.current_token)
            ttype = self.current_token.type
            self.current_token = self.lexer.get_next_token()
            node = BoolopNode(left = node, right = self.aexpr(), op = ttype)
        return node
    
    def bexpr(self):
        node = self.bterm()
        while self.current_token.type in ("AND", "OR"):
            ttype = self.current_token.type
            self.current_token = self.lexer.get_next_token()
            node = BinopNode(left = node, right = self.bterm(), op = ttype)
        return node

    #this returns a node that represents combination of aexpr and bexpr
    def bparse(self):
        return self.bexpr()

    def cterm(self):
        node = self.bexpr()
        if self.current_token.type == "ASSIGN":
            #print(self.current_token)
            ttype = self.current_token.type
            self.current_token = self.lexer.get_next_token()
            node = AssignNode(left = node, right = self.bexpr(), op = ttype)
        return node

    def cexpr(self):
        node = self.cterm()
        while self.current_token.type == "COMP":
            #print(self.current_token)
            ttype = self.current_token.type
            self.current_token = self.lexer.get_next_token()
            node = CompNode(left = node, right = self.cterm(), op = ttype)
        return node

    #this returns a node that represents combination of aexpr and bexpr
    def cparse(self):
        return self.cexpr()

#General helper function for evaluating AST.
def create_dict(var, value):
    return dict([tuple([var,value])])

#Helper fuctions to do match:
def switch(op):
    cases = {
    "PLUS":'+', 
    "MINUS":'-', 
    "MUL":'*',
    "EQUAL":'=',
    "LESSTHAN":'<', 
    "AND":'∨', 
    "OR":'∧', 
    "ASSIGN":':=',
    "COMP":';',
    "NOT":'¬',
    }
    return cases.get(op, "You sure?")

#Helper function that prints recursively
def print_command(node):
    if node.op in ("INT", "ARR", "VAR", "SKIP"):
        return node.value
    elif node.op in ("BOOL"):
        return str(node.value).lower()
    elif node.op in ("PLUS", "MINUS", "MUL","EQUAL","LESSTHAN", "AND", "OR"):
        return ''.join(['(',str(print_command(node.left)), switch(node.op), str(print_command(node.right)), ')'])
    elif node.op in ("NOT"):
        return ''.join([switch(node.op),str(print_command(node.ap))])
    elif node.op in ("ASSIGN"):
        return ' '.join([str(print_command(node.left)), switch(node.op), str(print_command(node.right))])
    elif node.op in ("COMP"):
        return ' '.join([''.join([str(print_command(node.left)), switch(node.op)]), str(print_command(node.right))]) 
    elif node.op in ("WHILE"):
        return ' '.join(['while',str(print_command(node.cond)),'do', '{', str(print_command(node.wtrue)), '}'])
    elif node.op in ("IF"):
        return ' '.join(['if',str(print_command(node.cond)),'then', '{', str(print_command(node.iftrue)), '}', 'else', '{', str(print_command(node.iffalse)) , '}'])
    else:
        raise Exception("Pretty sure you made a mistake")

#helper class to do string manipulation
class Sstr():
    def __init__(self, string):
        self.string = string
    def __add__(self, other):
        return (self.string + other.string)
    def __sub__(self, other):
        return (self.string.replace(other.string, "", 1))
       #return (re.sub(other.string,'',self.string, count=1))
#root = parsewhile.test("if (true) then x:=1 else zir9 := 2")
#parsewhile.evaluate_print(root.ast, root.state, root.print_var, root.print_state, root.print_step)

def evaluate_print(ast, state, print_var, print_state, print_step, init_step):
    state = state
    node = ast

    print_var = print_var

    print_state = print_state
    print_step = print_step
    init_step = init_step

    if node.op in ("INT", "ARR", "BOOL"):
        return node.value
    elif node.op == "VAR":
        if node.value in state:
            return state[node.value]
        else:
            state = state.update(create_dict(node.value, 0))
            return 0
    elif node.op == "SKIP":
        state = state
        temp_var = set(print_var)
        temp_state = copy.deepcopy(state)
        temp_state = dict((var, temp_state[var]) for var in temp_var)
        print_state.append(temp_state)
        temp_step = Sstr(str(print_command(node)))
        print_step.append([Sstr(Sstr(init_step) - temp_step) - Sstr("; ")])
        init_step = Sstr(Sstr(init_step) - temp_step) - Sstr("; ")
    elif node.op == "COMP":
        evaluate_print(node.left, state, print_var, print_state, print_step, init_step)
        temp_var = set(print_var)
        temp_state = copy.deepcopy(state)
        temp_state = dict((var, temp_state[var]) for var in temp_var)
        print_state.append(temp_state)
        temp_step = Sstr(str(print_command(node.left)))
        print_step.append([str(Sstr(Sstr(init_step) - temp_step) - Sstr("; "))])
        init_step = Sstr(Sstr(init_step) - temp_step) - Sstr("; ")
        evaluate_print(node.right, state, print_var, print_state, print_step, init_step)
    elif node.op =="ASSIGN":
        var = node.left.value
        print_var.append(var)
        if var in state:
            state[var] = evaluate_print(node.right, state, print_var, print_state, print_step, init_step)
        else:
            state.update(create_dict(var, evaluate_print(node.right, state, print_var, print_state, print_step, init_step)))
        temp_var = set(print_var)
        temp_state = copy.deepcopy(state)
        temp_state = dict((var, temp_state[var]) for var in temp_var)
        print_state.append(temp_state)
        temp_step = Sstr(str(print_command(node)))
        print_step.append(["skip; "+ str(Sstr(Sstr(init_step) - temp_step) - Sstr("; "))])
        init_step = Sstr(Sstr(init_step) - temp_step) - Sstr("; ")

    elif node.op == "PLUS":
        try:
            return evaluate_print(node.left, state, print_var, print_state, print_step, init_step)+evaluate_print(node.right, state, print_var, print_state, print_step, init_step)
        except TypeError:
            print("This operation is not supported")
    elif node.op == "MINUS":
        try:
            return evaluate_print(node.left, state, print_var, print_state, print_step, init_step)-evaluate_print(node.right, state, print_var, print_state, print_step, init_step)
        except TypeError:
            print("This operation is not supported")
    elif node.op == "MUL":
        try:
            return evaluate_print(node.left, state, print_var, print_state, print_step, init_step)*evaluate_print(node.right, state, print_var, print_state, print_step, init_step)
        except TypeError:
            print("This operation is not supported")
    elif node.op == "NOT":
        return not evaluate_print(node.ap, state, print_var, print_state, print_step, init_step)
        #print_state.append(copy.deepcopy(state))
    elif node.op =="EQUAL":
        #print("equal", state)
        return evaluate_print(node.left, state, print_var, print_state, print_step, init_step) == evaluate_print(node.right, state, print_var, print_state, print_step, init_step)
        #print_state.append(copy.deepcopy(state))
    elif node.op =="LESSTHAN":
        #print("LESSTHAN", state)
        #print_state.append(copy.deepcopy(state))
        return evaluate_print(node.left, state, print_var, print_state, print_step, init_step) < evaluate_print(node.right, state, print_var, print_state, print_step, init_step)
    elif node.op =="AND":
        #print("and", state)
        #print_state.append(copy.deepcopy(state))
        return (evaluate_print(node.left, state, print_var, print_state, print_step, init_step) and evaluate_print(node.right, state, print_var, print_state, print_step, init_step))
    elif node.op =="OR":
        #print("or",state)
        #print_state.append(copy.deepcopy(state))
        return (evaluate_print(node.left, state, print_var, print_state, print_step, init_step) or evaluate_print(node.right, state, print_var, print_state, print_step, init_step))
    elif node.op == "WHILE":
        cond = node.cond
        wtrue = node.wtrue
        wfalse = node.wfalse
        sentinel = 0
        while evaluate_print(cond, state, print_var, print_state, print_step, init_step):
            sentinel = sentinel+1
            if sentinel > 60000-1:
                break
            temp_var = set(print_var)
            temp_state = copy.deepcopy(state)
            temp_state = dict((var, temp_state[var]) for var in temp_var)
            print_state.append(temp_state)
            init_step = init_step.replace(print_command(node), str(print_command(node.wtrue)+'; '+print_command(node)))
            print_step.append([init_step])
            evaluate_print(wtrue, state, print_var, print_state, print_step, init_step)
            temp_var = set(print_var)
            temp_state = copy.deepcopy(state)
            temp_state = dict((var, temp_state[var]) for var in temp_var)
            print_state.append(temp_state)
            temp_step = Sstr(str(print_command(node.wtrue)))            # node = whole while node
            print_step.append([Sstr(Sstr(init_step) - temp_step) - Sstr("; ")])
            init_step = Sstr(Sstr(init_step) - temp_step) - Sstr("; ")
        temp_var = set(print_var)
        temp_state = copy.deepcopy(state)
        temp_state = dict((var, temp_state[var]) for var in temp_var)
        print_state.append(temp_state)
        temp_step = Sstr(print_command(node))
        print_step.append(["skip; "+ (Sstr(Sstr(init_step) - temp_step) - Sstr("; "))])
        init_step = Sstr(Sstr(init_step) - temp_step) - Sstr("; ")
    elif node.op =="IF":
        cond = node.cond
        iftrue = node.iftrue
        iffalse = node.iffalse
        if evaluate_print(cond, state, print_var, print_state, print_step, init_step):
            #only record the state before execution
            temp_var = set(print_var)
            temp_state = copy.deepcopy(state)
            temp_state = dict((var, temp_state[var]) for var in temp_var)
            print_state.append(temp_state)
            temp_step = Sstr(str(print_command(node)))
            print_step.append([str(print_command(node.iftrue)) + (Sstr(init_step) - temp_step)])
            init_step = str(print_command(node.iftrue)) + (Sstr(init_step) - temp_step)
            evaluate_print(iftrue, state, print_var, print_state, print_step, init_step)
        else:
            temp_var = set(print_var)
            temp_state = copy.deepcopy(state)
            temp_state = dict((var, temp_state[var]) for var in temp_var)
            print_state.append(temp_state)
            temp_step = Sstr(str(print_command(node)))
            print_step.append([str(print_command(node.iffalse)) + (Sstr(init_step) - temp_step)])
            init_step = str(print_command(node.iffalse)) + (Sstr(init_step) - temp_step)
            evaluate_print(iffalse, state, print_var, print_state, print_step, init_step)
    else:
        raise Exception("error in evaluation")

class Interpreter():
    def __init__(self, parser):
        self.state = parser.state
        self.ast = parser.cparse()      # load AST from root
        self.print_var = []
        self.print_state = []
        self.print_step = []
        self.init_step = print_command(self.ast)
    def error(self):
        raise Exception("This input is invalid")
    def visit(self):
        return evaluate_print(self.ast, self.state, self.print_var, self.print_state, self.print_step, self.init_step)

#returns an interpreter object for debugging
def test(text):
    a = Lexer(text)
    b = Parser(a)
    c = Interpreter(b)
    return c

###############################################################################
#                                                                             #
#   MAIN                                                                      #
#                                                                             #
###############################################################################

def main():

    while True:
        try:
            text = input('')
            #line = line.strip()
            #line = " ".join(line.split())
        except EOFError:
            break
        if not text:
            continue

        #print(text)
        lexer = Lexer(text)
        parser = Parser(lexer)
        interpreter = Interpreter(parser)
        interpreter.visit()
        step_list = interpreter.print_step
        step_list = [item for sublist in step_list for item in sublist]
        state_list = interpreter.print_state
        if text[0:5] == "skip;" or text[0:6] == "skip ;":
            del step_list[0]
            del state_list[0]
        
        step_list[-1] = 'skip'

        if len(state_list) > 10000:
            state_list = state_list[0:10000]
            step_list = step_list[0:10000]
        
        if len(state_list) ==1 and state_list[0] == {} and text[0:4] == "skip": 
            print('{}')
            pass
        else:
            for i in range(len(state_list)):
                output_string = []
                for key in sorted(state_list[i]):
                    separator = " "
                    output_string.append(separator.join([key, "→", str(state_list[i][key])]))

            # states managed by python dictionary
            state_string = ''.join(["{", ", ".join(output_string), "}"])
            step_string = ' '.join(['⇒', step_list[i]])
                #print(step_string, state_string, sep = ', ')
            print(state_string)
            

if __name__ == '__main__':
    main()

