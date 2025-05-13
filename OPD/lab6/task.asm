ORG 0x0
V0: WORD $default, 0x180 ; задаются вектора прерываний
V1: WORD $default, 0x180
V2: WORD $int2, 0x180
V3: WORD $int3, 0x180
V4: WORD $default, 0x180
V5: WORD $default, 0x180
V6: WORD $default, 0x180
V7: WORD $default, 0x180

DEFAULT: IRET ; Обрабока прерывания по умолчанию 

X: WORD ?

MIN: WORD 0xFFE0 ; -32
MAX: WORD 0x001F ; 31

ORG 0x20
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
        LD #0x9 ; Загрузка в аккумулятор MR (1000|0001=1001)
        OUT 0x7 ; Разрешение прерываний для 3 ВУ
        LD #0xB ; Загрузка в аккумулятор MR (1000|0011=1011)
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

INT3:   DI                 ; Запрет прерываний
        LD X              ; Загружаем X
        ASL               ; Умножаем X на 2
        NEG               ; Меняем знак → –2X
        OUT 0x6           ; Выводим результат на ВУ-3 (порт 0x6, например)
        EI                ; Разрешаем прерывания
        IRET              ; Возврат из прерывания


INT2:   DI                 ; Запрет прерываний
        IN 0x4             ; Считываем данные с ВУ-2 (уточни порт при необходимости)
        AND X              ; Побитовое И с X
        AND #0x000F        ; Маска для оставления только младших 4 бит
        ST X               ; Сохраняем результат в X
        EI                 ; Разрешаем прерывания
        IRET               ; Возврат из прерывания
		
