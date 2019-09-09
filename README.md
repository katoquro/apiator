![Logo](./docs/src/docs/img/repo_logo.png)

|               |                       Stable                      |                    develop-SNAPSHOT                    |
|---------------|:-------------------------------------------------:|:------------------------------------------------------:|
| Artifact repo |       [![Download][artifact_img]][artifact]       | [![Develop][artifact_img_snapshot]][artifact_snapshot] |
| Build status  |                         -                         |   [![Build Status][build_img_develop]][build_develop]  |

  [artifact_img]: https://api.bintray.com/packages/ainrif/maven/apiator/images/download.svg
  [artifact]: https://bintray.com/ainrif/maven/apiator/_latestVersion

  [artifact_img_snapshot]: https://img.shields.io/badge/JitPack-develop-blue.svg
  [artifact_snapshot]: https://jitpack.io/#com.github.ainrif/apiator/develop-SNAPSHOT
  [build_img_develop]: https://semaphoreci.com/api/v1/ainrif/apiator/branches/develop/shields_badge.svg
  [build_develop]: https://semaphoreci.com/ainrif/apiator/branches/develop

## What is Apiator

Apiator is a tool for creating API description automatically from annotation based web frameworks and javadoc.
The major idea is to take as much as possible from source code and override where it is needed with powerful configuration.

Currently supported frameworks are:

- [JAX-RS](https://jcp.org/en/jsr/detail?id=311) (such implementations as [Apache CXF](http://cxf.apache.org/), [Jersey](https://jersey.github.io/), [restlet](https://restlet.com)... )
- [Micronaut](https://micronaut.io/)

## Getting started

Apiator is available as a [Gradle Plugin](https://apiator.ainrif.com/#_gradle_plugin).

Another way to use is programmatic configuration.
In `*SmokeSpec.groovy` test you can find examples for different providers and renderers.

All starting steps and many more can be found at [Doc Site](https://apiator.ainrif.com/#_getting_a_binary).

## How you can help

* If you find some bugs please create an [issue](https://github.com/ainrif/apiator/issues)
* If you want to suggest feature create an [feature request](https://github.com/ainrif/apiator/issues)
* Or make a pool request with your feature