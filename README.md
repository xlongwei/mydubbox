# mydubbox
dubbox startup demo

# build
maven\conf\settings.xml
```
<mirror>
    <id>m2repos</id>
    <mirrorOf>*</mirrorOf>
    <name>m2repos</name>
	<url>http://nexus.xlongwei.com/content/groups/public/</url>
</mirror>
```
> mvn install

# register
redis

# provider
```
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.mydubbox.Provider#provide
```

> mvn assembly:directory
> sh bin/startup.sh OR start.bat

# consumer
```
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.mydubbox.tester.UserServiceConsumer
```
```
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.mydubbox.tester.UserServiceTester
```
