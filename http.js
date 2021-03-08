var ws = require("nodejs-websocket")
var http = require("http");
var formidable = require('formidable');
var util = require("util");
var fs = require("fs");
var path = require('path');
var url = require('url');
var cp = require('child_process'); 

var rd = require('rd');
var express = require('express');
var app = express();  
app.use("/static", express.static(__dirname + '/static'));//引入静态文件夹



function logg (logtt){
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = ""+date.getSeconds();
	console.log(year+' '+month+' '+day+' '+hour+':'+minute+':'+second + " "+ logtt);
}


var server = ws.createServer(function (conn) {
    logg ("New connection")

    function readPicList(path){
        var files = fs.readdirSync(path);
        files.forEach(function (itm,index) {
            var stat = fs.statSync(path + itm);
            if (stat.isDirectory()){
                readPicList(path+ itm + "/")
            }else{
                var obj = {};
                obj.type=1002;
                obj.path = path;
                obj.filename = itm
                obj.path2=  obj.path.slice( obj.path.indexOf('/')+1)
                obj.path2=  obj.path2.slice( obj.path2.indexOf('/')+1)
                obj.path2=  obj.path2.slice( obj.path2.indexOf('/')+1)
                obj.path2=  "/"+obj.path2
                var sss = ""+JSON.stringify(obj);
                conn.sendText(sss)
            }
        })

    }

    conn.on("text", function (obj) {
        logg ("Received "+obj)
        try{
            var objgot=JSON.parse(obj)
            if(objgot.type==1){
                if(objgot.password==("hello12345")){
                    conn.sendText(JSON.stringify({
                        type:1001,
                        result:0
                    }))
var path ="./static/videos/";
                readPicList(path);
                conn.sendText(JSON.stringify({
                    type:10021,
                    result:0
                }))
                }else{
                    conn.sendText(JSON.stringify({
                        type:1001,
                        result:222
                    }))
                }

            }else  if(objgot.type==2){
                
            }else if(objgot.type==1000){

            }else{
                conn.sendText(JSON.stringify({
                    type:1000
                }))
            }
        }catch(d){
            logg ("Not json or other exception");
        }

    })
    conn.on("close", function (code, reason) {
        logg ("Connection closed")
    })

    conn.on("error", function (code, reason) {
        logg ("Connection error")
    })
}).listen(7877)




function mkdirs(dirname, callback) {
    fs.exists(dirname, function (exists) {
        if (exists) {
            callback();
        } else {
            mkdirs(path.dirname(dirname), function () {
                fs.mkdir(dirname, callback);
            });
        }
    });
}




var server = http.createServer(function(req,res){
    if(req.url == "/dopost" && req.method.toLowerCase() == "post"){
   logg (":post");
        var form = new formidable.IncomingForm();
        form.uploadDir = "./static";
        form.parse(req, function(err, fields, files) {
            if(err){return;}
            var ppp= files.img.name
			
	logg ("basename:"+   path.parse(url.parse(files.img.name).pathname).base)
	logg ("namename:"+   path.parse(url.parse(files.img.name).pathname).name)
	logg ("extname:"+   path.parse(url.parse(files.img.name).pathname).ext)

	logg ("extname  after slice:"+   path.parse(url.parse(files.img.name).pathname).ext.slice(1,9))
	var q8 = path.parse(url.parse(files.img.name).pathname).ext.slice(1,9)
	var d9 = path.parse(url.parse(files.img.name).pathname).ext.slice(9,10)
	var h8 = path.parse(url.parse(files.img.name).pathname).ext.slice(9)
	if(q8 == "zhoutype"){
		logg ("is zhoutype h8 : "+h8)
		logg ("is zhoutype d9 : "+d9)
		
		ppp=  ppp.slice( 0,ppp.lastIndexOf('/')+1)
		ppp= "static/videos/"+ppp
		var oldpath=files.img.path
		
		newpath="static/"+ path.parse(url.parse(files.img.name).pathname).base

		newpath=decodeURIComponent(newpath)
		
		logg ("newpath: "+newpath);
		mkdirs(ppp,() => {
			fs.rename(oldpath,newpath,function(err){
				res.writeHead(200, {'content-type': 'text/plain;charset=UTF-8'});
				res.end("success");
				
				if(d9=="0"){
					
					total = path.parse(url.parse(files.img.name).pathname).ext.slice(10)
					logg ("total："+total)
					
					var i = 1;
					var max = parseInt(total)
					
					fs.unlink(""+decodeURIComponent("static/"+ path.parse(url.parse(files.img.name).pathname).name), (err) => {
					  if (err) {
						logg ('文件删除ERRORRRRR');
					  }
						for(i=1;i<max;i++){
							logg (i)
							try{
								var p= ""+decodeURIComponent("static/"+ path.parse(url.parse(files.img.name).pathname).name)
								let content1 = fs.readFileSync(p+".zhoutype"+i)
								fs.appendFileSync(p, content1);
                                                                                                                                var p= ""+decodeURIComponent("static/"+ path.parse(url.parse(files.img.name).pathname).name)
							                cp.exec("rm -rf \""+p+".zhoutype"+i+"\"", function() {
								logg (arguments[0]);
								logg ('success delete:'+ p+".zhoutype"+i);
							});
							}catch(d){
								logg ("ERROR!!!!!!!!!!!!!!!!!")
								break;
							}
						}
						
					
						cp.exec("rm -rf \""+p+".zhoutype0"+max+"\"", function() {
							logg (arguments[0]);
							logg ('success delete:'+ p+".zhoutype0"+max);
						});
						
					
						
						var  nownewpath = decodeURIComponent("static/videos"+url.parse(files.img.name).pathname)
						nownewpath=  nownewpath.slice( 0,nownewpath.lastIndexOf('.'))
						var  nowoldpath =""+decodeURIComponent("static/"+ path.parse(url.parse(files.img.name).pathname).name)
					
					
					
						logg ("nowoldpath: "+nowoldpath);
						logg ("nownewpath: "+nownewpath);
						fs.rename(nowoldpath,nownewpath,function(err){
							res.writeHead(200, {'content-type': 'text/plain;charset=UTF-8'});
							res.end("success");
						})					  
					});	
				}
				
			})

		})
		
		

		
	}else{
		ppp=  ppp.slice( 0,ppp.lastIndexOf('/')+1)
		ppp= "static/videos/"+ppp
        var oldpath=files.img.path
		var m =url.parse(files.img.name).pathname


		var newpath ="" 
		if (m.indexOf('\\')==-1 && m.indexOf('/')==-1){

			m="storage/emulated/0/DCIM/OnlineDiskForBrowser/"+m
			newpath="static/videos/"+m;
			ppp = "static/videos/storage/emulated/0/DCIM/OnlineDiskForBrowser"
		}else{
			 newpath="static/videos"+m

		}
		newpath=decodeURIComponent(newpath)
		mkdirs(ppp,() => {
			logg ("newpath: "+newpath);
			fs.rename(oldpath,newpath,function(err){
				res.writeHead(200, {'content-type': 'text/plain;charset=UTF-8'});
				res.end("success");
			})

		})

	}

        });
    }
});
server.listen(1231);


var server =http.createServer(function (request, response) {
    var pathname = ""+url.parse(request.url).pathname;
    pathname =decodeURIComponent(pathname);
    var basename = path.parse(pathname).base;
    var namename = path.parse(pathname).name;
    var extname = path.parse(pathname).ext;

logg ("request: "+pathname)

    fs.readFile("./static/videos/" + pathname,function(err,data){
        if(err){
            return;
        };

        var mime = getMime(extname);
        response.writeHead(200,{"Content-type":mime});
        response.end(data);
    });
});
server.listen(8888);

function getMime(extname){
    switch(extname){
        case ".html" :
            return "text/html";
            break;
        case ".jpg" :
            return "image/jpg";
            break;
        case ".css":
            return "text/css";

        case ".mp3":
            return "audio/mp3";

        case ".mp4":
            return "video/mp4";
            break;
        default :
            return "text/html";
    }
}


http.createServer(app).listen('5000', function () {
    logg ('启动服务器完成');
});





