v0.12.x

- new: Gradle plugin with dedicated source set for configuration
- new: CONTRIBUTING.md
- fix: Memoization issue which could lead to hang up during schema rendering
- improvement: UI stack was moved to Svelte.js
- improvement: Micronaut support

v0.11.x

- improvement: html was moved to parcel.js
- improvement: smoke specs for jax-rs provider were refactored

v0.10.x

- update: upgrade to JDK11
- update: dependencies were updated
- update: repo hosting changed from bitbucket to github
- improvement: reduce scope for javadoc scan. Start package is required
- improvement: JDK9 javadoc toolchain is used instead of rt tools jar

v0.9.x

- new: micronaut provider
- new: path transformation plugin
- improvement: reduce amount of file scans for javadoc
- improvement: jsr310 plugin becomes a core plugin. jdk8+ required

v0.8.x - Renderer Plugins

- new: gradle plugin
- new: plugins model for JSON renderer configuration
- new: defaultValues for model types (`@Param`, jax-rs `@DefaultValue`)
- new: info about optional params/fields in model and endpoints (`@Param`, javax `@Nullable`)
- new: support jackson annotation to override property names
- new: all-in-one Apiator module (requires java 8)
- update: doclet configuration was separated from Core JSON Renderer
- improvement: source path detection now includes model classes
- improvement: html renderer sources were refactored according to ui components

v0.7.x - Upgrade HTML View

- new : new annotation to allow to specify types for returns
- new : tech views for enums and types
- new : support of custom http verbs
- fix : enums are included in model search by `!model`
- update : transitive dependencies were reduced

v0.6.x - Technical boost

- update : core features were split across modules
- update : project versioning system
- update : livereload uses port as debug indicator
- update : Groovy code style
- update : legal info

v0.5.x - Dark theme

- new : JavaDoc Support
- new : JsDoc descriptions for code completion
- fix : JDK classes introspection
- fix : HTML renderer permalink restore state
- update : HTML renderer theme and design
- update : fuzzy search

v0.4.0 - HTML renderer update

- new : template names for api types
- new : documentation and feedback links
- new : raw endpoint view in HTML Renderer
- new : graphic assets
- fix : permalink with GET params
- update : HTML renderer dev kit

v0.3.1 - inline

- fix : full http path for endpoint card
- update : inline Core HTML Renderer dependencies

v0.3.0 - redesign

- new : new ModelTypes 'SYSTEM' and 'ANY'
- new : parsing of getters return types as ModelType for following introspection
- new : `@DefaultValues` annotation support
- new : info level logging about generation time
- fix : Core JSON Renderer generation order
- fix : endless recursive parsing of enum fields
- fix : `@Context` shouldn't be parsed as body param
- fix : explicit generic introspection in return type
- update : Core HTML Renderer redesign
- improvement : auto-deploy to Bintray from CI

v0.2.2 - generics

- new : bridge methods support
- new : GenericArrayType support
- new : nested generic types for generics of generics
- new : support of custom Jax-RS http method annotations
- fix : WildcardType support
- update : Core Renderer Model
- improvement : infra - CI and build statuses
- improvement : infra - gradle plugin to HTML Renderer development with Livereload

v0.2.1 - stability

- new : simple fuzzy search for HTML view
- fix : endpoint paths in HTML view
- improvement : core model refactoring
- improvement : increase test coverage for core functionality

v0.2.0 - HTML

- new : core HTML renderer
- new : support of Jax-RS param names
- new : converter of Enums to ApiTypes
- new : README.md
- fix : body param recognition for Jax-RS
- fix : introspection
- improvement : rename Writer to Renderer

v0.1.0 - initial

- new : core model
- new : core JSON Writer
- new : base support of Jax-RS specification
