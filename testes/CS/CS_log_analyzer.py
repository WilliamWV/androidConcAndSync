import argparse
import os

parser = argparse.ArgumentParser(description='Receive files to analyze')

parser.add_argument('-f', "--file", required=True, type=str,
                        help="base name of the log file, ex: \"J5_Log\" to \"J5_Log_01\" ")
						
args = parser.parse_args()


out = open(args.file.replace('.txt', '') + '_agreggated.csv', 'w')


files = os.listdir('.')
log_files = []
for file in files:
	if file.find(args.file) >= 0 and file.find('processed.txt') >=0:
		log_files.append(file)
		
		
logs = {}

for file in log_files:
		inp = open(file, 'r')

		previousLine = ''
		for line in inp.readlines():
			
			if previousLine.find('Repetition') != -1 and line.find('Time') != -1:
				# Log válido
				nums = int(previousLine[previousLine.find('adding') + 7:previousLine.find('numbers')-1])
				tasks = int(previousLine[previousLine.find('using') + 6:previousLine.find('tasks') - 1])
				impl = previousLine[previousLine.find("of") + 3:previousLine.find('implementation')-1]
				time = int(line[line.find('Time') + 6:line.find('ms')-1])
				if logs.get(impl) == None:
					logs[impl] = {}
				if logs[impl].get(nums) == None:
					logs[impl][nums] = {}
				if logs[impl][nums].get(tasks) == None:
					logs[impl][nums][tasks] = []
				
				logs[impl][nums][tasks].append(time)
				
			previousLine = line
		
out.write(';Concurrent sum;\n')
nums = [262144, 1048576]
tasks = [1, 16, 256]
impls = ['Threads', 'ThreadPool', 'HaMeR framework', 'Kotlin coroutines', 'Threads with barriers']
repetitions = 30
for n in nums:
	out.write(str(n) + ';\n')
	out.write('\n')
	for t in tasks:
		out.write(';'+str(t) + ' Tasks' + ';;;;;;;')
	out.write(';\n;;')
	for t in tasks:
		for i in impls:
			out.write(str(i)+';')
		out.write(';;;')
	
	out.write(';\n')
	for r in range(repetitions):
		out.write(';;')
		for t in tasks:
			for i in impls:
				if (len(logs[i][n][t]) > r):
					out.write(str(logs[i][n][t][r])+';')
				else:
					out.write(';')
			out.write(';;;')
		out.write(';\n')
	out.write(';\n;\n;\n')


