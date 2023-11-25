package org.romainlavabre.template;

import org.romainlavabre.template.exception.NotInitializedException;

public class TemplateConfigurer {
    private static TemplateConfigurer INSTANCE;
    private String baseTemplatePath;

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

    public void build(){}


    protected String getBaseTemplatePath() {
        return baseTemplatePath;
    }
}
