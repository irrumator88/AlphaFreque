# AlphaFreque
DNA sequence motif analysis

This Java code takes FASTA files as inputs (protein files in the 20-letter alphabet for scanning motifs of 6 or 8 characters. The quad pairs folder takes nucleotide encoded files (or can optimize for human, E. coli, or random).

The Matlab file quad.m is sample code to turn the output files into nicer visualizations.

RAM can be an issue when instantiating the large matrices, and sometimes adjusting the heap is necessary. For full octet analysis, it was necessary to request 500GB of RAM and reserve 480GB for the heap.
