ORG 0x0
V0: WORD $DEFAULT, 0x180 ; задаются вектора прерываний
V1: WORD $DEFAULT, 0x180
V2: WORD $INT2, 0x180
V3: WORD $INT3, 0x180
V4: WORD $DEFAULT, 0x180
V5: WORD $DEFAULT, 0x180
V6: WORD $DEFAULT, 0x180
V7: WORD $DEFAULT, 0x180

DEFAULT: IRET ; Обрабока прерывания по умолчанию 

ORG 0x20
X: WORD 0x1

MIN: WORD 0xFFBE ; -64
MAX: WORD 0x003F ; 63

ORG 0x03A
START:  DI
        CLA
        OUT 0x1
        OUT 0x5
        OUT 0xB
        OUT 0xD
        OUT 0x11
        OUT 0x15
        OUT 0x19
        OUT 0x1D
        LD #0xB ; Загрузка в аккумулятор MR (1000|0011=1011)
        OUT 0x7 ; Разрешение прерываний для 3 ВУ
        LD #0xA ; Загрузка в аккумулятор MR (1000|0010=1010)
        OUT 0x5 ; Разрешение прерываний для 2 ВУ
        EI

MAIN:   DI
        LD $X
        INC
        INC
        CALL CHECK
        ST $X
        EI
        JUMP MAIN

CHECK:
        CMP $MIN ; Если x > min переход на max
        BPL CHECK_MAX
        JUMP LD_MIN
        CHECK_MAX: CMP $MAX
        BMI RETURN ; Если x < max переход
        LD_MIN: LD $MIN
        RETURN: RET

INT3:   DI                
        LD X             
        ASL              
        NEG              
        OUT 0x6         
        EI               
        IRET              


INT2:   DI                 
        IN 0x4             
        AND X              
        AND #0x000F        
        ST X               
        EI                 
        IRET               
		
