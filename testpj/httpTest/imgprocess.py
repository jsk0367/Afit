from tensorflow.keras.models import Sequential, load_model
from PIL import Image, ImageEnhance, ImageChops
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img
import mediapipe as mp
import math
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import os
from matplotlib.image import imread
import cv2
import glob
import tensorflow as tf
from tensorflow.keras.layers import LeakyReLU
from tensorflow.python.keras.layers import Conv2D, MaxPool2D, Dense, Flatten, Dropout
from tensorflow.keras.optimizers import Adam
from .form_check import form_check


path = '/home/ubuntu/testpj/'

def imgprocess():
    model = load_model(path + 'models/train_model_2.h5', custom_objects={'LeakyReLU': LeakyReLU})
    mp_drawing = mp.solutions.drawing_utils
    mp_drawing_styles = mp.solutions.drawing_styles
    mp_pose = mp.solutions.pose

    #file = (path + 'media/*')
    
    name = glob.glob(path + 'media/*')
#    name = name[0].split('/')[-1].split('.')[0]
    filepath = name[0].split('/')[-1]

    file = (path + 'media/%s' % filepath)
    name = filepath.split('.')[0]
    
    BG_COLOR = (192, 192, 192)  # gray
    with mp_pose.Pose(
            static_image_mode=True,
            model_complexity=2,
            enable_segmentation=True,
            min_detection_confidence=0.5) as pose:
        # for idx, file in enumerate(image):
        image = cv2.imread(file)
        image_height, image_width, _ = image.shape
        # Convert the BGR image to RGB before processing.
        results = pose.process(cv2.cvtColor(image, cv2.COLOR_BGR2RGB))

    squat_pose = [results.pose_landmarks.landmark[11], results.pose_landmarks.landmark[12]]
    squat_pose_x = []
    squat_pose_y = []
    squat_pose_z = []

    for i in range(23, 33):
        squat_pose.append(results.pose_landmarks.landmark[i])

    for i in range(12):
        squat_pose_x.append(squat_pose[i].x)
        squat_pose_y.append(squat_pose[i].y)
        squat_pose_z.append(squat_pose[i].z)

    squat = pd.DataFrame({'x': squat_pose_x, 'y': squat_pose_y, 'z': squat_pose_z},
                         index=['L_SHOULDER', 'R_SHOULDER',
                                'L_HIP', 'R_HIP',
                                'L_KNEE', 'R_KNEE',
                                'L_ANKLE', 'R_ANKLE',
                                'L_HEEL', 'R_HEEL',
                                'L_FOOT', 'R_FOOT']).T

    SHOULDER_x_line = np.linspace(squat['L_SHOULDER']['x'], squat['R_SHOULDER']['x'], 1000)
    SHOULDER_y_line = np.linspace(squat['L_SHOULDER']['y'], squat['R_SHOULDER']['y'], 1000)
    SHOULDER_z_line = np.linspace(squat['L_SHOULDER']['z'], squat['R_SHOULDER']['z'], 1000)

    HIP_x_line = np.linspace(squat['L_HIP']['x'], squat['R_HIP']['x'], 1000)
    HIP_y_line = np.linspace(squat['L_HIP']['y'], squat['R_HIP']['y'], 1000)
    HIP_z_line = np.linspace(squat['L_HIP']['z'], squat['R_HIP']['z'], 1000)

    KNEE_x_line = np.linspace(squat['L_KNEE']['x'], squat['R_KNEE']['x'], 1000)
    KNEE_y_line = np.linspace(squat['L_KNEE']['y'], squat['R_KNEE']['y'], 1000)
    KNEE_z_line = np.linspace(squat['L_KNEE']['z'], squat['R_KNEE']['z'], 1000)

    L_TOP_x_line = np.linspace(squat['L_SHOULDER']['x'], squat['L_HIP']['x'], 1000)
    L_TOP_y_line = np.linspace(squat['L_SHOULDER']['y'], squat['L_HIP']['y'], 1000)
    L_TOP_z_line = np.linspace(squat['L_SHOULDER']['z'], squat['L_HIP']['z'], 1000)

    R_TOP_x_line = np.linspace(squat['R_SHOULDER']['x'], squat['R_HIP']['x'], 1000)
    R_TOP_y_line = np.linspace(squat['R_SHOULDER']['y'], squat['R_HIP']['y'], 1000)
    R_TOP_z_line = np.linspace(squat['R_SHOULDER']['z'], squat['R_HIP']['z'], 1000)

    L_MIDDLE_1_x_line = np.linspace(squat['L_HIP']['x'], squat['L_KNEE']['x'], 1000)
    L_MIDDLE_1_y_line = np.linspace(squat['L_HIP']['y'], squat['L_KNEE']['y'], 1000)
    L_MIDDLE_1_z_line = np.linspace(squat['L_HIP']['z'], squat['L_KNEE']['z'], 1000)

    R_MIDDLE_1_x_line = np.linspace(squat['R_HIP']['x'], squat['R_KNEE']['x'], 1000)
    R_MIDDLE_1_y_line = np.linspace(squat['R_HIP']['y'], squat['R_KNEE']['y'], 1000)
    R_MIDDLE_1_z_line = np.linspace(squat['R_HIP']['z'], squat['R_KNEE']['z'], 1000)

    L_MIDDLE_2_x_line = np.linspace(squat['L_KNEE']['x'], squat['L_HEEL']['x'], 1000)
    L_MIDDLE_2_y_line = np.linspace(squat['L_KNEE']['y'], squat['L_HEEL']['y'], 1000)
    L_MIDDLE_2_z_line = np.linspace(squat['L_KNEE']['z'], squat['L_HEEL']['z'], 1000)

    R_MIDDLE_2_x_line = np.linspace(squat['R_KNEE']['x'], squat['R_HEEL']['x'], 1000)
    R_MIDDLE_2_y_line = np.linspace(squat['R_KNEE']['y'], squat['R_HEEL']['y'], 1000)
    R_MIDDLE_2_z_line = np.linspace(squat['R_KNEE']['z'], squat['R_HEEL']['z'], 1000)

    L_BOTTOM_x_line = np.linspace(squat['L_HEEL']['x'], squat['L_FOOT']['x'], 1000)
    L_BOTTOM_y_line = np.linspace(squat['L_HEEL']['y'], squat['L_FOOT']['y'], 1000)
    L_BOTTOM_z_line = np.linspace(squat['L_HEEL']['z'], squat['L_FOOT']['z'], 1000)

    R_BOTTOM_x_line = np.linspace(squat['R_HEEL']['x'], squat['R_FOOT']['x'], 1000)
    R_BOTTOM_y_line = np.linspace(squat['R_HEEL']['y'], squat['R_FOOT']['y'], 1000)
    R_BOTTOM_z_line = np.linspace(squat['R_HEEL']['z'], squat['R_FOOT']['z'], 1000)

    x_max = max(squat.T.x)
    x_min = min(squat.T.x)
    x = (x_max + x_min) / 5

    y_max = max(squat.T.y)
    y_min = min(squat.T.y)
    y = (y_max + y_min) / 10

    z_max = max(squat.T.z)
    z_min = min(squat.T.z)
    z = (z_max - z_min)

    fig = plt.figure()
    # create data for 3d line
    # 3d container
    ax = plt.axes(projection='3d')

    # 3d scatter plot
    ax.plot3D(SHOULDER_z_line, SHOULDER_x_line, SHOULDER_y_line)
    ax.plot3D(L_TOP_z_line, L_TOP_x_line, L_TOP_y_line)
    ax.plot3D(R_TOP_z_line, R_TOP_x_line, R_TOP_y_line)
    ax.plot3D(HIP_z_line, HIP_x_line, HIP_y_line)
    ax.plot3D(L_MIDDLE_1_z_line, L_MIDDLE_1_x_line, L_MIDDLE_1_y_line)
    ax.plot3D(R_MIDDLE_1_z_line, R_MIDDLE_1_x_line, R_MIDDLE_1_y_line)
    ax.plot3D(L_MIDDLE_2_z_line, L_MIDDLE_2_x_line, L_MIDDLE_2_y_line)
    ax.plot3D(R_MIDDLE_2_z_line, R_MIDDLE_2_x_line, R_MIDDLE_2_y_line)
    ax.plot3D(L_BOTTOM_z_line, L_BOTTOM_x_line, L_BOTTOM_y_line)
    ax.plot3D(R_BOTTOM_z_line, R_BOTTOM_x_line, R_BOTTOM_y_line)
    ax.set_xlabel('z')
    ax.set_ylabel('x')
    ax.set_zlabel('y')
    plt.ylim(x_min - x, x_max + x)
    ax.set_zlim(y_min, y_max)
    plt.xlim(z_min - z, z_max + z)

    ax.grid(False)
    ax.set_axis_off()
    ax.view_init(elev=180, azim=180)

    plt.savefig(path + 'media/%s_result.jpg' % (name))

    img = Image.open(path + 'media/%s_result.jpg' % (name))
    img = img.convert('RGB')
    img = img.resize((400, 400))
    data = np.asarray(img)
    data = np.array(img)
    data = data.reshape(-1, 400, 400, 3)
    result = model.predict(data)
    result = result.argmax()
    
    
    if result == 1:
        
        #feedback = {'sqartyn': "not squat"}
        feedback = {'sqartyn': "이 사진은 스쿼트 자세가 아닙니다."}
        return feedback
    else:
        return form_check(path+'media/'+filepath)
