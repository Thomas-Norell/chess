import cv2
import os
import glob
from sklearn.utils import shuffle
import numpy as np

numbers = "12345678"
symbols = ["rR", "nN", "bB", "qQ", "kK", "pP"]
def color(symbol):
    if (symbol == symbol.upper()):
        return -1
    return 1

def parseRank(rank, channelSymbols):
    results = []
    for square in rank:
        if square in channelSymbols:
            results.append(color(square))
        elif square in numbers:
            for i in range(int(square)):
                results.append(0)
        else:
            results.append(0)
    return results

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
    tensor = np.zeros([8,8,6], dtype=int)
    lines = []
    for channel in symbols:
        for rank in ranks:
            thisChannel.append(parseRank(rank, channel))
        lines.append(thisChannel)
        thisChannel = []
    for i in range(8):
        for j in range(8):
            for k in range(6):
                tensor[i][j][k] = lines[k][i][j]
    return tensor



def load_train(train_path, image_size, classes):
    images = []
    labels = []
    img_names = []
    cls = []

    """
    #####################################################################
    for fields in classes:
        index = classes.index(fields)
        print('Now going to read {} files (Index: {})'.format(fields, index))
        path = os.path.join(train_path, fields, '*')
        files = glob.glob(path)
        for fl in files:
            image = load_image(fl)
            images.append(image)
            label = np.zeros(len(classes))
            label[index] = 1.0
            labels.append(label)
            flbase = os.path.basename(fl)
            img_names.append(flbase)
            cls.append(fields)
    #####################################################################
    """

    print("Beginning to load training data...")

    with open(train_path) as file:
        lines = file.readlines()
    lines = [line.replace('\n', '') for line in lines]
    count = 0
    import time
    from tqdm import tqdm
    for pos in tqdm(lines):
        if pos != "":
            position = posToLines(pos)
            if winner(pos) == 1: #white wins
                dir = "white"
            else:
                dir = "black"
            index = classes.index(dir)
            images.append(position)
            label = np.zeros(len(classes))
            label[index] = 1.0
            labels.append(label)
            img_names.append(str(count))
            cls.append(dir)
            count += 1


    images = np.array(images)
    labels = np.array(labels)
    img_names = np.array(img_names)
    cls = np.array(cls)

    return images, labels, img_names, cls


class DataSet(object):

  def __init__(self, images, labels, img_names, cls):
    self._num_examples = images.shape[0]

    self._images = images
    self._labels = labels
    self._img_names = img_names
    self._cls = cls
    self._epochs_done = 0
    self._index_in_epoch = 0

  @property
  def images(self):
    return self._images

  @property
  def labels(self):
    return self._labels

  @property
  def img_names(self):
    return self._img_names

  @property
  def cls(self):
    return self._cls

  @property
  def num_examples(self):
    return self._num_examples

  @property
  def epochs_done(self):
    return self._epochs_done

  def next_batch(self, batch_size):
    """Return the next `batch_size` examples from this data set."""
    start = self._index_in_epoch
    self._index_in_epoch += batch_size

    if self._index_in_epoch > self._num_examples:
      # After each epoch we update this
      self._epochs_done += 1
      start = 0
      self._index_in_epoch = batch_size
      assert batch_size <= self._num_examples
    end = self._index_in_epoch

    return self._images[start:end], self._labels[start:end], self._img_names[start:end], self._cls[start:end]


def read_train_sets(train_path, image_size, classes, validation_path):
  class DataSets(object):
    pass
  data_sets = DataSets()

  images, labels, img_names, cls = load_train(train_path, image_size, classes)
  images, labels, img_names, cls = shuffle(images, labels, img_names, cls)

  imagesV, labelsV, img_namesV, clsV = load_train(validation_path, image_size, classes)
  imagesV, labelsV, img_namesV, clsV = shuffle(imagesV, labelsV, img_namesV, clsV)

  validation_images = imagesV[:]
  validation_labels = labelsV[:]
  validation_img_names = img_namesV[:]
  validation_cls = clsV[:]

  train_images = images[:]
  train_labels = labels[:]
  train_img_names = img_names[:]
  train_cls = cls[:]

  data_sets.train = DataSet(train_images, train_labels, train_img_names, train_cls)
  data_sets.valid = DataSet(validation_images, validation_labels, validation_img_names, validation_cls)

  return data_sets

def lineToRow(line):
    row = []
    currentObj = ""
    for char in line:
        if char == ',':
            row.append(currentObj)
            currentObj = ''
        else:
            currentObj += char
    row.append(currentObj)
    return row

"""
def load_image(filename):
    with open(filename) as file:
        lines = file.readlines()
    lines = [line.replace('\n', '') for line in lines]
    image = [None] * 8
    for i in range(0, len(image)):
        image[i] = [None] * 8
        for j in range(len(image[i])):
            image[i][j] = [None] * 6

    for line in range(len(lines)):
        row = lineToRow(lines[line])
        for char in range(len(row)):
            image[line % 8][char][line // 8] = row[char]
    return image
"""
