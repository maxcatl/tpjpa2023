package fr.istic.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class RestAspect
{
    private final Logger logger = Logger.getLogger("RestAspect");
    @Before("execution(public * fr.istic..rest.*.* (..))")
    public void log(JoinPoint jpp)
    {
        StringBuilder builder = new StringBuilder("\nAppel API : " + jpp.getSignature().toString());

        if (jpp.getArgs().length > 0)
            builder.append(" avec les paramètres :\n");

        for (int i=0 ; i<jpp.getArgs().length ; i++)
            builder.append(i + " : " + jpp.getArgs()[i] + "\n");

        logger.info(builder.toString());
    }
}