![Logo](./docs/src/docs/img/repo_logo.png)

| [![Build Status][build_img_develop]][build_develop] | [![Artifact Version][artifact_img]][artifact] | [![Gradle plugin][gradle_img]][gradle_plugin] |
|:-:|:-:|:-:|

  [artifact_img]: https://img.shields.io/maven-central/v/com.ainrif.apiator/core.svg?label=Maven%20Central&color=blue
  [artifact]: https://search.maven.org/search?q=g:%22com.ainrif.apiator%22

  [build_img_develop]: https://semaphoreci.com/api/v1/ainrif/apiator/branches/develop/shields_badge.svg
  [build_develop]: https://semaphoreci.com/ainrif/apiator/branches/develop
 
  [gradle_img]: https://img.shields.io/maven-metadata/v?label=gradle-plugin&color=green&logo=gradle&metadataUrl=https://plugins.gradle.org/m2/com/ainrif/apiator/com.ainrif.apiator.gradle.plugin/maven-metadata.xml
  [gradle_plugin]: https://plugins.gradle.org/plugin/com.ainrif.apiator

## What is Apiator

Apiator is a tool for creating API description automatically from annotation based web frameworks and javadoc.
The major idea is to take as much as possible from source code and override where it is needed with powerful configuration.

Currently supported frameworks are:

- [JAX-RS](https://jcp.org/en/jsr/detail?id=311) (such implementations as [Apache CXF](http://cxf.apache.org/), [Jersey](https://jersey.github.io/), [restlet](https://restlet.com)... )
- [Micronaut](https://micronaut.io/)

![Build Status](/docs/src/docs/img/main_window.jpg)

## Getting started

Apiator is available as a [Gradle Plugin](https://docs.apiator.info/#_gradle_plugin).

Another way to use is programmatic configuration.
In `*SmokeSpec.groovy` test you can find examples for different providers and renderers.

All starting steps and many more can be found at [Doc Site](https://docs.apiator.info/#_getting_a_binary).

## We are looking for help

- JavaScript developers to evolve ![html-renderer issues](https://img.shields.io/github/issues/ainrif/apiator/component:html-renderer?color=lightgray&label=html-renderer&logo=github).
- Go lang developers to create standalone hosting to analyze, archive and preview API changes. 
  More information about [Apiator Hangar](https://github.com/ainrif/apiator/issues/16)
- Java/Groovy developers to extend core functionality.
  
If you have found a bug, want to suggest feature or documentation update please create corresponding [issue](https://github.com/ainrif/apiator/issues/new/choose).
Or make a pool request with your changes!