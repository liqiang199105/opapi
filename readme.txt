1. copy property files from <SourceCodeDIR>/src/main/resources/conf to <UserHomeDir>/opapi/conf.
    for test database, you can use below setting:
	jdbc.url=jdbc:mysql://192.168.66.31:3306/open_platform?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&sessionVariables=time_zone%3D%27%2B08%3A00%27&autoReconnect=true
	jdbc.username=db_admin
	jdbc.password=light2902
2. You can visit API document from http://<IP>:<PORT>/api/v1/doc/ eg. http://172.16.1.208:8091/api/v1/doc/.
3. How to register appName? by visit "http://<IP>:<PORT>/op/app/<YourAppName>/appInfo.create".
eg. if you want to register appName "sp-test", please visit 
	http://172.16.1.208:8091/op/app/sp-test/appInfo.create

Notice: IP 172.16.1.208 is on-line server.