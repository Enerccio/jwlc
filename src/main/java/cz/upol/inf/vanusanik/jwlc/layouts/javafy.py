#!/usr/bin/python

import sys

with open(sys.argv[1]) as f:
	while True:
		lines_buffer = f.readlines(1024)
		if not lines_buffer:
			break
		for line in lines_buffer:
			t = line.strip()
			if t.startswith("public static"):
				print("\t" + t.replace("0x", "= 0x") + ";")
			else:
				print(line.replace("\n", ""))
