package org.romainlavabre.template;

import org.romainlavabre.template.exception.NotInitializedException;

public class TemplateConfigurer {
    private static TemplateConfigurer INSTANCE;
    private String baseTemplatePath;
    private boolean escapeHtml;
    private boolean secureIntrospection;

    public TemplateConfigurer(){
        INSTANCE = this;
    }

    public static TemplateConfigurer get(){
        if ( INSTANCE == null ){
            throw new NotInitializedException();
        }

        return INSTANCE;
    }

    public static TemplateConfigurer init(){
        return new TemplateConfigurer();
    }

    public TemplateConfigurer setBaseTemplatePath(String path){
        baseTemplatePath = path;

        return this;
    }


    /**
     * Every inserted reference ($value) is HTML-escaped: a value can no longer inject markup. There is no raw
     * output left, so a template must not insert HTML held by a variable. Read once, at the first build.
     */
    public TemplateConfigurer setEscapeHtml( boolean escapeHtml ) {
        this.escapeHtml = escapeHtml;

        return this;
    }


    /**
     * Templates can no longer reach classes, class loaders, reflection, threads or the runtime through the
     * objects of their context. Read once, at the first build.
     */
    public TemplateConfigurer setSecureIntrospection( boolean secureIntrospection ) {
        this.secureIntrospection = secureIntrospection;

        return this;
    }

    public void build(){}


    protected String getBaseTemplatePath() {
        return baseTemplatePath;
    }


    protected boolean isEscapeHtml() {
        return escapeHtml;
    }


    protected boolean isSecureIntrospection() {
        return secureIntrospection;
    }


    /**
     * The configuration if one was built, null otherwise: the engines start with the defaults.
     */
    protected static TemplateConfigurer getIfInitialized() {
        return INSTANCE;
    }
}
