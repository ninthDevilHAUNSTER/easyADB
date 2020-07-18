# AeasyADB

## Introduction

之前写过一个adb相关的辅助脚本，参考链接 https://github.com/ninthDevilHAUNSTER/ArknightsAutoHelper

之后打算学习java的知识，在学习spring框架之前，打算先写一个正式的java学习项目，打算复刻一些ADB的操作，正好自己也有一些之前的经验。

## 用法相关

目前只在windows上做了开发，后续有空会支持Linux以及Mac。各种代码都写的很屎。简单介绍一下




## 功能相关

- 日志功能
  - [x] 日志输出类设计
-  ADB 连接设备
  - [x] ADB 单设备默认选择
  - [x] ADB 多设备参数选择
  - [x] ADB 软件自定义选择
    - [x] config支持ADB选择
    - [x] 多平台适用
    - [ ] ~~远程安装adb命令~~
-  ADB shell 命令执行
  - [x] adb 基础命令执行方法
  - [x] 屏幕点击
  - [x] 屏幕拖动
  - [x] 启动特定程序
  - [x] 查找特定程序
  - [x] 点击位置随机化
-  ADB 截图
  - [x] 按照特定尺寸截图
  - [x] 图像相似度匹配算法
    - [x] P-Hash相似度
    - [x] 色彩直方图相似度
-  OCR 
  - [x] Baidu OCR 支持
    - [x] OCR 结果简单提取
    - [x] OCR 结果富文本解析
    - [x] OCR 结果富文本位置提取
  - [x] Tesseract OCR 支持
    - [x] OCR 结果简单提取
    - [x] OCR 结果富文本解析
    - [x] OCR 结果富文本位置提取
 - [ ] Tecent Youtu OCR 支持 （未完成）


## DDL 相关

准备花5天时间完成上述功能，能写到哪里算哪里吧。应该上述功能都能实现。还能学点JAVA的知识，8错8错

FROM 2020.7.13-2020.7.17