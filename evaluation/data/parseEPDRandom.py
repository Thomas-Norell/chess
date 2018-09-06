import random
numbers = "12345678"
symbols = ["rR", "nN", "bB", "qQ", "kK", "pP"]
def color(symbol):
    if (symbol == symbol.upper()):
        return '-1,'
    return '1,'

def parseRank(rank, channelSymbols):
    results = ""
    for square in rank:
        if square in channelSymbols:
            results += color(square)
        elif square in numbers:
            for i in range(int(square)):
                results += "0,"
        else:
            results += "0,"
    return results[:len(results) - 1]

#Converts position to list of ranks
def posToLRank(pos):
    ranks = []
    thisRank = ""
    for i in (pos):
        if i == ' ':
            ranks.append(thisRank)
            return ranks
        if i == '/':
            ranks.append(thisRank)
            thisRank = ""
        else:
            thisRank += i
    return ranks

def winner(pos):
    if int(pos[len(pos) - 2]) == 0:
        return 1 #white wins
    return -1 #black wins

def posToLines(pos): #converts a position to lines of tensor
    ranks = posToLRank(pos)
    lines = []
    for channel in symbols:
        for rank in ranks:
            lines.append(parseRank(rank, channel))
    return lines
def write(pos, count):
    position = posToLines(pos)
    if winner(pos) == 1: #white wins
        dir = "white/"
    else:
        dir = "black/"
    file = open(dir + str(count), 'w')
    for line in position:
        file.write("%s\n" % line)


with open("games3") as file:
    lines = file.readlines()
lines = [line.replace('\n', '') for line in lines]
count = 0
canWrite = True
start = 0
openZ = True
for pos in range(len(lines) - 1):
    if (lines[pos] == ""):
        start = pos + 1
        openZ = True
    if (lines[pos + 1] == "" and openZ):
        end = pos
        write(lines[random.randint(start, end)], count)
        count += 1
        openZ = False
