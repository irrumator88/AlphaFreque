# AlphaFreque
DNA sequence motif analysis

This Java code takes FASTA files as inputs (protein files in the 20-letter alphabet for scanning motifs of 6 or 8 characters. The quad pairs folder takes nucleotide encoded files (or can optimize for human, E. coli, or random).

The Matlab file quad.m is sample code to turn the output files into nicer visualizations.

RAM can be an issue when instantiating the large matrices, and sometimes adjusting the heap is necessary. For full octet analysis, it was necessary to request 500GB of RAM and reserve 480GB for the heap.

This tool is provided as part of a dissertation titled "Application of Software Engineering Principles to Synthetic Biology and Emerging Regulatory Concerns" with the University of Nebraska-Lincoln. Please cite that dissertation if you use the tool in your research.

Funding for this research has been provided by the National Institute of Justice (NIJ) Grant 2016-R2-CX-0023. The views and conclusions of this tool are those of the author and do not necessarily reflect the position or policy of the NIJ.

This tool is provided as-is and as-available, with no representations or warranties of any kind.
