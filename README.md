Hello Flattitude!

In order to connect to the Tomcat server, please go to the following link:
http://ec2-52-27-170-102.us-west-2.compute.amazonaws.com:8080/

In order to call the WebService, please go to:
http://ec2-52-27-170-102.us-west-2.compute.amazonaws.com:8080/FlattitudeServer/flattitude/{service}/{function}

Please note that {service} and {function} correspond to the paths that are at the JAVA classes.
They respond to what is specified in the API REST document.

If you want to deploy a new version of the server, you have to EXPORT the project in eclipse in WAR format.
Afterwards, please go to the Tomcat Server (link above), then to the Manager section.
Username: admin
Password: adminadmin

Then, undeploy the Flattitude Server and, down under, please deploy the new Flattitude by selecting the exported WAR file.

Should you have any questions, do not hesitate telling me.

Best,
G. 
