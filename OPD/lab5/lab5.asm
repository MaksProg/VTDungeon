ORG 0x010
RES_ADDRESS: WORD $RES
EOF: WORD 0x0d
TMP: WORD ? 

ORG 0x042
START:			CLA

FIRST_SYMBOL:	IN 5
				AND #0x40
				BEQ FIRST_SYMBOL
				
				IN 4
				ST (RES_ADDRESS)
				
				CMP EOF
				BEQ STOP

SECOND_SYMBOL:	IN 5
				AND #0x040
				BEQ SECOND_SYMBOL
				
				IN 4
				ST TMP
				SWAB
				ADD (RES_ADDRESS)
				ST (RES_ADDRESS)+
				
				LD TMP
				CMP EOF
				BEQ STOP
				
				JUMP FIRST_SYMBOL

STOP:		HLT

ORG 0X646
RES: WORD ?