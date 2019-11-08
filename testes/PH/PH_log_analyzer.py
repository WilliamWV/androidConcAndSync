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

		prev_prev_line = ''
		prev_line = ''
		for line in inp.readlines():
			
			if prev_prev_line.find('Repetition') != -1 and prev_line.find('Average') != -1 and line.find('Min') != -1:
				# Log válido
				time = int(prev_prev_line[prev_prev_line.find('running by') + 11:prev_prev_line.find('seconds')-1])
				philosophers = int(prev_prev_line[prev_prev_line.find('with') + 5:prev_prev_line.find('philosophers')-1])
				impl = prev_prev_line[prev_prev_line.find("of") + 3:prev_prev_line.find('implementation')-1]
				sync = prev_prev_line[prev_prev_line.find("using") + 6: prev_prev_line.find('to synchronize')-1]
				avg = float(prev_line[prev_line.find('Average executions:') + 20:prev_line.find('; Standard deviation:')])
				std = float(prev_line[prev_line.find('Standard deviation:')+20:])
				mini = int(line[line.find('Min: ') + 5:line.find('; Max:')])
				maxi = int(line[line.find('; Max: ') + 7:])
				if logs.get(impl) == None:
					logs[impl] = {}
				if logs[impl].get(time) == None:
					logs[impl][time] = {}
				if logs[impl][time].get(philosophers) == None:
					logs[impl][time][philosophers] = {}
				if logs[impl][time][philosophers].get(sync) == None:
					logs[impl][time][philosophers][sync] = []
				
				logs[impl][time][philosophers][sync].append((avg, std, mini, maxi))
				
			prev_prev_line = prev_line
			prev_line = line
			
		
out.write(';Philosophers problem;\n')
times = [2]
philosophers = [5, 11, 51]
impls = ['Threads', 'ThreadPool', 'HaMeR framework', 'Kotlin coroutines']
sync = ['Semaphore', 'Synchronized', 'Lock and Condition']

repetitions = 30
for p in philosophers:
	out.write(str(p) + ' Philosophers;\n')
	for t in times:
		out.write('Running by ' + str(t) + 's;\n')
		for s in sync:
			out.write('Using ' + str(s) + ';;;;;;;;;;;;;')
		out.write(';\n')
		out.write('; Average executions;;;;;;Standard deviation;;;;;;;Average executions;;;;;;Standard deviation;;;;;;;Average executions;;;;;;Standard deviation\n;;')

		for s in sync:
			for i in impls:
				out.write(str(i)+';')
			out.write(';;')
			
			for i in impls:
				out.write(str(i)+';')
			out.write(';;;')
		
		out.write(';\n')
		for r in range(repetitions):
			out.write(';;')
			for s in sync:
				for i in impls:
					if (len(logs[i][t][p][s]) > r):
						out.write(str(logs[i][t][p][s][r][0])+';')
					else:
						out.write(';')
				out.write(';;')
				for i in impls:
					if (len(logs[i][t][p][s]) > r):
						out.write(str(logs[i][t][p][s][r][1])+';')
					else:
						out.write(';')
				out.write(';;;')
			out.write(';\n')
		
		out.write(';\n\n\n')
		out.write('; Min;;;;;;Max;;;;;;;Min;;;;;;Max;;;;;;;Min;;;;;;Max\n;;')

		for s in sync:
			for i in impls:
				out.write(str(i)+';')
			out.write(';;')
			
			for i in impls:
				out.write(str(i)+';')
			out.write(';;;')
		
		out.write(';\n')
		for r in range(repetitions):
			out.write(';;')
			for s in sync:
				for i in impls:
					if (len(logs[i][t][p][s]) > r):
						out.write(str(logs[i][t][p][s][r][2])+';')
					else:
						out.write(';')
				out.write(';;')
				for i in impls:
					if (len(logs[i][t][p][s]) > r):
						out.write(str(logs[i][t][p][s][r][3])+';')
					else:
						out.write(';')
				out.write(';;;')
			out.write(';\n')
	out.write(';\n;\n;\n')


