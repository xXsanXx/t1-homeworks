package com.synthetic_human.core_starter.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    private final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    private final AuditMode auditMode;
    private final String auditTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public AuditAspect(@Value("${synthetic_human.audit.mode:CONSOLE}") String auditMode,
                       @Value("${synthetic_human.audit.kafka.topic:synthetic_human-log}") String auditTopic,
                       KafkaTemplate<String, String> kafkaTemplate) {
        if (auditMode.equals(AuditMode.CONSOLE.name()))
            this.auditMode = AuditMode.CONSOLE;
        else if (auditMode.equals(AuditMode.KAFKA.name()))
            this.auditMode = AuditMode.KAFKA;
        else
            throw new IllegalArgumentException(String.format("synthetic_human.audit.mode is invalid: %s.", auditMode));
        this.auditTopic = auditTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Around("@annotation(com.synthetic_human.core_starter.audit.WeylandWatchingYou)")
    public Object logMethodInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Executing method ");
        stringBuilder.append(joinPoint.getSignature().getName());

        stringBuilder.append(" with arguments ");
        stringBuilder.append(Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();
        stringBuilder.append(". Result : ");
        stringBuilder.append(result);

        logger.info(stringBuilder.toString());

        if (auditMode == AuditMode.CONSOLE)
            logger.info(stringBuilder.toString());
        else
            kafkaTemplate.send(auditTopic, stringBuilder.toString());
        return result;
    }
}
