package org.romainlavabre.template.exception;

public class NotInitializedException extends RuntimeException{
    public NotInitializedException(){
        super("Use TemplateConfigurer for fix it");
    }
}
