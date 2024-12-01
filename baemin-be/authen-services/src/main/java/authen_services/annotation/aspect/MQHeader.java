package authen_services.annotation.aspect;

import authen_services.annotation.MQEndpoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MQHeader {


    @Around("@annotation(authen_services.annotation.MQEndpoint)")
    public Object validateHeader(ProceedingJoinPoint joinPoint, MQEndpoint checkHeader) throws Throwable {
        //Get method arguments
//        Object[] args = joinPoint.getArgs();
//        System.out.println("args la clg: "+ args.toString());
//        if (args.length > 1 && args[1] instanceof Message) {
//            // Extract the Message object
//            Message message = (Message) args[1];
//            String headerValue = (String) message.getMessageProperties().getHeader("endpoint");
//
//            System.out.println("Where Am I going: " + headerValue);
//
//            // Validate the header value
//            if (headerValue != null && headerValue.equals(checkHeader.endpoint()) ) {
//                // Proceed if the header value matches
//                return joinPoint.proceed();
//            } else {
//                throw new IllegalStateException("Invalid header value: " + headerValue);
//            }
//        } else {
//            throw new RuntimeException("Invalid argument type: " + args[1].getClass().getName());
//        }
        return null;
    }


}
