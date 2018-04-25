all: sprint0.pdf specyfikacja.pdf architektura.pdf

sprint0.pdf: sprint0.tex
	mkdir -p sprint0
	pdflatex -file-line-error -shell-escape -halt-on-error -output-directory sprint0 sprint0.tex

specyfikacja.pdf: specyfikacja.tex
	mkdir -p sprint1
	pdflatex -file-line-error -shell-escape -halt-on-error -output-directory sprint1 specyfikacja.tex


architektura.pdf: architektura.tex
	mkdir -p sprint2
	pdflatex -file-line-error -shell-escape -halt-on-error -output-directory sprint2 architektura.tex

clean:
	rm -rf sprint0
	rm -rf sprint1
	rm -rf sprint2
