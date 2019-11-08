import argparse
import os

parser = argparse.ArgumentParser(description='Receive files to analyze')

parser.add_argument('-f', "--file", required=True, type=str,
                        help="base name of the log file, ex: \"J5_Log\" to \"J5_Log_01\" ")
		
parser.add_argument('-d', '--dir', required=False, type=str, 
						help="Directory to find the log files")
		
args = parser.parse_args()


out = open(args.file.replace('.txt', '') + '_agreggated.csv', 'w')


if args.dir == None:
	files = os.listdir('.')
else:
	files = os.listdir(args.dir)
log_files = []
for file in files:
	if file.find(args.file) >= 0 and file.find('processed.txt') >=0:
		if args.dir == None:
			log_files.append(file)
		else:
			log_files.append(args.dir + '/' + file)


logs = {}

for file in log_files:
		inp = open(file, 'r')

		previousLine = ''
		for line in inp.readlines():
			
			if previousLine.find('Repetition') != -1 and line.find('Time') != -1:
				# Log válido
				img = previousLine[previousLine.find('downloading image of a:') + 24:len(previousLine) - 1]
				impl = previousLine[previousLine.find("of") + 3:previousLine.find('implementation')-1]
				time = int(line[line.find('Time') + 6:line.find('ms')-1])
				
				if logs.get(impl) == None:
					logs[impl] = {}
				if logs[impl].get(img) == None:
					logs[impl][img] = []
				logs[impl][img].append(time)
				
			previousLine = line
		
out.write(';Image download;\n')
imgs = ["Cat", "Dog", "Lion", "Platypus", "Pigeon"]
impls = ['Threads', 'ThreadPool', 'HaMeR framework', 'Kotlin coroutines', 'AsyncTask', 'IntentServices']
repetitions = 32

for img in imgs:
	out.write(str(img) + ';\n')
	out.write('\n;;')

	for impl in impls:
		out.write(str(impl)+';')
	out.write(';\n')
	for r in range(repetitions):
		out.write(';;')
		for impl in impls:
			if (len(logs[impl][img]) > r):
				out.write(str(logs[impl][img][r])+';')
			else:
				out.write(';')
		out.write(';\n')
	out.write(';\n;\n;\n')


