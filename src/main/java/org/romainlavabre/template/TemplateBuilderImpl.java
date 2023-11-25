package org.romainlavabre.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
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


    public TemplateBuilderImpl() {
        this.velocityEngineRelative = new VelocityEngine();
        this.velocityEngineRelative.setProperty( RuntimeConstants.RESOURCE_LOADER, "classpath" );
        this.velocityEngineRelative.setProperty( "classpath.resource.loader.class", ClasspathResourceLoader.class.getName() );
        this.velocityEngineRelative.init();

        this.velocityEngineAbsolute = new VelocityEngine();
        this.velocityEngineAbsolute.setProperty( "file.resource.loader.path", "/" );
        this.velocityEngineAbsolute.init();
    }


    @Override
    public String build( final String name ) {
        assert name != null && !name.isBlank() : "variable name should not be null or blank";

        return this.build( name, Map.of() );
    }


    @Override
    public String build( final String name, final Map< String, Object > parameters ) {
        assert name != null && !name.isBlank() : "variable name should not be null or blank";

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
        return TemplateConfigurer.get().getBaseTemplatePath() +
                name +
                ".vm";
    }
}
