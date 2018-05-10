v0.8.x

- new: plugins model for JSON renderer configuration
- add: support jackson annotation to override property names
- improvement: source path detection now includes model classes

v0.7.x 

- update : transitive dependencies were reduced
- add : new annotation to allow to specify types for returns
- add : tech views for enums and types
- add : support of custom http verbs
- fix : enums are included in model search by !model

v0.6.0 - Technical boost

- update : core features were split across modules
- update : project versioning system
- update : livereload uses port as debug indicator
- update : Groovy code style
- update : legal info 

v0.5.1

- fix : HTML renderer permalink restore state

v0.5.0 - Dark theme

- update : HTML renderer theme and design
- update : fuzzy search
- add : JavaDoc Support
- add : JsDoc descriptions for code completion 
- fix : JDK classes introspection

v0.4.0 - HTML renderer update

- update : HTML renderer dev kit
- add : template names for api types
- add : documentation and feedback links
- add : raw endpoint view in HTML Renderer
- add : graphic assets
- fix : permalink with GET params

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