# Example web app
An example of a gradle-based Java web app.

The example demonstrates the use of gradle for developing a Java web application that can be deployed to Servlet containers such as Tomcat, Jetty, etc.

## Google Maps API Key
You need to get an API Key for the Google Maps examples to function properly. 

The API key should be placed into the ```src/main/webapp/conf/app.properties``` file.

## File Structure
```
src
 main
  java                              Your java code goes here
   gr
    ntua
     ece
      softeng17b                    The gr.ntua.ece.softeng17b package
       conf                         The conf sub-package with configuration classes.
        Numbers.java                A helper class that processes numbers
        HelloWorldServlet.java      A simple servlet example
 webapp                             The directory of the web app 
  index.jsp                         A jsp example  
  conf                              Contains the configuration files of the web app
  static                            Contains the static files of the web app (images, js, css, etc)        
  WEB-INF                           Contains the web app deployment descriptor file (web.xml)         
```

## Gradle tasks

Invoke gradle using the following command: 

``` > ./gradlew taskA [taskB] ```

Some useful tasks include:

* classes: compile all sources.
* war    : generate war file (to be deployed to a container like tomcat).
* appRun : compile and deploy everything into an embedded jetty container, automatically re-deploying the files you change during its execution.
