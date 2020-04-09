'''
1. lexer: to turn the input of characters into a stream of tokens
2. parse: to recognize phrases in the stream of tokens 
3. interpreter: to execute parse input

TODO:
* spaces between digits
	  1 0 + 100 - 1 0  

'''

# Token types
# EOF token = no more input left for lexical analysis
INTEGER, PLUS, MINUS, EOF = 'INTEGER', 'PLUS', 'MINUS', 'EOF'



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



class Interpreter(object):

    #constructor
    def __init__(self, user_input):
        print("1. " + user_input)
        self.user_input = user_input   # client string input, e.g. "3+5", "12 - 5"
        self.index = 0                 # self.index is an index into self.text
        self.current_token = None      # current token instance
        self.current_character = self.user_input[self.index]
        print("2. " + self.current_character)
        
    def error(self):
        raise Exception('Error parsing input')

        
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
            print("3a. " + self.current_character)

            if self.current_character.isspace():
                self.remove_white_spaces()
                continue
                
            if self.current_character.isdigit():
                i = self.get_integer_input()
                print( i )
                return Token(INTEGER, i)

            if self.current_character == '+':
                self.update_current_character()
                return Token(PLUS, self.current_character)

            if self.current_character == '-':
                self.update_current_character()
                return Token(MINUS, self.current_character)

            self.error()

        return Token(EOF, None)
     
        
        
    def eat(self, token_type):
        # compare the current token type with the passed token
        # type and if they match then "eat" the current token
        # and assign the next token to the self.current_token,
        # otherwise raise an exception.
        if self.current_token.type == token_type:
            self.current_token = self.get_next_token()
        else:
            self.error()

    def expr(self):
        """
        i. PARSING
            syntax analysis
            verifies that the sequence of tokens does indeed correspond to the expected sequence of tokens
            expr -> INTEGER OP INTEGER
            find the structure in the flat stream of tokens it gets from the lexer 
            tries to find a sequence of tokens: integer followed by a plus sign followed by an integer
        ii. INTERPRETING
            After it’s successfully confirmed the structure, it generates the result

        parsing and interpreting done hand in hand    
        """
        
        # set current token to the first token taken from the input
        # we expect the first token to be an integer
        self.current_token = self.get_next_token()
        result = self.current_token.value
        self.eat(INTEGER)
        print(result)

        while self.current_token.type in (PLUS, MINUS):
        # we expect the current token to be an operator
            op = self.current_token

            if op.type == PLUS:
                self.eat(PLUS)
                # we expect the next token to be an integer
                right = self.current_token.value
                self.eat(INTEGER)
                result = result + right
            else:
                self.eat(MINUS)
                # we expect the next token to be an integer
                right = self.current_token.value
                self.eat(INTEGER)
                result = result - right

        # after the above call the self.current_token is set to EOF token
        
        return result


def main():
    while True:
        try:
            user_input = input('calc> ')
        except EOFError:
            break
        if not user_input:
            continue
            
        interpreter = Interpreter(user_input)
        print("3. " + interpreter.current_character)
        
        result = interpreter.expr()
        print(result)


if __name__ == '__main__':
    main() 
