# 1source-api

Examples and utilities for accessing the 1Source REST API. You can find out more about 1Source at [https://equilend.com](https://equilend.com). Access to stageauth.equilend.com is required to run most examples. Please contact <a name="1source_help" href="mailto:1source_help@equilend.com">1Source Help</a> for assistance.

## Requirements

Building the API examples requires:
1. Java 1.7+
2. Maven/Gradle

### Maven users

API examples include a dependency on the 1Source Client API library:

```xml
<dependency>
  <groupId>com.os</groupId>
  <artifactId>1source-api-client</artifactId>
  <version>1.1.0</version>
  <scope>compile</scope>
</dependency>
```

This library is hosted in the 1Source GitHub Packages repository. To download SNAPSHOT artifacts, enable SNAPSHOTS in the POM of the consuming project or your ~/.m2/settings.xml file. Replace USERNAME with your GitHub username, and TOKEN with your personal access token that has read:packages permission.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo1.maven.org/maven2</url>
        </repository>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/Equilend/1source-codegen-client-api</url>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    <server>
      <id>github</id>
      <username>USERNAME</username>
      <password>TOKEN</password>
    </server>
  </servers>
</settings>
```

### Gradle users

This dependency will be in the build file:

```groovy
compile "com.os:1source-api-client:1.1.0"
```

Add the repository to your build.gradle file (Gradle Groovy). Replace USERNAME with your GitHub username, and TOKEN with your personal access token that has read:packages permission.

```repositories {
    maven {
        url = uri("https://maven.pkg.github.com/Equilend/1source-codegen-client-api")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
   }
}
```

## Author

Matthew Schoenberg

## Last Update

Thursday, October 17, 2024 08:53:10
