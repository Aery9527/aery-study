http://rocketmq.apache.org/dowloading/releases/

NamesrvStartup的main要加入
String dir = System.getProperty("user.dir") + File.separator + "distribution";
System.setProperty(MixAll.ROCKETMQ_HOME_PROPERTY, dir);


NamesrvStartup的main要加入
String dir = System.getProperty("user.dir") + File.separator + "distribution";
System.setProperty(MixAll.ROCKETMQ_HOME_PROPERTY, dir);
args = new String[]{"-n", "localhost:9876"};

