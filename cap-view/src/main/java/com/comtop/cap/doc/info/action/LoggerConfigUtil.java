/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.doc.info.action;

import static com.comtop.corm.resource.util.Assert.notNull;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.sift.MDCBasedDiscriminator;
import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.sift.AppenderFactory;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;

/**
 * 日志配置工具
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2015年12月11日 lizhongwen
 */
public class LoggerConfigUtil {
    
    /**
     * 更新日志配置，动态设置日志路径
     * 
     * @param loggerName 日志名称
     * @param appenderName 日志记录名称
     * @param path 日志文件路径
     */
    public static final void update(final String loggerName, final String appenderName, final String path) {
        notNull(path);
        ILoggerFactory factory = LoggerFactory.getILoggerFactory();
        Logger logger = LoggerFactory.getLogger(loggerName);
        MDC.put("path", path);
        if (logger instanceof ch.qos.logback.classic.Logger) { // logback
            ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) logger;
            Appender<ILoggingEvent> append = log.getAppender(appenderName);
            LoggerContext context = (LoggerContext) factory;
            // log = context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
            SiftingAppender sifting;
            MDCBasedDiscriminator discriminator;
            StatusManager sm;
            if (append == null) {
                context.reset();
                sm = context.getStatusManager();
                if (sm != null) {
                    sm.add(new InfoStatus("Configuring logger", context));
                }
                discriminator = new MDCBasedDiscriminator();
                discriminator.setContext(context);
                discriminator.setKey("path");
                discriminator.setDefaultValue("./docx.log");
                discriminator.start();
                sifting = new SiftingAppender();
                sifting.setName("SIFT");
                sifting.setContext(context);
                sifting.setDiscriminator(discriminator);
                sifting.setAppenderFactory(new AppenderFactory<ILoggingEvent>() {
                    
                    @Override
                    public Appender<ILoggingEvent> buildAppender(Context cxt, String discriminatingValue)
                        throws JoranException {
                        FileAppender<ILoggingEvent> appender = new FileAppender<ILoggingEvent>();
                        appender.setContext(cxt);
                        appender.setName(discriminatingValue);
                        appender.setAppend(false);
                        appender.setFile(discriminatingValue);
                        // filter
                        ThresholdFilter filter = new ThresholdFilter();
                        filter.setLevel(Level.ERROR.toString());
                        filter.setContext(cxt);
                        filter.start();
                        appender.addFilter(filter);
                        // encoder
                        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
                        encoder.setImmediateFlush(true);
                        // encoder.setCharset(Charset.forName("UTF-8"));
                        encoder.setPattern("[%-5p] %d{yyyy-MM-dd HH:mm:ss} \\(%file:%line\\) - %m%n");
                        encoder.setContext(cxt);
                        encoder.start();
                        appender.setEncoder(encoder);
                        appender.start();
                        return appender;
                    }
                });
                sifting.start();
                log.addAppender(sifting);
                // log.setLevel(Level.DEBUG);
            } else if (!append.isStarted()) {
                append.start();
            }
        } else if (logger instanceof org.apache.log4j.Logger) { // log4j
        
        }
    }
    
    /**
     * 技术以前配置的日志
     *
     * @param loggerName 日志名称
     * @param appenderName 日志记录名称
     */
    public static void finish(final String loggerName, final String appenderName) {
        Logger logger = LoggerFactory.getLogger(loggerName);
        if (logger instanceof ch.qos.logback.classic.Logger) { // logback
            ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) logger;
            Appender<ILoggingEvent> append = log.getAppender(appenderName);
            if (append != null) {
                append.stop();
            }
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.removeObject(appenderName);
            context.removeObject(loggerName);
            context.reset();
        }
    }
}
