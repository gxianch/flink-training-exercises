This repository contains examples as well as reference solutions and utility classes for the Apache Flink Training exercises 
found on [http://training.ververica.com](http://training.ververica.com).

数据集下载：

wget http://training.ververica.com/trainingData/nycTaxiRides.gz 

wget http://training.ververica.com/trainingData/nycTaxiFares.gz

Apache Flink 培训系列

https://blog.csdn.net/jiangshouzhuang/article/details/103590375

这里再解释一下数据集：

#### nycTaxiRides 

包含纽约市的出租车出行的信息，每一次出行包含两个事件，START 和 END，表示行程的开始和结束。每一个事件又包括11个属性：

```
rideId：Long//每次出行的唯一ID
taxiId：Long //每个出租车的唯一ID
driverId：Long //每个司机的唯一ID
isStart：Boolean //对于乘车开始事件为TRUE，对于乘车结束事件为FALSE
startTime：DateTime //乘车的开始时间
endTime：DateTime //乘车的结束时间，
                           //“ 1970-01-01 00:00:00”开始事件
startLon：Float //行驶开始位置的经度
startLat：Float //行驶开始位置的纬度
endLon：Float//行驶终点位置的经度
endLat：Float//行驶终点位置的纬度
passengerCnt：Short//乘车人数
```
#### nycTaxiFares 
包含出租车的费用信息，与上面乘车的每一次行程相对应：

```
rideId：Long//每次骑行的唯一ID
taxiId：Long //每个出租车的唯一ID
driverId：Long //每个驱动程序的唯一ID
startTime：DateTime //乘车的开始时间
paymentType：String// 支付类型CSH或CRD
tip：Float//此乘车提示
tolls：Float//此行车通行费
totalFare：Float//总收费
```
为了方便实验演示，笔者将两个数据各自抽取出了几条数据，具体如下： 

nycTaxiRides 数据集：

```
6,START,2013-01-01 00:00:00,1970-01-01 00:00:00,-73.866135,40.771091,-73.961334,40.764912,6,2013000006,2013000006
8,START,2013-01-01 00:00:00,1970-01-01 00:00:00,-73.955925,40.781887,-73.963181,40.777832,3,2013000008,2013000008
2,START,2013-01-01 00:00:00,1970-01-01 00:00:00,-73.978325,40.778091,-73.981834,40.768639,5,2013000002,2013000002
4,START,2013-01-01 00:00:00,1970-01-01 00:00:00,-73.981575,40.767632,-73.977737,40.757927,2,2013000004,2013000004
10,START,2013-01-01 00:00:00,1970-01-01 00:00:00,-73.989845,40.758041,-73.972008,40.757069,3,2013000010,2013000010
19,START,2013-01-01 00:00:00,1970-01-01 00:00:00,-73.99295,40.733986,-73.968025,40.680325,1,2013000019,2013000019
49,START,2013-01-01 00:00:00,1970-01-01 00:00:00,-73.995056,40.760185,-73.96859,40.754955,1,2013000049,2013000049
```

nycTaxiRides 数据集中的 rideId 为： 2、4、6、8、10、19、49

nycTaxiFares 数据集：

```
1,2013000001,2013000001,2013-01-01 00:00:00,CSH,0,0,21.5
2,2013000002,2013000002,2013-01-01 00:00:00,CSH,0,0,7
3,2013000003,2013000003,2013-01-01 00:00:00,CRD,2.2,0,13.7
4,2013000004,2013000004,2013-01-01 00:00:00,CRD,1.7,0,10.7
5,2013000005,2013000005,2013-01-01 00:00:00,CRD,4.65,0,20.15
6,2013000006,2013000006,2013-01-01 00:00:00,CSH,0,4.8,34.3
7,2013000007,2013000007,2013-01-01 00:00:00,CRD,1.9,0,11.9
8,2013000008,2013000008,2013-01-01 00:00:00,CSH,0,0,6
9,2013000009,2013000009,2013-01-01 00:00:00,CRD,1,0,6
10,2013000010,2013000010,2013-01-01 00:00:00,CSH,0,0,15.5
```

nycTaxiFares 数据集中的 rideId 为：1、2、3、4、5、6、7、8、9、10

```
rideId         : Long      // a unique id for each ride
taxiId         : Long      // a unique id for each taxi
driverId       : Long      // a unique id for each driver
isStart        : Boolean   // TRUE for ride start events, FALSE for ride end events
startTime      : DateTime  // the start time of a ride
endTime        : DateTime  // the end time of a ride,
                           //   "1970-01-01 00:00:00" for start events
startLon       : Float     // the longitude of the ride start location
startLat       : Float     // the latitude of the ride start location
endLon         : Float     // the longitude of the ride end location
endLat         : Float     // the latitude of the ride end location
passengerCnt   : Short     // number of passengers on the ride
```

```
rideId         : Long      // a unique id for each ride
taxiId         : Long      // a unique id for each taxi
driverId       : Long      // a unique id for each driver
startTime      : DateTime  // the start time of a ride
paymentType    : String    // CSH or CRD
tip            : Float     // tip for this ride
tolls          : Float     // tolls for this ride
totalFare      : Float     // total fare collected
```