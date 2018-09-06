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


with open("games7") as file:
    lines = file.readlines()
lines = [line.replace('\n', '') for line in lines]
count = 70000000
for pos in lines:
    if (count % 1000 == 0):
        print(count)
    if pos != "":
        position = posToLines(pos)
        if winner(pos) == 1: #white wins
            dir = "white/"
        else:
            dir = "black/"
        file = open(dir + str(count), 'w')
        for line in position:
            file.write("%s\n" % line)
        count += 1
