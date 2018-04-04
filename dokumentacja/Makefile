all: sprint0.pdf specyfikacja.pdf

sprint0.pdf: sprint0.tex
	mkdir -p sprint0
	pdflatex -file-line-error -shell-escape -halt-on-error -output-directory sprint0 sprint0.tex

specyfikacja.pdf: specyfikacja.tex
	mkdir -p sprint1
	pdflatex -file-line-error -shell-escape -halt-on-error -output-directory sprint1 specyfikacja.tex

clean:
	rm -rf sprint0
	rm -rf sprint1
