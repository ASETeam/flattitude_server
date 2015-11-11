Hello Flattitude!

In order to connect to the Tomcat server, please go to the following link:
https://flattiserver-flattitude.rhcloud.com/flattiserver

In order to call the WebService, please go to:
https://flattiserver-flattitude.rhcloud.com/flattiserver/{service}/{function}


Please note that {service} and {function} correspond to the paths that are at the JAVA classes.
They respond to what is specified in the API REST document.

If you want to deploy a new version of the server, you have to EXPORT the project in eclipse in WAR format.
Afterwards, please go to the Tomcat Server (link above), then to the Manager section.
Username: admin
Password: adminadmin

Then, undeploy the Flattitude Server and, down under, please deploy the new Flattitude by selecting the exported WAR file.

---------

CONNECTION TO DATABASE.

It is still on the board to connect into the database externally. It will be a bit complex as the application is under my account I have to check how can I give you permissions.

Any of you that wants to connect into the database, please use MySQL Workbench or terminal.

Should you have any questions, do not hesitate telling me.

Best,
G. 
