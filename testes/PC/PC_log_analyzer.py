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

		prev_prev_line = ''
		prev_line = ''
		for line in inp.readlines():
			
			if prev_prev_line.find('Repetition') != -1 and prev_line.find('Produced') != -1 and line.find('Consumed') != -1:
				# Log válido
				buff = int(prev_prev_line[prev_prev_line.find(', and') + 6:prev_prev_line.find('buffer')-1])
				prod = int(prev_prev_line[prev_prev_line.find('with') + 5:prev_prev_line.find('producers')-1])
				cons = int(prev_prev_line[prev_prev_line.find('producers and') + 14:prev_prev_line.find('consumers')-1])
				impl = prev_prev_line[prev_prev_line.find("of") + 3:prev_prev_line.find('implementation')-1]
				prod_items = int(prev_line[prev_line.find('Produced items:') + 16:])
				cons_items = int(line[line.find('Consumed items:')+16:])
				
				
				if logs.get(impl) == None:
					logs[impl] = {}
				if logs[impl].get(buff) == None:
					logs[impl][buff] = {}
				if logs[impl][buff].get(prod) == None:
					logs[impl][buff][prod] = {}
				if logs[impl][buff][prod].get(cons) == None:
					logs[impl][buff][prod][cons] = []
				
				logs[impl][buff][prod][cons].append((prod_items, cons_items))
				
			prev_prev_line = prev_line
			prev_line = line
			
		
out.write(';Producers-Consumers problem;\n')
buffs = [2,8]
prods = [5,10,20]
cons = [5,10,20]
impls = ['Semaphore', 'Lock and Condition', 'Atomic variables', 'Synchronized']
repetitions = 30
for p in prods:
	for c in cons:
		out.write(';' + str(p) + ' producers and ' + str(c) + ' consumers;\n')
		for b in buffs:
			out.write(';;Buffer = ' + str(b) + ';;;;;;;;;;;;')
		
			
		out.write('\n;;Produced items;;;;;;;Consumed items;;;;;;;Produced items;;;;;;;Consumed items\n;;')
		
		
		for b in buffs:
		
			for i in impls:
				out.write(str(i)+';')
			out.write(';;;')
		
			for i in impls:
				out.write(str(i)+';')
			
			out.write(';;;')
			
			
		out.write(';\n')
		for r in range(repetitions):
			out.write(';;')
			for b in buffs:
				for i in impls:
					if (len(logs[i][b][p][c]) > r):
						out.write(str(logs[i][b][p][c][r][0])+';')
					else:
						out.write(';')
				out.write(';;;')
				for i in impls:
					if (len(logs[i][b][p][c]) > r):
						out.write(str(logs[i][b][p][c][r][1])+';')
					else:
						out.write(';')
				
				
				out.write(';;;')
					
			out.write(';\n')
		out.write(';\n;\n;\n')


