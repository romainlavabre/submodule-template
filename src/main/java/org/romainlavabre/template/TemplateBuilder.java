package org.romainlavabre.template;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface TemplateBuilder {
    String build( String name );


    String build( String name, Map< String, Object > parameters );


    /**
     * Like {@link #build(String, Map)}, and gives back the variables the template set (<code>#set( $subject = … )</code>).
     */
    Rendered render( String name, Map< String, Object > parameters );


    /**
     * For a plain text (an SMS): never HTML-escaped, even with {@link TemplateConfigurer#setEscapeHtml(boolean)}. The
     * secure introspection still applies.
     */
    String buildText( String name, Map< String, Object > parameters );


    /**
     * @param variables every variable of the context after the merge: the parameters and those the template set
     */
    record Rendered( String content, Map< String, Object > variables ) {

        /**
         * A variable set by the template, as plain text: with {@link TemplateConfigurer#setEscapeHtml(boolean)},
         * the values interpolated in <code>#set( $subject = "Devis $data.number" )</code> are HTML-escaped like any
         * insertion; this undoes it, for a value that is not HTML (the subject of a mail).
         *
         * @return null when the template did not set it, or set something else than a String
         */
        @SuppressWarnings( "deprecation" )
        public String text( String variable ) {
            return variables.get( variable ) instanceof String value
                    ? org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4( value )
                    : null;
        }
    }
}
