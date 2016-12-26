/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.component.facade;


/**
 * 业务序列号
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-5-27 李忠文
 */
public class BizSequenceMethodModel /*implements TemplateMethodModelEx */{
    
//    /**
//     * 业务序列号方法
//     * 
//     * @see freemarker.template.TemplateMethodModelEx#exec(java.util.List)
//     */
//    @Override
//    public Object exec(List arguments) throws TemplateModelException {
//        if (arguments == null || arguments.size() < 4) { // key,length,initial,step
//            throw new IllegalArgumentException("参数配置错误！");
//        }
//        String strKey = String.valueOf(arguments.get(0));
//        int iLength = parseInt(arguments.get(1));
//        int iInitial = parseInt(arguments.get(2));
//        int iStep = parseInt(arguments.get(3));
//        BizSequenceAppService objService = AppBeanUtil.getBean(BizSequenceAppService.class);
//        if (objService == null) {
//            throw new RuntimeException("没有找到BizSequenceAppService！");
//        }
//        int iLast = iInitial;
//        synchronized (this.getClass()) {
//            BizSequenceVO objSeq = objService.loadBizSequenceByKey(strKey);
//            if (objSeq == null) {
//                objSeq = new BizSequenceVO();
//                objSeq.setKey(strKey);
//            } else {
//                iLast = objSeq.getLast() + iStep;
//            }
//            objSeq.setLast(iLast);
//            objService.saveBizSequenceByKey(objSeq);
//        }
//        String strResult = String.valueOf(iLast).replace(",", "");
//        return StringUtil.leftPad(strResult, iLength, "0");
//    }
//    
//    /**
//     * 转换成整形数字
//     * 
//     * @param arg 参数
//     * @return 转换成整形数字
//     * @throws TemplateModelException 异常
//     */
//    private int parseInt(Object arg) throws TemplateModelException {
//        int iLength;
//        if (arg instanceof String) {
//            try {
//                iLength = Integer.parseInt(String.valueOf(arg));
//            } catch (NumberFormatException e) {
//                throw new IllegalArgumentException("参数配置错误！", e);
//            }
//        } else if (arg instanceof Integer) {
//            iLength = (Integer) arg;
//        } else if (arg instanceof TemplateNumberModel) {
//            TemplateNumberModel objNumber = (TemplateNumberModel) arg;
//            iLength = objNumber.getAsNumber().intValue();
//        } else {
//            throw new IllegalArgumentException("参数配置错误！");
//        }
//        return iLength;
//    }
    
}
