#!/bin/sh

#tatic文件夹
if [ ! -x "static" ] ; then
mkdir "static"
fi


#创建static/img  static/videos  static/js static/css文件夹
if [ ! -x "static/img" ] ; then
mkdir "static/img"
fi
if [ ! -x "static/videos" ] ; then
mkdir "static/videos"
fi
if [ ! -x "static/js" ] ; then
mkdir "static/js"
fi
if [ ! -x "static/css" ] ; then
mkdir "static/css"
fi

yum update
yum install screen
yum install nodejs

npm install nodejs-websocket
npm install http
npm install formidable
npm install util
npm install fs
npm install path
npm install url
npm install express
npm install rd
npm install child_process
npm install supervisor -g


