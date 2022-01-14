import os
import cv2
import math
import glob
import colorsys
import numpy as np
import pandas as pd
from PIL import Image
import mediapipe as mp
from math import acos, degrees
import matplotlib.pyplot as plt
from matplotlib.image import imread

path = '/home/ubuntu/testpj/AIPT'

def form_check(file):
    
    mp_drawing = mp.solutions.drawing_utils
    mp_drawing_styles = mp.solutions.drawing_styles
    mp_pose = mp.solutions.pose

    BG_COLOR = (192, 192, 192) # gray
    with mp_pose.Pose(
        static_image_mode=True,
        model_complexity=2,
        enable_segmentation=True,
        min_detection_confidence=0.5) as pose:
            #for idx, file in enumerate(image):
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

    squat = pd.DataFrame({'x':squat_pose_x, 'y':squat_pose_y, 'z':squat_pose_z},
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
    x = (x_max + x_min)/5

    y_max = max(squat.T.y)
    y_min = min(squat.T.y)
    y = (y_max + y_min)/30

    z_max = max(squat.T.z)
    z_min = min(squat.T.z)
    z = (z_max - z_min)

    name = file.split('/')[-1].split('.')[0]

    fig = plt.figure(figsize=(12, 12))
    ax = plt.axes(projection = '3d')
    ax.scatter(squat['L_SHOULDER'].z, squat['L_SHOULDER'].x, squat['L_SHOULDER'].y, c='green', s=0.01)
    ax.scatter(squat['L_KNEE'].z, squat['L_KNEE'].x, squat['L_KNEE'].y, c='red', s=0.01)
    ax.scatter(squat['L_HIP'].z, squat['L_HIP'].x, squat['L_HIP'].y, c='blue', s=0.01)
    ax.set_xlabel('z')
    ax.set_ylabel('x')
    ax.set_zlabel('y')
    plt.ylim(x_min-x, x_max+x)
    ax.set_zlim(y_min, y_max)
    plt.xlim(z_min*3, z_max*3)
    ax.grid(False)
    ax.set_axis_off()
    ax.view_init(elev=180, azim=90)
    try :
        os.makedirs(path + '/Angle_Image/Squat/Point/Waist')
    except :
        pass
    plt.savefig(path + '/Angle_Image/Squat/Point/Waist/%s_waist.jpg'%(name))
    ax.plot3D(R_TOP_z_line, R_TOP_x_line, R_TOP_y_line)
    ax.plot3D(R_MIDDLE_1_z_line, R_MIDDLE_1_x_line, R_MIDDLE_1_y_line)
    plt.savefig(path + '/Angle_Image/Squat/Point/Waist/%s_waist_sub.jpg'%(name))

    fig = plt.figure(figsize=(12, 12))
    ax = plt.axes(projection = '3d')
    ax.scatter(squat['L_HEEL'].z, squat['L_HEEL'].x, squat['L_HEEL'].y, c='green', s=0.01)
    ax.scatter(squat['L_KNEE'].z, squat['L_KNEE'].x, squat['L_KNEE'].y, c='red', s=0.01)
    ax.scatter(squat['R_KNEE'].z, squat['R_KNEE'].x, squat['R_KNEE'].y, c='blue', s=0.01)
    ax.set_xlabel('z')
    ax.set_ylabel('x')
    ax.set_zlabel('y')
    plt.ylim(x_min-x, x_max+x)
    ax.set_zlim(y_min, y_max)
    plt.xlim(z_min*3, z_max*3)
    ax.grid(False)
    ax.set_axis_off()
    ax.view_init(elev=180, azim=180)
    try :
        os.makedirs(path + '/Angle_Image/Squat/Point/Knee')
    except :
        pass
    plt.savefig(path + '/Angle_Image/Squat/Point/Knee/%s_knee.jpg'%(name))
    ax.plot3D(KNEE_z_line, KNEE_x_line, KNEE_y_line)
    ax.plot3D(L_MIDDLE_2_z_line, L_MIDDLE_2_x_line, L_MIDDLE_2_y_line)
    plt.savefig(path + '/Angle_Image/Squat/Point/Knee/%s_knee_sub.jpg'%(name))

    fig = plt.figure(figsize=(12, 12))
    ax = plt.axes(projection = '3d')
    ax.scatter(squat['L_HIP'].z, squat['L_HIP'].x, squat['L_HIP'].y, c='green', s=0.01)
    ax.scatter(squat['L_KNEE'].z, squat['L_KNEE'].x, squat['L_KNEE'].y, c='red', s=0.01)
    # 허벅지-> 무릎-> 무릎에서 아래로 내린 땅으로 각도 뽑아내기 위해 scatter 첫번째 값 L_HEEL에서 L_KNEE로 변경
    ax.scatter(squat['L_KNEE'].z, squat['L_HEEL'].x, squat['L_HEEL'].y, c='blue', s=0.01)
    ax.set_xlabel('z')
    ax.set_ylabel('x')
    ax.set_zlabel('y')
    plt.ylim(x_min-x, x_max+x)
    ax.set_zlim(y_min, y_max)
    plt.xlim(z_min*3, z_max*3)
    ax.grid(False)
    ax.set_axis_off()
    ax.view_init(elev=180, azim=90)
    try :
        os.makedirs(path + '/Angle_Image/Squat/Point/Squat')
    except :
        pass
    plt.savefig(path + '/Angle_Image/Squat/Point/Squat/%s_squat.jpg'%name)
    # 허벅지-> 무릎-> 무릎에서 아래로 내린 땅으로 각도 뽑아내기 위해 R_MIDDEL_2_z_line을 변경
    R_MIDDLE_2_z_line = np.linspace(squat['R_KNEE']['z'], squat['R_KNEE']['z'], 1000)
    ax.plot3D(R_MIDDLE_1_z_line, R_MIDDLE_1_x_line, R_MIDDLE_1_y_line)
    ax.plot3D(R_MIDDLE_2_z_line, R_MIDDLE_2_x_line, R_MIDDLE_2_y_line)
    plt.savefig(path + '/Angle_Image/Squat/Point/Squat/%s_squat_sub.jpg'%name)

    return result(name)

    # 변의 길이 계산식
def distance(x1, y1, x2, y2):
    result = math.sqrt( math.pow(x1 - x2, 2) + math.pow(y1 - y2, 2))
    return result

# 허리 각도 계산식
def waist_angle(name):
    img_name = (path + '/Angle_Image/Squat/Point/Waist/%s_waist.jpg'%name)
    im = Image.open(img_name)
    pix = im.load()
    A_C = []
    for i in range(im.size[0]):
        for j in range(im.size[1]):
            if pix[i,j][0] < 245 or pix[i,j][1] < 245 or pix[i,j][2] < 245:
                A_C.append([i,j])

    num = [0, 1, 2]
    x = [A_C[0][0],A_C[1][0],A_C[2][0]]
    y = [A_C[0][1],A_C[1][1],A_C[2][1]]
    fi = 0
    se = 0
    th = 0
    for i in num:
        if min(y) == y[i]:
            fi = i
            num.remove(i)
            break;
    if x[num[0]] < x[num[1]]:
        se = num[0]
    else :
        se = num[1]
    th = 3-(fi+se)
    a = distance(x[fi], y[fi], x[se], y[se])
    b = distance(x[se], y[se], x[th], y[th])
    c = distance(x[th], y[th], x[fi], y[fi])
    waist_result = degrees(acos((a**2 + b**2 - c**2) / (2*a*b)))
    return waist_result

# 무릎 각도 계산식
def knee_angle(name):
    img_name = (path + '/Angle_Image/Squat/Point/Knee/%s_knee.jpg'%name)
    im = Image.open(img_name)
    pix = im.load()
    A_C = []
    for i in range(im.size[0]):
        for j in range(im.size[1]):
            if pix[i,j][0] < 245 or pix[i,j][1] < 245 or pix[i,j][2] < 245:
                A_C.append([i,j])

    num = [0, 1, 2]
    x = [A_C[0][0],A_C[1][0],A_C[2][0]]
    y = [A_C[0][1],A_C[1][1],A_C[2][1]]
    fi = 0
    se = 0
    th = 0
    for i in num:
        if max(y) == y[i]:
            fi = i
            num.remove(i)
            break;
    if x[num[0]] < x[num[1]]:
        se = num[0]
    else :
        se = num[1]

    th = 3-(fi+se)
    a = distance(x[fi], y[fi], x[se], y[se])
    b = distance(x[se], y[se], x[th], y[th])
    c = distance(x[th], y[th], x[fi], y[fi])
    knee_result = degrees(acos((a**2 + b**2 - c**2) / (2*a*b)))
    return knee_result

# 스쿼트 각도 계산식
def squat_angle(name):
    img_name = (path + '/Angle_Image/Squat/Point/Squat/%s_squat.jpg'%name)
    im = Image.open(img_name)
    pix = im.load()
    A_C = []

    for i in range(im.size[0]):
        for j in range(im.size[1]):
            if pix[i,j][0] < 245 or pix[i,j][1] < 245 or pix[i,j][2] < 245:
                A_C.append([i,j])
    num = [0, 1, 2]
    x = [A_C[0][0],A_C[1][0],A_C[2][0]]
    y = [A_C[0][1],A_C[1][1],A_C[2][1]]
    fi = 0
    se = 0
    th = 0
    
    for i in num:
        if max(y) == y[i]:
            fi = i
            num.remove(i)
            break;
    if x[num[0]] > x[num[1]]:
        se = num[0]
    else :
        se = num[1]

    th = 3-(fi+se)
    a = distance(x[fi], y[fi], x[se], y[se])
    b = distance(x[se], y[se], x[th], y[th])
    c = distance(x[th], y[th], x[fi], y[fi])
    squat_result = degrees(acos((a**2 + b**2 - c**2) / (2*a*b)))
    return squat_result

def angle(name):
    print('스쿼트각도: %d˚'% (squat_angle(name)))
    print('허리  각도: %d˚'% (waist_angle(name)))
    print('무릎  각도: %d˚'% (knee_angle(name)))

def result(name):
    
    if squat_angle(name) <=  115 and 90 >= waist_angle(name) >= 65 and knee_angle(name) < 90:    
        resultdict = {'feedback': "O올바른 스쿼트 자세입니다! 지금 자세를 유지하여 운동하도록 하세요!\\n"}
    else :
        resultdict = {'feedback': "올바르지 않은 자세 입니다."}
        if squat_angle(name) > 110:
            resultdict['squat_angle'] = "엉덩이: 엉덩이를 조금 더 낮춰주세요! 다리를 벌려 골반과 무릎이 바깥쪽을 바라보게 하면 무게중심을 유지한 채 더 앉을 수 있습니다."
        if waist_angle(name) < 65:
            resultdict['waist_angle'] = "허리: 허리에 무리가 갈 수 있는 자세입니다. 복근에 힘을 줘 더욱 허리를 세워주세요."
        if knee_angle(name) >= 90:
            resultdict['knee_angle'] = "무릎: 무릎에 무리가 가는 자세 입니다. 앉으실 때 무릎이 안쪽이 아닌 바깥쪽 방향을 보도록 해주시고 발이 무릎보다 더 안쪽을 바라봐서도 안 됩니다."
    
    return resultdict
