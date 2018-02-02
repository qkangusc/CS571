<html>
<head>
<meta content="text/html;charset=utf-8" http-equiv="Content-Type">  
<title>My homework 6</title>
<style>

a{ text-decoration:none;}
a:hover{
      color:black;
      }
a:visited{
      color:blue;
      }
div.conversation{
   text-align:center;
   padding:70px,70px,70px,70px;
   border:70px;
   margin:0 auto;
   background-color:rgb(240,240,240);
   height:200px;
   width:600px;
}
h1{text-align:center;}

form{
    
	text-align: center;
}
input.search{
    
    width:150px;height:30px;
    text-decoration:none;padding:4px 4px 4px 4px;
    background-color:gray;border-radius:10px;
    font-size:20px;
    margin:auto;
}
</style>
</head>
<body>
<div class="conversation" >
<h1>Stock Search</h1>
</br>
<form id="myform" method="GET" action="">
Enter Stock Ticker Symbol:* <input id="input" type="text" size="12" maxlength="12" name="symbol" value="<?php if(isset($_GET['symbol'])){echo $_GET['symbol'];} ?>">
<button type="submit">Search</button>
<button type="button" onClick="Clear_all('Below_table','input')">Clear</button>
<p style="margin-left:50px;">*-Mandatory fields.</p></form>
<script>

function Clear_all(id1,id2){
document.getElementById(id1).style.display='none';
document.getElementById(id2).value="";
}
</script>
</div>
</br>
<div id="Below_table">

<?php
//echo "start!"; 
if(isset($_GET['symbol'])&&$_GET['symbol']==""){  
    $message = "Please enter a symbol!";
    echo"<script>alert('$message');</script>";} 
else if(isset($_GET['symbol'])&&$_GET['symbol']!=""){
   
    $info_url = 'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol='.urlencode($_GET['symbol']).'&apikey=39I7VDS18H1LP3XK'; //url Price/Volume
    $info_json = file_get_contents($info_url);   //request
    $info_content = json_decode($info_json,false);  //parse
    $info_array = json_decode($info_json,true);
    
    
   

   if(key($info_content)=='Error Message'){echo '<table align="center" border="1px" width="1200px" cellspacing=0">
  <tr>
  <td style="width:400px;background-color:rgb(243,243,243);">
  <b>Error</b></td>
  <td style="text-align:center;">Error: NO record has been found, please enter a valid symbol.</td></tr></table>'; }
   else{
    $series = $info_content->{"Time Series (Daily)"};
    $close = reset($series)->{"4. close"};
    $open = reset($series)->{"1. open"};
    $pre_close = next($series)->{"4. close"};
    $change_value = round(($close-$pre_close),2);
    $change_percent = round($change_value*100/$pre_close,2);
    $volume = number_format(reset($series)->{"5. volume"});
    $name = $info_array['Meta Data']['2. Symbol'];
    $timestamp = $info_array['Meta Data']['3. Last Refreshed'];
//***********************JS variable for JSON.parse****************************    
    echo '<script>var indicator_sma="SMA"</script>';
    echo '<script>var indicator_ema="EMA"</script>';
    echo '<script>var indicator_stoch="STOCH"</script>';
    echo '<script>var indicator_rsi="RSI"</script>';
    echo '<script>var indicator_adx="ADX"</script>';
    echo '<script>var indicator_cci="CCI"</script>';
    echo '<script>var indicator_bbands="BBANDS"</script>';
    echo '<script>var indicator_macd="MACD"</script>';
    
    echo  '<table id="upper_table" style="border:1px solid rgb(211,211,211); width:1400px; height:300px;" align="center"  cellspacing=0><tr><td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>Stock Ticker Symbol</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'.$name.'</td></tr><tr>
<td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>Close</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'.$close.'</td></tr>
<tr><td style="border:1px solid rgb(211,211,211);background-color:rgb(243,243,243);"><b>Open</td>
<td style="border:1px solid rgb(211,211,211);background-color:rgb(251,251,251); text-align:center;">'. $open.'</td></tr>
<tr><td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>Previous Close</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'.$pre_close.'</td></tr>';
if($change_value > 0){
echo'<tr><td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>Change</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'.$change_value.'<img src="http://cs-server.usc.edu:45678/hw/hw6/images/Green_Arrow_Up.png" style="height:16px;">'.'</td></tr><tr><td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>Change Percent</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'.$change_percent.'%'.'<img src="http://cs-server.usc.edu:45678/hw/hw6/images/Green_Arrow_Up.png" style="height:16px;">'.'</td></tr>';}

else{
echo'<tr><td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>Change</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'.$change_value.'<img src="http://cs-server.usc.edu:45678/hw/hw6/images/Red_Arrow_Down.png" style="height:16px;">'.'</td></tr><tr>
<td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);""><b>Change Percent</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'.$change_percent.'%'.'<img src="http://cs-server.usc.edu:45678/hw/hw6/images/Red_Arrow_Down.png" style="height:16px;">'.'</td></tr>';}

echo'<tr><td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>Days Change</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'.reset($series)->{"3. low"}.'-'.reset($series)->{"2. high"}.'</td></tr>

<tr>
<td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>Volume</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'. $volume.'</td>
</tr>
<tr>
<td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);"><b>TimeStamp</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">'. $timestamp.'</td>
</tr>
<tr>
<td style="border:1px solid rgb(211,211,211);width:400px;background-color:rgb(243,243,243);">
<b>Indicators</td>
<td style="border:1px solid rgb(211,211,211);width:800px;text-align:center;background-color:rgb(251,251,251);">
<font color="blue">
<a href="#" onClick="viewchart1()">Price</a> 
<a href="#" onClick="viewchart2(indicator_sma)">SMA</a> 
<a href="#" onClick="viewchart3(indicator_ema)">EMA</a> 
<a href="#" onClick="viewchart4(indicator_stoch)">STOCH</a> 
<a href="#" onClick="viewchart5(indicator_rsi)">RSI</a> 
<a href="#" onClick="viewchart6(indicator_adx)">ADX</a> 
<a href="#" onClick="viewchart7(indicator_cci)">CCI</a> 
<a href="#" onClick="viewchart8(indicator_bbands)">BBANDS</a> 
<a href="#" onClick="viewchart9(indicator_macd)">MACD</a>
</font></td>
</tr></table>';
    //reset($series);
    date_default_timezone_set('US/Eastern');
    $price = array();
    $volume_data = array();
    $date = array();
    $temp = reset($series);
    $i = 0;
    while( $temp && $i < 150 ){
      array_unshift($price, (float)$temp->{"4. close"});
      array_unshift($volume_data, (float)$temp->{"5. volume"});
      array_unshift($date, key($series));
      $temp = next($series);
      $i++;
    }
   
   //print_r($date);
    //$current_date = data_format($timestamp,'m/d/Y');
      echo '<script>var price = '.json_encode($price). ';</script>';
      echo '<script>var volume = '.json_encode($volume_data). ';</script>';
      echo '<script>var symbol = "'.$name.'";</script>';
      echo '<script>var date = "'. date_format(new DateTime($timestamp), 'm/d/Y'). '";</script>';
      echo '<script>var date1 = '. json_encode($date).';</script>';
//}
 
?>

<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
</br>
<div id="container" style="width:1400px; border:2px solid rgb(211,211,211); height: 700px; margin: 0 auto"></div>
<script type="text/javascript">
Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text:'Stock Price'+'('+ date + ')'
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'Stock Price',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5,
    }, { // Secondary yAxis
        tickInterval: 50000000,
        title: {
            text: 'Volume',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        labels: {
	    formatter: function() {
		return this.value / 1000000 + 'M ';
	    },
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        opposite: true
    }],
    tooltip: {
        shared: true
         
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'Stock Price',
        type: 'area',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  price,
        tooltip: {
            valueSuffix: ''
        }
    },
    {
        name: 'Volume',
        type: 'column',
        color: "#ffffff",
        yAxis: 1,
        data:  volume,
        tooltip: {
            valueSuffix: ' '
        }

    }]
});
//*************************************Price*********************************************
function viewchart1(){
   Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text:'Stock Price'+'('+ date + ')'
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'Stock Price',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5,
    }, { // Secondary yAxis
        tickInterval: 50000000,
        title: {
            text: 'Volume',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        labels: {
	    formatter: function() {
		return this.value / 1000000 + 'M ';
	    },
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        opposite: true
    }],
    tooltip: {
        //formatter: function(){return this.x.value.substring(5,7)+'/'+this.value.substring(8);},
        shared: true
       
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'Stock Price',
        type: 'area',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  price,
        tooltip: {
            valueSuffix: ''
        }
    },
    {
        name: 'Volume',
        type: 'column',
        color: "#ffffff",
        yAxis: 1,
        data:  volume,
        tooltip: {
            valueSuffix: ' '
        }

    }]
});

}
//************************SMA*********************
 function viewchart2(what){
   var URL = 'https://www.alphavantage.co/query?function='+what+'&symbol='+symbol+'&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
    function loadJSON(url){
       if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
   else {xmlhttp=new ActiveXObject("Mircrosoft.XMLHTTP");}

       // try{
            xmlhttp.overrideMimeType("application/json");
            xmlhttp.open("GET",url,true);
           
       xmlhttp.onreadystatechange=function() {
                if(xmlhttp.readyState==4 && xmlhttp.status==200) {
                    Draw_Indicator_Chart1(xmlhttp.responseText);
                   
                } else if (xmlhttp.status==404){
                    alert("The JSON File Does not Exist!");
                    document.write(xmlhttp.status);
                    return false;
                }
           }
            xmlhttp.send();
            //jsonObj=JSON.parse(xmlDoc);
           // return jsonObj;
       }
    loadJSON(URL);
}
  function Draw_Indicator_Chart1(what) {   
    //jsonObj = loadJSON(URL);
     //alert("Hi!");
    var jsonObj = JSON.parse(what);
    var sma = jsonObj['Technical Analysis: SMA'];
    var sma_for_indicator=jsonObj['Meta Data']['2: Indicator'];
    var sma_data=[];
    var count1 = 0;
    for(var k in sma){    
       sma_data.unshift(parseFloat(sma[k]['SMA']));
       count1++;
       if(count1==date1.length){break;}
    }
   // alert(sma_data[0]);

  Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text: sma_for_indicator
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'SMA',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5
    //}, 
       
    }],
    tooltip: {
        shared: false,
         formatter: function () {
                var symbol1;
            
                switch ( this.point.graphic.symbolName ) {
                    case 'circle':
                        symbol1 = '●';
                        break;
                    case 'diamond':
                        symbol1 = '♦';
                        break;
                    case 'square':
                        symbol1 = '■';
                        break;
                    case 'triangle':
                        symbol1 = '▲';
                        break;
                    case 'triangle1-down':
                        symbol = '▼';
                        break;
                }
                
                return this.x.substring(5,7)+'/'+this.x.substring(8) + '<br/>' + '<span style="color:' + this.series.color + '">'+ symbol1 +'</span>'+ this.series.name + ': ' + '<b>' + this.y + '</b>';
            
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'SMA',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  sma_data,
        tooltip: {
            valueSuffix: ''
        }
    }]
});  
}

//*******************************************EMA*************************************
function viewchart3(what){
   var URL = 'https://www.alphavantage.co/query?function='+what+'&symbol='+symbol+'&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
    function loadJSON(url){
       if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
   else {xmlhttp=new ActiveXObject("Mircrosoft.XMLHTTP");}

       // try{
            xmlhttp.overrideMimeType("application/json");
            xmlhttp.open("GET",url,true);
           
       xmlhttp.onreadystatechange=function() {
                if(xmlhttp.readyState==4 && xmlhttp.status==200) {
                    Draw_Indicator_Chart2(xmlhttp.responseText);
                   
                } else if (xmlhttp.status==404){
                    alert("The JSON File Does not Exist!");
                    document.write(xmlhttp.status);
                    return false;
                }
           }
            xmlhttp.send();
            //jsonObj=JSON.parse(xmlDoc);
           // return jsonObj;
       }
    loadJSON(URL);
}
  function Draw_Indicator_Chart2(what) {   
    //jsonObj = loadJSON(URL);
    //alert("Hi!");
    var jsonObj = JSON.parse(what);
    var ema = jsonObj['Technical Analysis: EMA'];
    var ema_for_indicator=jsonObj['Meta Data']['2: Indicator'];
    var ema_data=[];
    var count1 = 0;
    for(var k in ema){    
       ema_data.unshift(parseFloat(ema[k]['EMA']));
       count1++;
       if(count1==date1.length){break;}
    }
   // alert(sma_data[0]);
//****************************************************************************************Highchart for Indicator********************************************************************//
  Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text: ema_for_indicator
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'EMA',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5
    //}, 
       
    }],
    tooltip: {
        shared: false,
         formatter: function () {
                var symbol1;
            
                switch ( this.point.graphic.symbolName ) {
                    case 'circle':
                        symbol1 = '●';
                        break;
                    case 'diamond':
                        symbol1 = '♦';
                        break;
                    case 'square':
                        symbol1 = '■';
                        break;
                    case 'triangle':
                        symbol1 = '▲';
                        break;
                    case 'triangle1-down':
                        symbol = '▼';
                        break;
                }
                
                return this.x.substring(5,7)+'/'+this.x.substring(8) + '<br/>' + '<span style="color:' + this.series.color + '">'+ symbol1 +'</span>'+ this.series.name + ': ' + '<b>' + this.y + '</b>';
            
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'EMA',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  ema_data,
        tooltip: {
            valueSuffix: ''
        }
    
   

    }]
});  
}

//**********************************STOCH*********************************************
function viewchart4(what){
var URL = 'https://www.alphavantage.co/query?function='+what+'&symbol='+symbol+'&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
    function loadJSON(url){
       if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
   else {xmlhttp=new ActiveXObject("Mircrosoft.XMLHTTP");}

       // try{
            xmlhttp.overrideMimeType("application/json");
            xmlhttp.open("GET",url,true);
           
       xmlhttp.onreadystatechange=function() {
                if(xmlhttp.readyState==4 && xmlhttp.status==200) {
                    Draw_Indicator_Chart3(xmlhttp.responseText);
                   
                } else if (xmlhttp.status==404){
                    alert("The JSON File Does not Exist!");
                    document.write(xmlhttp.status);
                    return false;
                }
           }
            xmlhttp.send();
            //jsonObj=JSON.parse(xmlDoc);
           // return jsonObj;
       }
    loadJSON(URL);
}
  function Draw_Indicator_Chart3(what) {   
    //jsonObj = loadJSON(URL);
    //alert("Hi!");
    var jsonObj = JSON.parse(what);
    var stoch = jsonObj['Technical Analysis: STOCH'];
    var stoch_indicator=jsonObj['Meta Data']['2: Indicator'];
    var slowd_data=[];
    var slowk_data=[];
    var count1 = 0;
    for(var k in stoch){    
       slowd_data.unshift(parseFloat(stoch[k]['SlowD']));
       slowk_data.unshift(parseFloat(stoch[k]['SlowK']));
       count1++;
       if(count1==date1.length){break;}
    }
   // alert(sma_data[0]);
//****************************************************************************************Highchart for Indicator********************************************************************//
  Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text: stoch_indicator
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'STOCH',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5
    //}, 
       
    }],
    tooltip: {
        shared: false,
         formatter: function () {
                var symbol1;
            
                switch ( this.point.graphic.symbolName ) {
                    case 'circle':
                        symbol1 = '●';
                        break;
                    case 'diamond':
                        symbol1 = '♦';
                        break;
                    case 'square':
                        symbol1 = '■';
                        break;
                    case 'triangle':
                        symbol1 = '▲';
                        break;
                    case 'triangle1-down':
                        symbol = '▼';
                        break;
                }
                
                return this.x.substring(5,7)+'/'+this.x.substring(8) + '<br/>' + '<span style="color:' + this.series.color + '">'+ symbol1 +'</span>'+ this.series.name + ': ' + '<b>' + this.y + '</b>';
            
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'SlowD',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  slowd_data,
        tooltip: {
            valueSuffix: ''
        }
        },
        {
        name: 'SlowK',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[0],
        data:  slowk_data,
        tooltip: {
            valueSuffix: ''
        }
   

    }]
});  
}
//*************************************RSI**************************************
function viewchart5(what){
    var URL = 'https://www.alphavantage.co/query?function='+what+'&symbol='+symbol+'&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
    function loadJSON(url){
       if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
   else {xmlhttp=new ActiveXObject("Mircrosoft.XMLHTTP");}

       // try{
            xmlhttp.overrideMimeType("application/json");
            xmlhttp.open("GET",url,true);
           
       xmlhttp.onreadystatechange=function() {
                if(xmlhttp.readyState==4 && xmlhttp.status==200) {
                    Draw_Indicator_Chart4(xmlhttp.responseText);
                   
                } else if (xmlhttp.status==404){
                    alert("The JSON File Does not Exist!");
                    document.write(xmlhttp.status);
                    return false;
                }
           }
            xmlhttp.send();
            //jsonObj=JSON.parse(xmlDoc);
           // return jsonObj;
       }
    loadJSON(URL);
}
  function Draw_Indicator_Chart4(what) {   
    //jsonObj = loadJSON(URL);
    //alert("Hi!");
    var jsonObj = JSON.parse(what);
    var rsi = jsonObj['Technical Analysis: RSI'];
    var rsi_for_indicator=jsonObj['Meta Data']['2: Indicator'];
    var rsi_data=[];
    var count1 = 0;
    for(var k in rsi){    
       rsi_data.unshift(parseFloat(rsi[k]['RSI']));
       count1++;
       if(count1==date1.length){break;}
    }
   // alert(sma_data[0]);
//****************************************************************************************Highchart for Indicator********************************************************************//
  Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text: rsi_for_indicator
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'RSI	',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5
    //}, 
       
    }],
    tooltip: {
       shared: false,
         formatter: function () {
                var symbol1;
            
                switch ( this.point.graphic.symbolName ) {
                    case 'circle':
                        symbol1 = '●';
                        break;
                    case 'diamond':
                        symbol1 = '♦';
                        break;
                    case 'square':
                        symbol1 = '■';
                        break;
                    case 'triangle':
                        symbol1 = '▲';
                        break;
                    case 'triangle1-down':
                        symbol = '▼';
                        break;
                }
                
                return this.x.substring(5,7)+'/'+this.x.substring(8) + '<br/>' + '<span style="color:' + this.series.color + '">'+ symbol1 +'</span>'+ this.series.name + ': ' + '<b>' + this.y + '</b>';
            
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'RSI',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  rsi_data,
        tooltip: {
            valueSuffix: ''
        }
    
   

    }]
});  
}
//******************************************************************ADX*******************************************
function viewchart6(what){
    var URL = 'https://www.alphavantage.co/query?function='+what+'&symbol='+symbol+'&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
    function loadJSON(url){
       if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
   else {xmlhttp=new ActiveXObject("Mircrosoft.XMLHTTP");}

       // try{
            xmlhttp.overrideMimeType("application/json");
            xmlhttp.open("GET",url,true);
           
       xmlhttp.onreadystatechange=function() {
                if(xmlhttp.readyState==4 && xmlhttp.status==200) {
                    Draw_Indicator_Chart5(xmlhttp.responseText);
                   
                } else if (xmlhttp.status==404){
                    alert("The JSON File Does not Exist!");
                    document.write(xmlhttp.status);
                    return false;
                }
           }
            xmlhttp.send();
            //jsonObj=JSON.parse(xmlDoc);
           // return jsonObj;
       }
    loadJSON(URL);
}
  function Draw_Indicator_Chart5(what) {   
    //jsonObj = loadJSON(URL);
    //alert("Hi!");
    var jsonObj = JSON.parse(what);
    var adx = jsonObj['Technical Analysis: ADX'];
    var adx_for_indicator=jsonObj['Meta Data']['2: Indicator'];
    var adx_data=[];
    var count1 = 0;
    for(var k in adx){    
       adx_data.unshift(parseFloat(adx[k]['ADX']));
       count1++;
       if(count1==date1.length){break;}
    }
   // alert(sma_data[0]);
//****************************************************************************************Highchart for Indicator********************************************************************//
  Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text: adx_for_indicator
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'ADX',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5
    //}, 
       
    }],
    tooltip: {
        shared: false,
         formatter: function () {
                var symbol1;
            
                switch ( this.point.graphic.symbolName ) {
                    case 'circle':
                        symbol1 = '●';
                        break;
                    case 'diamond':
                        symbol1 = '♦';
                        break;
                    case 'square':
                        symbol1 = '■';
                        break;
                    case 'triangle':
                        symbol1 = '▲';
                        break;
                    case 'triangle1-down':
                        symbol = '▼';
                        break;
                }
                
                return this.x.substring(5,7)+'/'+this.x.substring(8) + '<br/>' + '<span style="color:' + this.series.color + '">'+ symbol1 +'</span>'+ this.series.name + ': ' + '<b>' + this.y + '</b>';
            
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'ADX',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  adx_data,
        tooltip: {
            formatter: function(){return this.x.substring(5,7)+'/'+this.x.substring(8);},
            valueSuffix: ''
        }
    }]
});  
}
//****************************************CCI*******************************************
function viewchart7(what){
    var URL = 'https://www.alphavantage.co/query?function='+what+'&symbol='+symbol+'&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
    function loadJSON(url){
       if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
   else {xmlhttp=new ActiveXObject("Mircrosoft.XMLHTTP");}

       // try{
            xmlhttp.overrideMimeType("application/json");
            xmlhttp.open("GET",url,true);
           
       xmlhttp.onreadystatechange=function() {
                if(xmlhttp.readyState==4 && xmlhttp.status==200) {
                    Draw_Indicator_Chart6(xmlhttp.responseText);
                   
                } else if (xmlhttp.status==404){
                    alert("The JSON File Does not Exist!");
                    document.write(xmlhttp.status);
                    return false;
                }
           }
            xmlhttp.send();
            //jsonObj=JSON.parse(xmlDoc);
           // return jsonObj;
       }
    loadJSON(URL);
}
  function Draw_Indicator_Chart6(what) {   
    //jsonObj = loadJSON(URL);
    //alert("Hi!");
    var jsonObj = JSON.parse(what);
    var cci = jsonObj['Technical Analysis: CCI'];
    var cci_for_indicator=jsonObj['Meta Data']['2: Indicator'];
    var cci_data=[];
    var count1 = 0;
    for(var k in cci){    
       cci_data.unshift(parseFloat(cci[k]['CCI']));
       count1++;
       if(count1==date1.length){break;}
    }
   // alert(sma_data[0]);
//****************************************************************************************Highchart for Indicator********************************************************************//
  Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text: cci_for_indicator
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'CCI',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5
    //}, 
       
    }],
    tooltip: {
        shared: false,
         formatter: function () {
                var symbol1;
            
                switch ( this.point.graphic.symbolName ) {
                    case 'circle':
                        symbol1 = '●';
                        break;
                    case 'diamond':
                        symbol1 = '♦';
                        break;
                    case 'square':
                        symbol1 = '■';
                        break;
                    case 'triangle':
                        symbol1 = '▲';
                        break;
                    case 'triangle1-down':
                        symbol = '▼';
                        break;
                }
                
                return this.x.substring(5,7)+'/'+this.x.substring(8) + '<br/>' + '<span style="color:' + this.series.color + '">'+ symbol1 +'</span>'+ this.series.name + ': ' + '<b>' + this.y + '</b>';
            
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'CCI',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  cci_data,
        tooltip: {
            formatter: function(){return this.x.substring(5,7)+'/'+this.x.substring(8);},
            valueSuffix: ''
        }
    }]
});  
}
//*************************************************************BBANDS*************************************
function viewchart8(what){
    var URL = 'https://www.alphavantage.co/query?function='+what+'&symbol='+symbol+'&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
    function loadJSON(url){
       if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
   else {xmlhttp=new ActiveXObject("Mircrosoft.XMLHTTP");}

       // try{
            xmlhttp.overrideMimeType("application/json");
            xmlhttp.open("GET",url,true);
           
       xmlhttp.onreadystatechange=function() {
                if(xmlhttp.readyState==4 && xmlhttp.status==200) {
                    Draw_Indicator_Chart7(xmlhttp.responseText);
                   
                } else if (xmlhttp.status==404){
                    alert("The JSON File Does not Exist!");
                    document.write(xmlhttp.status);
                    return false;
                }
           }
            xmlhttp.send();
            //jsonObj=JSON.parse(xmlDoc);
           // return jsonObj;
       }
    loadJSON(URL);
}
  function Draw_Indicator_Chart7(what) {   
    //jsonObj = loadJSON(URL);
    //alert("Hi!");
    var jsonObj = JSON.parse(what);
    var bbands = jsonObj['Technical Analysis: BBANDS'];
    var bbands_for_indicator=jsonObj['Meta Data']['2: Indicator'];
    var middle_data=[];
    var upper_data=[];
    var lower_data=[];
    var count1 = 0;
    for(var k in bbands){    
       middle_data.unshift(parseFloat(bbands[k]['Real Middle Band']));
       upper_data.unshift(parseFloat(bbands[k]['Real Upper Band']));
       lower_data.unshift(parseFloat(bbands[k]['Real Lower Band']));
       count1++;
       if(count1==date1.length){break;}
    }
   // alert(sma_data[0]);
//****************************************************************************************Highchart for Indicator********************************************************************//
    Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text: bbands_for_indicator
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'BBANDS',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5
    //}, 
       
    }],
    tooltip: {
        shared: false,
         formatter: function () {
                var symbol1;
            
                switch ( this.point.graphic.symbolName ) {
                    case 'circle':
                        symbol1 = '●';
                        break;
                    case 'diamond':
                        symbol1 = '♦';
                        break;
                    case 'square':
                        symbol1 = '■';
                        break;
                    case 'triangle':
                        symbol1 = '▲';
                        break;
                    case 'triangle1-down':
                        symbol = '▼';
                        break;
                }
                
                return this.x.substring(5,7)+'/'+this.x.substring(8) + '<br/>' + '<span style="color:' + this.series.color + '">'+ symbol1 +'</span>'+ this.series.name + ': ' + '<b>' + this.y + '</b>';
            
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'Real Middle Band',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  middle_data,
        tooltip: {
            valueSuffix: ''
        }
        },
        {
        name: 'Real Upper Band',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[0],
        data:  upper_data,
        tooltip: {
            valueSuffix: ''
        }
        }, 
        {
        name: 'Real Lower Band',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[3],
        data:  lower_data,
        tooltip: {
            valueSuffix: ''
        }
        
    }]
});  
}
//************************************MACD**********************************************
function viewchart9(what){
   var URL = 'https://www.alphavantage.co/query?function='+what+'&symbol='+symbol+'&interval=daily&time_period=10&series_type=close&apikey=39I7VDS18H1LP3XK';
    function loadJSON(url){
       if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        }
   else {xmlhttp=new ActiveXObject("Mircrosoft.XMLHTTP");}

       // try{
            xmlhttp.overrideMimeType("application/json");
            xmlhttp.open("GET",url,true);
           
       xmlhttp.onreadystatechange=function() {
                if(xmlhttp.readyState==4 && xmlhttp.status==200) {
                    Draw_Indicator_Chart8(xmlhttp.responseText);
                   
                } else if (xmlhttp.status==404){
                    alert("The JSON File Does not Exist!");
                    document.write(xmlhttp.status);
                    return false;
                }
           }
            xmlhttp.send();
            //jsonObj=JSON.parse(xmlDoc);
           // return jsonObj;
       }
    loadJSON(URL);
}
  function Draw_Indicator_Chart8(what) {   
    //jsonObj = loadJSON(URL);
    //alert("Hi!");
    var jsonObj = JSON.parse(what);
    var macd = jsonObj['Technical Analysis: MACD'];
    var macd_for_indicator=jsonObj['Meta Data']['2: Indicator'];
    var macd_hist_data=[];
    var macd_data=[];
    var macd_signal_data=[];
    var count1 = 0;
    for(var k in macd){    
       macd_hist_data.unshift(parseFloat(macd[k]['MACD_Hist']));
       macd_data.unshift(parseFloat(macd[k]['MACD']));
       macd_signal_data.unshift(parseFloat(macd[k]['MACD_Signal']));
       count1++;
       if(count1==date1.length){break;}
    }
   // alert(sma_data[0]);
//****************************************************************************************Highchart for Indicator********************************************************************//
    Highcharts.chart('container', {
    chart: {
        zoomType: 'xy'
    },
    title: {
         text: macd_for_indicator
	    
        
    },
    subtitle: {
        useHTML: true,
        text: '<a href="https://www.alphavantage.co/">Source: Alpha Vantage</a>'
    },
    xAxis: [{
          
          tickInterval: 5,
          categories:date1,
          labels:{ 
         formatter: function(){return this.value.substring(5,7)+'/'+this.value.substring(8);}}
          
    }],
    yAxis: [{ 
        labels: {
            format: '{value}',
            
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        
        title: {
            text: 'MACD',
            style: {
                color: Highcharts.getOptions().colors[1]
            }
        },
        tickInterval: 5
    //}, 
       
    }],
    tooltip: {
        shared: false,
            formatter: function () {
                var symbol1;
            
                switch ( this.point.graphic.symbolName ) {
                    case 'circle':
                        symbol1 = '●';
                        break;
                    case 'diamond':
                        symbol1 = '♦';
                        break;
                    case 'square':
                        symbol1 = '■';
                        break;
                    case 'triangle':
                        symbol1 = '▲';
                        break;
                    case 'triangle1-down':
                        symbol = '▼';
                        break;
                }
                
                return this.x.substring(5,7)+'/'+this.x.substring(8) + '<br/>' + '<span style="color:' + this.series.color + '">'+ symbol1 +'</span>'+ this.series.name + ': ' + '<b>' + this.y + '</b>';
            
        }
    },
    legend: {
        layout: 'vertical',
        align: 'right',
        verticalAlign: 'middle',
        labelFormatter: function(){
        return symbol+' '+this.name;
        },
        backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
    },
    series: [{
        name: 'MACD_Hist',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[8],
        data:  macd_hist_data,
        tooltip: {
            valueSuffix: ''
            
        }
        },
        {
        name: 'MACD',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[0],
        data:  macd_data,
        tooltip: {
            valueSuffix: ''
        }
        }, 
        {
        name: 'MACD_Signal',
        type: 'spline',
        threshold: null,
        color: Highcharts.getOptions().colors[3],
        data:  macd_signal_data,
        tooltip: {
            valueSuffix: ''
        }
        
    }]
});  
}
</script>
</br>

<?php 
   $news_table = 'https://seekingalpha.com/api/sa/combined/'.urlencode($_GET['symbol']).'.xml';
   $news_content = file_get_contents($news_table);
   $news_xml = simplexml_load_string($news_content);
   
   $title = array();
   $link = array();
   $pubdate = array();
   $news_item=array();
   $i = 0;
   //echo  '<script>var html_text="";</script>';
  // echo  '<script>html_text+="<table>";</script>';
   
   foreach($news_xml->channel->item as $item){
      
      if(strpos($item->{'guid'}, "Article")==false){continue;}
      array_push($news_item, $item);
      $i++;
      if($i == 5){break;}
   }
   
   echo '<script>var news = '.json_encode($news_item). ';</script>';
           
      
?>
<div id="STOCK_NEWS" style="width: 1400px;height:400px; margin:0 auto; text-align:center;"> 

<div class="Stoc_News" style="width:220px;height:60px; margin:0 auto;" onClick="HideOrShow('content','text','imag')">
<p id="text" align="center" style="color:rgb(200,200,200);">click to hide stock news</p>
<img id="imag" src="http://cs-server.usc.edu:45678/hw/hw6/images/Gray_Arrow_Up.png" style="height:40px;">
</div>
</br>
</br>
<div id="content" style="width: 1400px;height:300px;"></div>
<script>   
       var html_text="";
           html_text+="<table cellspacing=0 cellpadding=3 style='width:1400px;height:240px; background-color:rgb(243,243,243);border:1px solid rgb(211,211,211);'><tbody>";
           for(var i = 0; i < 5; i++){
              html_text+="<tr><td style='border:1px solid rgb(211,211,211);font-size:16px;'><a href='"+news[i].link+"'>"+news[i].title+"</a>"+'&nbsp&nbsp&nbsp&nbsp'+'Publicated Time:'+news[i].pubDate+"</td></tr>";
           }
       html_text+="</tbody></table>";
       document.getElementById("content").innerHTML = html_text;  

        function HideOrShow(what1,what2,what3){
           if(document.getElementById(what1).style.display=='none'){
              document.getElementById(what1).style.display='block';
               document.getElementById(what2).innerHTML='click to hide stock news';
               document.getElementById(what3).src="http://cs-server.usc.edu:45678/hw/hw6/images/Gray_Arrow_Up.png";
              }
           else{
           document.getElementById(what1).style.display='none';
           document.getElementById(what2).innerHTML='click to show stock news';
           document.getElementById(what3).src="http://cs-server.usc.edu:45678/hw/hw6/images/Gray_Arrow_Down.png";}
           }
           //document.getElementById("hide_news")='';         
</script> 

</div>
</div>
</body>
</html>

<?php
}
}
?>
