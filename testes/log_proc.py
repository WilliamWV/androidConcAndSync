import argparse

parser = argparse.ArgumentParser(description='Receive files to simplify')

parser.add_argument('-f', "--file", required=True, type=str,
                        help="name of the file with to process")

args = parser.parse_args()
inp = open(args.file, 'r')
out = open(args.file.replace('.txt', '') + '_processed.txt', 'w')

for line in inp.readlines():
	line=line.replace(line[0:line.find('TEST') + 6], '')
	out.write(line)
