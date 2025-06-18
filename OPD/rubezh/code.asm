ORG 0x300

I:         WORD 0          
N:         WORD 23         
BASE:      WORD 0x6C4      

ADDR_LO:   WORD 0          
ADDR_HI:   WORD 0          

TMP_LO:    WORD 0          
TMP_HI:    WORD 0          

RES_LO:    WORD 0xFFFF     
RES_HI:    WORD 0x003F     

START:
        CLA
        ST I               

MAIN_LOOP:
        LD I
        CMP N
        BGE STOP            

        
        LD I
        AND #0x0003
        CMP #0
        BNE SKIP           

        
        LD I
        ASL                
        ADD BASE
        ST ADDR_LO

        
        LD ADDR_LO
        INC
        ST ADDR_HI

        
        LD (ADDR_LO)
        ST TMP_LO

        
        LD (ADDR_HI)
        AND #0x003F
        ST TMP_HI

        
        LD RES_LO
        AND TMP_LO
        ST RES_LO

        
        LD RES_HI
        AND TMP_HI
        ST RES_HI

SKIP:
        ; I++
        LD I
        INC
        ST I
        JUMP MAIN_LOOP

STOP:
        LD RES_LO
        ST 0x400

        LD RES_HI
        ST 0x401

        HLT


