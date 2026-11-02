# template

Velocity templates read from the classpath.

```java
TemplateConfigurer.init()
        .setBaseTemplatePath( "templates/" )
        .setEscapeHtml( true )          // 4.1.0: every $reference inserted is HTML-escaped
        .setSecureIntrospection( true ) // 4.1.0: no reflection from a template (getClass().forName…)
        .build();
```

| Method | Result |
|---|---|
| `build( name, parameters )` | the rendered text of `{base}{name}.vm` |
| `render( name, parameters )` (4.2.0) | the text, and the variables of the context after the merge: those the template set with `#set( $subject = … )` included |
| `buildText( name, parameters )` (4.2.0) | a plain text (an SMS): **never** HTML-escaped, the secure introspection still applies |

The escaping applies to the inserted values only: the markup written in a template is not escaped. Accents are
written as entities (`é` → `&eacute;`). The values interpolated in a `#set` are escaped too:
`render( … ).text( "subject" )` reads such a variable back as plain text (the subject of a mail).
