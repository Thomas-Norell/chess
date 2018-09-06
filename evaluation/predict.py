import tensorflow as tf
import numpy as np
import os,glob,cv2
import sys,argparse
import dataset
def predict(myFile):
# First, pass the path of the image
    dir_path = os.path.dirname(os.path.realpath(__file__))
    image_path= myFile
    filename = str(dir_path +'/' +image_path)
    image_size=8
    num_channels=6
    images = []
    # Reading the image using OpenCV
    image = dataset.load_image(filename)
    # Resizing the image to our desired size and preprocessing will be done exactly as done during training
    x_batch = [image]
    ## Let us restore the saved model
    sess = tf.Session()
    # Step-1: Recreate the network graph. At this step only graph is created.
    saver = tf.train.import_meta_graph('eval-model.meta')
    # Step-2: Now let's load the weights saved using the restore method.
    saver.restore(sess, tf.train.latest_checkpoint('./'))

    # Accessing the default graph which we have restored
    graph = tf.get_default_graph()

    # Now, let's get hold of the op that we can be processed to get the output.
    # In the original network y_pred is the tensor that is the prediction of the network
    y_pred = graph.get_tensor_by_name("y_pred:0")

    ## Let's feed the images to the input placeholders
    x= graph.get_tensor_by_name("x:0")
    y_true = graph.get_tensor_by_name("y_true:0")
    y_test_images = np.zeros((1, len(os.listdir('training_data'))))


    ### Creating the feed_dict that is required to be fed to calculate y_pred
    feed_dict_testing = {x: x_batch, y_true: y_test_images}
    result=sess.run(y_pred, feed_dict=feed_dict_testing)
    # result is of this format [probabiliy_of_rose probability_of_sunflower]
    #print(sess.run(tf.argmax(y_pred, 1), feed_dict={x: mnist.test.images}))
    """
    builder = tf.saved_model.builder.SavedModelBuilder("/Users/thomas/Personal-Projects/chess/evaluation/pos-model" )
    builder.add_meta_graph_and_variables(
        sess,
        [tf.saved_model.tag_constants.SERVING]
        )
    builder.save()
    """
    return result
print(predict("0"))
