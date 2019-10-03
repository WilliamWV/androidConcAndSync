import argparse

parser = argparse.ArgumentParser(description='Receive files to analyze')

parser.add_argument('-f', "--file", required=True, type=str,
                        help="name of the file with to process")
parser.add_argument('-p', '--problem', required=True, type=str,
						help="type MM or CS or PH or ID or PC")

args = parser.parse_args()
inp = open(args.file, 'r')

out = open(args.file.replace('.txt', '') + '_anal.csv', 'w')

if args.problem == 'MM':
	logs = {}
	previousLine = ''
	for line in inp.readlines():
		
		if previousLine.find('Repetition') != -1 and line.find('Time') != -1:
			# Log válido
			#print (previousLine)
			#print (str(previousLine.find('size') + 6))
			#print (previousLine.find('x', previousLine.find('size') + 6))
			#print (previousLine[previousLine.find('size') + 6:previousLine.find('x')])
			size = int(previousLine[previousLine.find('size') + 6:previousLine.find('x', previousLine.find('size') + 6)])
			tasks = int(previousLine[previousLine.find('using') + 6:previousLine.find('tasks') - 1])
			impl = previousLine[previousLine.find("of") + 3:previousLine.find('implementation')-1]
			time = int(line[line.find('Time') + 6:line.find('ms')-1])
			if logs.get(impl) == None:
				logs[impl] = {}
			if logs[impl].get(size) == None:
				logs[impl][size] = {}
			if logs[impl][size].get(tasks) == None:
				logs[impl][size][tasks] = []
			
			logs[impl][size][tasks].append(time)
			
		previousLine = line
	
	out.write(';Matrix multiplication;\n')
	sizes = [128, 256, 512]
	tasks = [1, 2, 8, 64]
	impls = ['Threads', 'ThreadPool', 'HaMeR framework', 'Kotlin coroutines']
	repetitions = 10
	for s in sizes:
		out.write(str(s) + 'x' + str(s) + ';\n')
		out.write('\n')
		for t in tasks:
			out.write(';'+str(t) + 'Tasks' + ';\n;;')
			for i in impls:
				out.write(str(i)+';')
			out.write(';\n')
			for r in range(repetitions):
				out.write(';;')
				for i in impls:
					if (len(logs[i][s][t]) > r):
						out.write(str(logs[i][s][t][r])+';')
					else:
						out.write(';')
				out.write(';\n')


