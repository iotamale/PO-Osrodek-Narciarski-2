.PHONY: clean pdf

clean:
	rm -rf katalogMapki*
	rm -f katalogPdf/*

pdf:
	cd katalogPdf
	# dokonczyc