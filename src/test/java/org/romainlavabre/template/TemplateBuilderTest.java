package org.romainlavabre.template;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateBuilderTest {

    private static final TemplateBuilder BUILDER = new TemplateBuilderImpl();


    @BeforeAll
    public static void configure() {
        TemplateConfigurer.init()
                .setBaseTemplatePath( "templates/" )
                .setEscapeHtml( true )
                .setSecureIntrospection( true )
                .build();
    }


    @Test
    public void render_gives_back_the_variables_set_by_the_template_and_escapes_the_values() {
        TemplateBuilder.Rendered rendered = BUILDER.render( "test/mail", Map.of( "data", Map.of( "number", "D-12", "name", "<b>Léa</b>" ) ) );

        assertThat( rendered.content() ).contains( "<p>&lt;b&gt;L&eacute;a&lt;/b&gt;</p>" );
        assertThat( rendered.variables() ).containsEntry( "subject", "Devis D-12" ).containsKey( "data" );
    }


    @Test
    public void a_variable_set_by_the_template_is_read_back_as_plain_text() {
        TemplateBuilder.Rendered rendered = BUILDER.render( "test/mail", Map.of( "data", Map.of( "number", "Léa & <Cie>", "name", "x" ) ) );

        assertThat( rendered.variables().get( "subject" ) ).isEqualTo( "Devis L&eacute;a &amp; &lt;Cie&gt;" );
        assertThat( rendered.text( "subject" ) ).isEqualTo( "Devis Léa & <Cie>" );
        assertThat( rendered.text( "missing" ) ).isNull();
    }


    @Test
    public void build_text_is_never_escaped_and_stays_secured() {
        String text = BUILDER.buildText( "test/sms", Map.of( "data", Map.of( "name", "Léa & <Cie>", "reflection", "x" ) ) );

        assertThat( text ).startsWith( "RDV chez Léa & <Cie>." ).doesNotContain( "class java.lang.Runtime" );
    }


    @Test
    public void build_still_escapes() {
        String html = BUILDER.build( "test/sms", Map.of( "data", Map.of( "name", "Léa & <Cie>", "reflection", "x" ) ) );

        assertThat( html ).startsWith( "RDV chez L&eacute;a &amp; &lt;Cie&gt;." ).doesNotContain( "class java.lang.Runtime" );
    }
}
