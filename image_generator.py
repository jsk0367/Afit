#!/usr/bin/env python
# coding: utf-8

# In[1]:


from tensorflow.keras.preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img
from tqdm import tqdm
import glob
import time, random


# In[2]:


datagen = ImageDataGenerator(
        rotation_range=2,
        width_shift_range=0.05,
        height_shift_range=0.05,
        shear_range=0.05,
        zoom_range = 0.1,
        horizontal_flip=False,
        fill_mode="nearest")


# In[17]:


plank = [name.split('\\')[-1] for name in glob.glob('D:/plank/*')]


# In[18]:


len(plank)


# In[19]:


for i in tqdm(range(len(plank))):
    image = load_img("D:/plank/" + plank[i])  # PIL 이미지
    x = img_to_array(image)  # (3, 400, 400) 크기의 NumPy 배열
    x = x.reshape((1,) + x.shape)  # (1, 3, 400, 400) 크기의 NumPy 배열

#     아래 .flow() 함수는 임의 변환된 이미지를 배치 단위로 생성해서
#     지정된 `preview/` 폴더에 저장합니다.
    i = 0
    for batch in datagen.flow(x, batch_size=5,
                              save_to_dir="D:/plank", save_prefix="plank_image", save_format="jpg"):
        i += 1
        time.sleep(0.01)
        if i > 1:
            break  # 이미지 10장을 생성하고 마칩니다


# In[28]:


stand = [name.split('\\')[-1] for name in glob.glob('D:/stand/*')]


# In[29]:


for i in tqdm(range(len(stand))):
    image = load_img("D:/stand/" + stand[i])  # PIL 이미지
    x = img_to_array(image)  # (3, 400, 400) 크기의 NumPy 배열
    x = x.reshape((1,) + x.shape)  # (1, 3, 400, 400) 크기의 NumPy 배열

#     아래 .flow() 함수는 임의 변환된 이미지를 배치 단위로 생성해서
#     지정된 `preview/` 폴더에 저장합니다.
    i = 0
    for batch in datagen.flow(x, batch_size=5,
                              save_to_dir="D:/stand", save_prefix="stand_image", save_format="jpg"):
        i += 1
        time.sleep(0.01)
        if i > 4:
            break  # 이미지 10장을 생성하고 마칩니다


# In[ ]:




