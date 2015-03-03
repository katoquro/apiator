# Apiator [ ![Download](https://api.bintray.com/packages/ainrif/maven/apiator/images/download.svg) ](https://bintray.com/ainrif/maven/apiator/_latestVersion)

Simple library for auto-documenting of you java/groovy rest-api

## Getting started

To start with Apiator just add one of this to you dependencies in a test scope

**Maven**:
```
#!xml
<repositories>
    <repository>
        <id>ainrif-repo</id>
        <url>http://dl.bintray.com/ainrif/maven</url>
    </repository>
</repositories>

<dependencies>
     <!--...-->
    <dependency>
        <groupId>com.ainrif</groupId>
        <artifactId>apiator</artifactId>
        <version>0.1.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**Gradle**:
```
#!groovy
repositories {
    maven {
        url "http://dl.bintray.com/ainrif/maven"
    }
}

dependencies {
    //...
    testCompile "com.ainrif:apiator:0.1.0"
}
```

After adding you can write a test (in TestNg, jUnit, Spock ...) with such configuration:
```
#!java
// Default config. You can override all properties
ApiatorConfig config = new ApiatorConfig();
// Path.class means you use JAX-RS and all you controllers are annotated with @Path(...)
config.setApiClass(Path.class)
// Create new instance of Apiator and getting api in Json format (json used as default in config)
String coreJsonWriterOut = new Apiator(config).render();

System.out.println(coreJsonWriterOut)
```

## About source...

Now it supports one _provider_ and one _writer_

* _JaxRsProvider_ - allows you to document JAX-RS api
* _CoreJsonWriter_ - allows you to see you Api in Json (Apiator core representation)

## How you can help

* If you find some bugs please create an issue [here](https://bitbucket.org/katoquro/apiator/issues)
* If you want to suggest or vote for feature, visit this [trello board](http://bit.ly/apiator_fr)
* Or make a pool request with your feature