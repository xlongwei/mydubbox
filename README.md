# mydubbox
dubbox startup demo

# register
redis

# provider
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.mydubbox.Provider#provide

mvn assembly:directory
sh bin/startup.sh OR start.bat

# consumer
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.mydubbox.tester.UserServiceConsumer
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.mydubbox.tester.UserServiceTester
