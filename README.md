# lottoland
Webdriver testing project

Running tests
-----------

Run tests with maven command 'mvn clean test -DtestngFile=technical_task.xml'
Configure testNG .xml file contains test suites
All properties in main/resources/application.properties

Project Structure
-----------

Directory src/main/ contains all pages classes, commons classes (entities, utils and helper)
Directory src/test/ contains all tests classes
Directory src/test/ resources contains tests files, files of configurations
Used pageFactory pattern, dependent tests, priority of tests (for running in order) and groups for running in suite (smoke/regression...) tests

Project Tools
-------------

Java 1.8, Maven, Selenium WebDriver, TestNG, Log4j

Required for running tests:
--------------------------
- Java 1.8
- Maven
