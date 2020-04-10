'''

TODO:
* spaces between digits
	  1 0 + 100 - 1 0  
* handle
    - exponentiation

* tests:
calc> 7 + 3 * (10 / (12 / (3 + 1) - 1)) / (2 + 3) - 5 - 3 + (8)
10

'''

###############################################################################
#                                                                             #
#  1. LEXER                                                                   #
#     To turn the input of characters into a stream of tokens                 #
#                                                                             #
###############################################################################


# Token types
# EOF token = no more input left for lexical analysis
INTEGER, PLUS, MINUS, MULTIPLY, DIVIDE, LPAREN, RPAREN, EOF = (
    'INTEGER', 'PLUS', 'MINUS', 'MULTIPLY', 'DIVIDE', '(', ')', 'EOF'
)


class Token(object):
    
    # constructor
    def __init__(self, type, value):
        # token type: INTEGER, PLUS, MINUS or EOF
        self.type = type
        # token value: 0, 1, 2. 3, 4, 5, 6, 7, 8, 9, '+', or None
        self.value = value

    # to print objects as string: Token(INTEGER, 3), Token(PLUS '+')    
    def __str__(self): 
        return 'Token({type}, {value})'.format(
            type=self.type,
            value=repr(self.value)
        )

    def __repr__(self):
        return self.__str__()



    
class Lexer(object):

    #constructor
    def __init__(self, user_input):
        self.user_input = user_input   # client string input, e.g. "3+5", "12 - 5"
        self.index = 0                 # self.index is an index into self.text
        self.current_character = self.user_input[self.index]
        
        
    def error(self):
        raise Exception('INVALID CHARACTER!')


    # TODO
    def checkIfOperator(self, char):
        if char == '+' or char == '-': 
            return True
        return False  
    
    
    def update_current_character(self):
        self.index += 1
        
        # check for end of user input
        if self.index > len(self.user_input)-1:
            self.current_character = None
        else:
            self.current_character = self.user_input[self.index]

    
    def remove_white_spaces(self):
        while self.current_character is not None and self.current_character.isspace():
            self.update_current_character()
            
            
    def get_integer_input(self):
    # to get multiple integers in the input
        number = ''
        while self.current_character is not None and self.current_character.isdigit():
            number += self.current_character
            self.update_current_character()
        return int(number)
       
        
    # Lexical analyzer: breaks a sentence into tokens one at a time
    # returns next token in input
    def get_next_token(self):

        while self.current_character is not None:
            #print("self.current_character: " + self.current_character)

            if self.current_character.isspace():
                self.remove_white_spaces()
                continue
                
            if self.current_character.isdigit():
                return Token(INTEGER, self.get_integer_input())

            if self.current_character == '+':
                self.update_current_character()
                return Token(PLUS, '+')

            if self.current_character == '-':
                self.update_current_character()
                return Token(MINUS, '-')

            if self.current_character == '*':
                self.update_current_character()
                return Token(MULTIPLY, '*')    

            if self.current_character == '/':
                self.update_current_character()
                return Token(DIVIDE, '/')  

            if self.current_character == '(':
                self.update_current_character()
                return Token(LPAREN, '(')      

            if self.current_character == ')':
                self.update_current_character()
                return Token(RPAREN, ')')  

            self.error()

        return Token(EOF, None)
     


###############################################################################
#                                                                             #
#  2. PARSER                                                                  #
#     To build AST from the tokens                                            #
#                                                                             #
###############################################################################


class AST(object):
    pass



class BinaryOperation(AST):
    def __init__(self, left, op, right):
        self.left = left                # integer node of type 'Number' / subtree of BinOp
        self.token = self.op = op       # operation token 
        self.right = right              # integer node of type 'Number' / subtree of BinOp



class Number(AST):
    # number node
    def __init__(self, token):
        self.token = token              # integer token
        self.value = token.value        # actual integer value



class Parser(object):
    '''
       i. PARSING
       syntax analysis
        verifies that the sequence of tokens does indeed correspond to the expected sequence of tokens
        expr -> INTEGER OP INTEGER
        find the structure in the flat stream of tokens it gets from the lexer 
        tries to find a sequence of tokens: integer followed by a plus sign followed by an integer
    '''

    def __init__(self, lexer):
        self.lexer = lexer
        self.current_token = self.lexer.get_next_token()  
        # set current token to the first token taken from the input


    def error(self):
        raise Exception('INVALID SYNTAX!')


    def eat(self, token_type):
        # compare the current token type with the passed token type
        # if they match, "eat" current token, then assign the next token
        # else raise exception
        if self.current_token.type == token_type:
            self.current_token = self.lexer.get_next_token()
        else:
            self.error()


    def get_next_factor(self):
    # returns the next integer term 
    # factor : INTEGER | LPAREN expr RPAREN   
        token = self.current_token

        if token.type == INTEGER:
            self.eat(INTEGER)
            return Number(token)

        elif token.type == LPAREN:
            self.eat(LPAREN)
            node = self.expr()
            self.eat(RPAREN)
            return node   


    def get_next_term(self):
    # for MUL/DIV
    # term: factor ( (MUL|DIV) factor )*   
        node = self.get_next_factor()
        
        while self.current_token.type in (MULTIPLY, DIVIDE):
        # we expect the current token to be an operator
            token = self.current_token

            if token.type == MULTIPLY:
                self.eat(MULTIPLY)
                #result = result * self.get_next_factor()
            else:
                self.eat(DIVIDE)
                #result = result / self.get_next_factor()
        
            node = BinaryOperation( left=node , op=token, right=self.get_next_term())

        return node



    def expr(self):
    # expr : term ((PLUS | MINUS) term)*    

        """Arithmetic expression parser / interpreter.
        expr   : term ((PLUS | MINUS) term)*
        term   : factor ((MUL | DIV) factor)*
        factor : INTEGER
        """
        """
        ii. INTERPRETING
        After it’s successfully confirmed the structure, it generates the result
       """

        node = self.get_next_term()

        while self.current_token.type in (PLUS, MINUS):
        # we expect the current token to be an operator
            token = self.current_token

            if token.type == PLUS:
                self.eat(PLUS)
                #result = result + self.get_next_term()
            
            elif token.type == MINUS:
                self.eat(MINUS)
                #result = result - self.get_next_term()

            node = BinaryOperation( left=node, op=token, right=self.get_next_term())
            # left assiociativity
            
        # after the above call the self.current_token is set to EOF token
        
        return node


    def parse(self):
        return self.expr()



###############################################################################
#                                                                             #
#  3. INTERPRETER                                                             #
#     To execute the parsed AST                                               #
#                                                                             #
###############################################################################


# visitor design pattern
class NodeVisitor(object):

    def visit(self, node):
        method_name = 'visit_' + type(node).__name__                # type(node).__name__ = returns name of the class
        #print(method_name)
        visitor = getattr(self, method_name, self.generic_visit)    # visitor is the actual function: visit_BinaryOperation/ visit_Number
        #print("2 ~~~~")
        #print( visitor(node) )
        #print("3 ~~~~")
        return visitor(node)        

    def generic_visit(self, node):
        raise Exception('No visit_{} method'.format(type(node).__name__))



class Interpreter(NodeVisitor):
    def __init__(self, parser):
        self.parser = parser

    # post-order traversal
    def visit_BinaryOperation(self, node):
        if node.op.type == PLUS:
            #print(" + ^^^^^")
            return self.visit(node.left) + self.visit(node.right)
        elif node.op.type == MINUS:
            #print(" - ^^^^^")
            return self.visit(node.left) - self.visit(node.right)
        elif node.op.type == MULTIPLY:
            #print(" * ^^^^^")
            return self.visit(node.left) * self.visit(node.right)
        elif node.op.type == DIVIDE:
            #print(" / ^^^^^")
            return self.visit(node.left) / self.visit(node.right)


    def visit_Number(self, node):
        return node.value


    def interpret(self):
        tree = self.parser.parse()
        return self.visit(tree)



###############################################################################
#                                                                             #
#   MAIN                                                                      #
#                                                                             #
###############################################################################


def main():
    while True:
        try:
            user_input = input('calc> ')
        except EOFError:
            break
        if not user_input:
            continue

        lexer = Lexer(user_input)  
        parser = Parser(lexer)  
        interpreter = Interpreter(parser)

        result = interpreter.interpret()

        print("=======")
        print(result)



if __name__ == '__main__':
    main() 