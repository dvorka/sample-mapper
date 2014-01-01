#------------------------------------------------------------------------------
#
#	                   JNaaga makefile
#
#				Dvorka
#				 1999
#------------------------------------------------------------------------------

GOAL=SampleMapping

#JPP=javac
#JPPFLAGS= -deprecation
JPP=jikes
JPPFLAGS= +E

DSTS=   SampleMapping.class  \
	CombCanvas.class     \
	KeybCanvas.class     \
	KeybRecord.class     \
	SMStatus.class       \
	DelayFrame.class     \
	EnterNameFrame.class \
	AppletButton.class   \
	AboutFrame.class	



compile: soft $(DSTS)
	true

%.class: %.java
	$(JPP) $(JPPFLAGS) $<
        
go:     show
	true        
show:   compile
	java $(GOAL)



soft:
	rm -vf *~
clean: soft
	rm -vf *.class
javap:
	javap -c $(GOAL)

#- EOF ------------------------------------------------------------------------
