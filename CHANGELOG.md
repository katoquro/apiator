v0.3.1 - inline
- update : inline Core HTML Renderer dependencies
- fix : full http path for endpoint card

v0.3.0 - redesign

- update : Core HTML Renderer redesign
- infra : auto-deploy to Bintray from CI 
- add : new ModelTypes 'SYSTEM' and 'ANY'
- add : parsing of getters return types as ModelType for following introspection
- add : @DefaultValues annotation support
- add : info level logging about generation time 
- fix : Core JSON Renderer generation order
- fix : endless recursive parsing of enum fields
- fix : @Context shouldn't be parsed as body param
- fix : explicit generic introspection in return type

v0.2.2 - generics 

- update : Core Renderer Model
- infra : CI and build statuses
- infra : gradle plugin to HTML Renderer development with Livereload
- add : bridge methods support
- add : GenericArrayType support  
- add : nested generic types for generics of generics
- add : support of custom Jax-RS http method annotations 
- fix : WildcardType support

v0.2.1 - stability

- core model refactoring
- add simple fuzzy search for HTML view
- fix endpoint paths in HTML view
- increase test coverage for core functionality

v0.2.0 - HTML

- rename Writer to Renderer
- add core HTML renderer
- add support of Jax-RS param names
- add converter of Enums to ApiTypes
- add README.md
- fix body param recognition for Jax-RS
- introspection fixes

v0.1.0 - initial

- add core model
- add core JSON Writer
- add base support of Jax-RS specification