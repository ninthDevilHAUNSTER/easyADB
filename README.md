# AeasyADB

## Introduction

之前写过一个adb相关的辅助脚本，参考链接 https://github.com/ninthDevilHAUNSTER/ArknightsAutoHelper

之后打算学习java的知识，在学习spring框架之前，打算先写一个正式的java学习项目，打算复刻一些ADB的操作，正好自己也有一些之前的经验。



## TODO LIST

目前需要实现的功能包括

- 日志功能
  - [x] 日志输出类设计
-  ADB 连接设备
  - [x] ADB 单设备默认选择
  - [x] ADB 多设备参数选择
-  ADB shell 命令执行
  - [x] adb 基础命令执行方法
  - [x] 屏幕点击
  - [x] 屏幕拖动
  - [x] 启动特定程序
  - [x] 查找特定程序
  - [x] 点击位置随机化
-  ADB 截图
  - [x] 按照特定尺寸截图
  - [ ] 图像相似度匹配算法
    - [x] P-Hash相似度
    - [ ] SSMI 结构相似度
    - [ ] 灰度直方图相似度
-  OCR 
  - [ ] Tesseract OCR 支持
    - [ ] OCR 结果简单提取
    - [ ] OCR 结果富文本解析
    - [ ] OCR 结果富文本位置提取
  - [ ] Baidu OCR 支持
    - [x] OCR 结果简单提取
    - [x] OCR 结果富文本解析
    - [ ] OCR 结果富文本位置提取


## DDL 相关

准备花5天时间完成上述功能，能写到哪里算哪里吧。应该上述功能都能实现。还能学点JAVA的知识，8错8错

FROM 2020.7.13-2020.7.17