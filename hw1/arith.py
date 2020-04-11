'''
TODO:
    - exponentiation
    - spaces between digits?
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
    
    def __init__(self, type, value):
        
        self.type = type        # one of the defined token types
        self.value = value      # actual token value: 0, 1, 2, '+', '(', None

    def __str__(self): 
    # to print objects as string: Token(INTEGER, 3), Token(PLUS '+')    

        return 'Token({type}, {value})'.format(
            type=self.type,
            value=repr(self.value)
        )

    def __repr__(self):
        return self.__str__()


    
class Lexer(object):

    def __init__(self, user_input):
        self.user_input = user_input   # client string input: '3+5', '12 - 5'
        self.index = 0                 # index of current character in user_input
        self.current_character = self.user_input[self.index]
        
        
    def error(self):
        raise Exception('INVALID CHARACTER!')
    
    
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
       
        
    def get_next_token(self):
    # returns next token in user input

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
# binary operator node    
    def __init__(self, left, op, right):
        self.left = left                # integer node of type 'Number' / subtree of BinOp
        self.token = self.op = op       # operation token 
        self.right = right              # integer node of type 'Number' / subtree of BinOp



class UnaryOperation(AST):
# unary operator node    
    def __init__(self, op, expr):
        self.token = self.op = op       # unary operator: + or -
        self.expr = expr                # expr = AST node



class Number(AST):
# number node
    def __init__(self, token):
        self.token = token              # integer token
        self.value = token.value        # actual integer value



class Parser(object):
# To verify that the sequence of tokens does indeed correspond to the expected sequence of tokens    
# Finds the structure in the flat stream of tokens it gets from the lexer 

    def __init__(self, lexer):
        self.lexer = lexer
        self.current_token = self.lexer.get_next_token()    # set current token to the first token taken from the input


    def error(self):
        raise Exception('INVALID SYNTAX!')


    def eat(self, token_type):
    # compare the current token type with the passed token type
    # if they match, 'eat' current token, then assign the next token
    # else raise exception
        if self.current_token.type == token_type:
            self.current_token = self.lexer.get_next_token()
        else:
            self.error()


    def get_next_factor(self):
    # returns the next integer term / unary operator subtree
    # factor : (PLUS|MINUS) factor | INTEGER | LPAREN expr RPAREN   
        token = self.current_token

        if token.type == PLUS:
            self.eat(PLUS)
            node = UnaryOperation(token, self.get_next_factor())
            return node

        elif token.type == MINUS:
            self.eat(MINUS)
            node = UnaryOperation(token, self.get_next_factor())
            return node

        elif token.type == INTEGER:
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
            else:
                self.eat(DIVIDE)
        
            node = BinaryOperation( left=node , op=token, right=self.get_next_term())

        return node


    def expr(self):
    # Arithmetic Expression Grammar:
    # expr   : term ((PLUS | MINUS) term)*
    # term   : factor ((MUL | DIV) factor)*
    # factor : (PLUS|MINUS) factor | INTEGER | LPAREN expr RPAREN   
      
        node = self.get_next_term()

        while self.current_token.type in (PLUS, MINUS):
        # we expect the current token to be an operator
            token = self.current_token

            if token.type == PLUS:
                self.eat(PLUS)
            
            elif token.type == MINUS:
                self.eat(MINUS)

            # left assiociativity
            node = BinaryOperation( left=node, op=token, right=self.get_next_term())
            
            
        # after all the above calls, the self.current_token is now set to EOF token
        
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
        visitor = getattr(self, method_name, self.generic_visit)    # visitor is the actual function: visit_BinaryOperation/visit_Number
        return visitor(node)        

    def generic_visit(self, node):
        raise Exception('No visit_{} method'.format(type(node).__name__))



class Interpreter(NodeVisitor):
    def __init__(self, parser):
        self.parser = parser

    # post-order traversal
    def visit_BinaryOperation(self, node):
        if node.op.type == PLUS:
            return self.visit(node.left) + self.visit(node.right)
        elif node.op.type == MINUS:
            return self.visit(node.left) - self.visit(node.right)
        elif node.op.type == MULTIPLY:
            return self.visit(node.left) * self.visit(node.right)
        elif node.op.type == DIVIDE:
            return self.visit(node.left) / self.visit(node.right)


    def visit_UnaryOperation(self,node):
        if node.op.type == PLUS:
            return +self.visit(node.expr)
        elif node.op.type == MINUS:
            return -self.visit(node.expr)
        

    def visit_Number(self, node):
        return node.value


    def eval(self):
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
            user_input = input('')
        except EOFError:
            break
        if not user_input:
            continue

        lexer = Lexer(user_input)  
        parser = Parser(lexer)  
        interpreter = Interpreter(parser)

        result = interpreter.eval()
        print(result)



if __name__ == '__main__':
    main() 
