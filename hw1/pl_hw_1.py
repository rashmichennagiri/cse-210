#!/usr/bin/env python
# coding: utf-8


'''
Only single digit integers are allowed in the input
The only arithmetic operation supported at the moment is addition

1. lexer: to turn the input of characters into a stream of tokens
2. 
'''

# Token types
# EOF token = no more input left for lexical analysis
INTEGER, PLUS, MINUS, EOF = 'INTEGER', 'PLUS', 'MINUS', 'EOF'


class Token(object):
    
    #constructor
    def __init__(self, type, value):
        # token type: INTEGER, PLUS, MINUS or EOF
        self.type = type
        # token value: 0, 1, 2. 3, 4, 5, 6, 7, 8, 9, '+', or None
        self.value = value

    def __str__(self):
        """String representation of the class instance.
        Examples: Token(INTEGER, 3), Token(PLUS '+') """
        
        return 'Token({type}, {value})'.format(
            type=self.type,
            value=repr(self.value)
        )

    def __repr__(self):
        return self.__str__()



class Interpreter(object):

    #constructor
    def __init__(self, text):
        # client string input, e.g. "3+5", "12 - 5"
        self.text = text
        # self.pos is an index into self.text
        self.position = 0
        # current token instance
        self.current_token = None
        self.current_character = self.text[self.position]

        
    def error(self):
        raise Exception('Error parsing input')

        
    def checkIfOperator(self, char):
        if char == '+' or char == '-': 
            return True
        return False  
    
    
    # Lexical analyzer: breaks a sentence into tokens one at a time    
    def get_next_token(self):
        
        user_input = self.text
        
        # remove extra white spaces in input
        user_input = user_input.replace(" ", "")
        
        # check for end of user input
        if self.position > len(user_input)-1:
            return Token(EOF, None)

        # get a character and decide what token to create
        current_char = text[self.pos]
        self.pos += 1
        print("1" + current_char)
        print("2" + text[self.pos])
        current_char = text[self.pos]
        
        while current_char.isdigit():
            print(">>" + current_char)
            self.pos += 1
            current_char += text[self.pos]
            
        
        
            
        # if the character is a digit then convert it to
        # integer, create an INTEGER token, increment self.pos
        # index to point to the next character after the digit,
        # and return the INTEGER token
        if current_char.isdigit():
            token = Token(INTEGER, int(current_char))
            self.pos += 1
            return token

        if current_char == '+':
            token = Token(PLUS, current_char)
            self.pos += 1
            return token

        if current_char == '-':
            token = Token(MINUS, current_char)
            self.pos += 1
            return token
        
        self.error()

     
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
        i. verifies that the sequence of tokens does indeed correspond to the expected sequence of tokens
            expr -> INTEGER PLUS INTEGER
            find the structure in the flat stream of tokens it gets from the lexer 
            tries to find a sequence of tokens: integer followed by a plus sign followed by an integer
        ii. After it’s successfully confirmed the structure, it generates the result
        
        """
        
        # set current token to the first token taken from the input
        self.current_token = self.get_next_token()

        # we expect the current token to be a single-digit integer
        left = self.current_token
        self.eat(INTEGER)

        # we expect the current token to be a '+' token
        op = self.current_token
        
        self.eat(PLUS)

        # we expect the current token to be a single-digit integer
        right = self.current_token
        self.eat(INTEGER)
        # after the above call the self.current_token is set to EOF token

        # at this point INTEGER PLUS INTEGER sequence of tokens
        # has been successfully found and the method can just
        # return the result of adding two integers, thus
        # effectively interpreting client input
        result = left.value + right.value
        return result


def main():
    while True:
        try:
            text = input('calc> ')
        except EOFError:
            break
        if not text:
            continue
        interpreter = Interpreter(text)
        result = interpreter.expr()
        print(result)


if __name__ == '__main__':
    main()
