LATEX 	= latex -shell-escape
BIBTEX 	= bibtex
DVIPS 	= dvips
DVIPDF 	= dvipdft
XDVI	= xdvi -gamma 4
GH 		= gv

BUILDDIR = /usr/local/apps/tex_live/current/bin/x86_64-linux


EXAMPLES	= $(wildcard *.c)
SRC	  := $(shell egrep -l '^[^%]*\\begin\{document\}' *.tex)
TRG 		= $(SRC:%.tex=%.dvi)
PSF 		= $(SRC:%.tex=%.ps)
PDF 		= $(SRC:%.tex=%.pdf)

pdf: $(PDF)

ps: $(PSF)

$(TRG): %.dvi: %.tex $(EXAMPLES)
	#use pygments to include source code
	#pygmentize -f latex -o __${EXAMPLES}.tex ${EXAMPLES}

	if [ -d "$(BUILDDIR)" ] ; \
	then \
		$(BUILDDIR)/$(LATEX) $< ; \
		$(BUILDDIR)/$(BIBTEX) $(<:%.tex=%) ; \
		$(BUILDDIR)/$(LATEX) $< ; \
		$(BUILDDIR)/$(LATEX) $< ; \
	fi;
	if [ ! -d "$(BUILDDIR)" ] ; \
	then \
		$(LATEX) $< ; \
		$(BIBTEX) $(<:%.tex=%) ; \
		$(LATEX) $< ; \
		$(LATEX) $< ; \
	fi;

	#rm __${SRC}.tex
	#remove pygmentized output to avoid cluttering up directory

$(PSF):%.ps: %.dvi
	if [ -d "$(BUILDDIR)" ] ; \
	then \
		$(BUILDDIR)/$(DVIPS) -R -Poutline -t letter $< -o $@ ; \
	fi;
	if [ ! -d "$(BUILDDIR)" ] ; \
	then \
		$(DVIPS) -R -Poutline -t letter $< -o $@ ; \
	fi;

$(PDF): %.pdf: %.ps
	ps2pdf $<

show: $(TRG)
	@for i in $(TRG) ; do $(XDVI) $$i & done

showps: $(PSF)
	@for i in $(PSF) ; do $(GH) $$i & done

all: pdf

clean:
	rm -f *.ps *.dvi *.out *.log *.aux *.bbl *.blg *.pyg *.synctex.gz *.toc

.PHONY: all show clean ps pdf showps
