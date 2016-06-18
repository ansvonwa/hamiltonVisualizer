# Visualizer for hamilton path contest

This software visualizes an algorithm which is calculating the
number of hamilton paths from one node to another. This was written
for a contest at my university.

## Converting the input

Some example data for the contest is provided as a simple text file.
To have a good layout of the graph, you need to execute the python script
`txt2dot.py` in this repository to convert the input into the dot file
format. After that, you need graphviz to generate another text file,
which then contains coordinates of the nodes. These coordinates help
rendering the nodes to a nice grid.