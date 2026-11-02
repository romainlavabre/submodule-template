package org.romainlavabre.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.EscapeHtmlReference;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.util.introspection.SecureUberspector;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class TemplateBuilderImpl implements TemplateBuilder {

    protected final static String         ENCODING = "UTF-8";
    protected              VelocityEngine velocityEngineRelative;
    protected              VelocityEngine velocityEngineAbsolute;


    @Override
    public String build( final String name ) {
        assert name != null && !name.isBlank() : "variable name should not be null or blank";

        return this.build( name, Map.of() );
    }


    @Override
    public String build( final String name, final Map< String, Object > parameters ) {
        assert name != null && !name.isBlank() : "variable name should not be null or blank";

        initEngines();

        String path = getPath( name );

        final Template template = path.startsWith( "/" )
                ? this.velocityEngineAbsolute.getTemplate( path, TemplateBuilderImpl.ENCODING )
                : this.velocityEngineRelative.getTemplate( path, TemplateBuilderImpl.ENCODING );

        final VelocityContext velocityContext = new VelocityContext();

        if ( parameters != null ) {
            parameters.forEach( velocityContext::put );
        }

        return this.getContent( velocityContext, template );
    }


    protected String getContent( final VelocityContext velocityContext, final Template template ) {
        final StringWriter content = new StringWriter();

        template.merge( velocityContext, content );

        return content.toString();
    }


    protected String getPath( final String name ) {
        if ( name.startsWith( "//" ) ) {
            return name.substring( 1);
        }

        return TemplateConfigurer.get().getBaseTemplatePath() +
                name +
                ".vm";
    }


    /**
     * At the first build and not in the constructor: the configuration is usually built after this bean.
     */
    protected synchronized void initEngines() {
        if ( this.velocityEngineRelative != null ) {
            return;
        }

        TemplateConfigurer configurer = TemplateConfigurer.getIfInitialized();

        VelocityEngine relative = new VelocityEngine();
        relative.setProperty( RuntimeConstants.RESOURCE_LOADERS, "classpath" );
        relative.setProperty( "resource.loader.classpath.class", ClasspathResourceLoader.class.getName() );
        secure( relative, configurer );
        relative.init();

        VelocityEngine absolute = new VelocityEngine();
        absolute.setProperty( "resource.loader.file.path", "/" );
        secure( absolute, configurer );
        absolute.init();

        this.velocityEngineAbsolute = absolute;
        this.velocityEngineRelative = relative;
    }


    protected void secure( VelocityEngine engine, TemplateConfigurer configurer ) {
        if ( configurer == null ) {
            return;
        }

        if ( configurer.isEscapeHtml() ) {
            engine.setProperty( RuntimeConstants.EVENTHANDLER_REFERENCEINSERTION, EscapeHtmlReference.class.getName() );
        }

        if ( configurer.isSecureIntrospection() ) {
            engine.setProperty( RuntimeConstants.UBERSPECT_CLASSNAME, SecureUberspector.class.getName() );
        }
    }
}
