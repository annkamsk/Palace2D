all: build/sprint0.pdf

build/sprint0.pdf: sprint0.tex
	mkdir -p build
	pdflatex -file-line-error -shell-escape -halt-on-error -output-directory build sprint0.tex

clean:
	rm -rf build
