
var express = require("express");
var app = express();
var request = require("request");
var cors=require("cors");
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var parseString = require('xml2js').parseString;

app.use(cors());
app.use(express.static(__dirname + '/views'));
app.set('views', __dirname + '/views');
app.engine('html', require('ejs').renderFile);
   
app.get("/", function(req, res){
 /* res.send("Hello!");*/
   res.render("search.html");
});

app.get("/auto/:query",function(req,res){
  console.log("got");
   var input = req.params.query;
   console.log(input);
    var url = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input="+ input;
       request(url, function(error, response, body){
        if(!error && response.statusCode == 200 ) { 
            var data = JSON.parse(body);
          /*console.log(data);*/
            res.send(data);
            res.end();
         }
         else{
          res.send("Error!!");
          console.log("Error!");
          res.end();
         }
    
      });
  });

app.get("/json", function(req, res){
    var symbol = req.query.symbol;
    var indicators = req.query.indicators;
    
    console.log(symbol);
    console.log(indicators);
  if(indicators=="News_Feeds"){
    var url = 'https://seekingalpha.com/api/sa/combined/'+symbol+'.xml';
     request(url, function(error, response, body){
        if(!error && response.statusCode == 200 ) { 
           parseString(body, function (err, result) {
              console.dir(result);
              res.send(result);
              res.end();
            });
         }
         else{
          res.send("Error!!");
          
          console.log("Error!");
          res.end();
         }

});
    
  }
  else{
 	   var url = 'https://www.alphavantage.co/query?function='+indicators+'&symbol='+symbol+'&outputsize=full&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
      request(url, function(error, response, body){
        if(!error && response.statusCode == 200 ) {
        	var data = JSON.parse(body);
        	/*console.log(data);*/
           
            res.send(body);
            res.end();
          }
          /*else{
          res.send("Error!!");
          res.end();
          console.log("Error!");
         }*/
    });
  }
  
});


app.listen(9090,'127.0.0.1',function(){
    console.log("Server has started listening!!");
});                  //控制台输出
/*app.listen(process.env.PORT,function(){
    console.log("Server has started listening!!");
});*/

