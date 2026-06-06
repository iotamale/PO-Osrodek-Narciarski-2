.PHONY: clean pdf

clean:
	rm -rf katalogMapki*
	rm *.aux *.log *.pdf

pdf:
	cd katalogPdf
	# dokonczyc