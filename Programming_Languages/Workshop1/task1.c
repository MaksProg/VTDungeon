#include <stdio.h>
#include <stdlib.h>

void encrypt(char *, int);
void decrypt(char *, int);

int main(int argc, char * argv[]) {
    printf("argc = %d\n", argc);
    printf("argv[0] = %s\n", argv[0]);

    if (argc != 3) {
        printf("Incorrect usage");
        return 1;
    }

    char * word = argv[1];
    int shift = atoi(argv[2]);

   

    encrypt(word, shift);

    printf("encrypt = %s\n", word);

    decrypt(word, shift);

    printf("decrypt = %s\n", word);
    return 0;
}

void encrypt(char * word, int shift) {
    shift = (shift % 26 + 26) % 26;
    for (int i = 0; word[i] != '\0'; ++i) {
        char current_char = word[i];
        if (current_char >= 'a' && current_char <= 'z') {
            word[i] = 'a' + (current_char - 'a' + shift) % 26;
        }
        if (current_char >= 'A' && current_char <= 'Z') {
            word[i] = 'A' + (current_char - 'A' + shift) % 26;
        }   
    }
}

void decrypt(char * word, int shift) {
    encrypt(word, -shift);
}