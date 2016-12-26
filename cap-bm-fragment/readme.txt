cap-bm-fragment 是基于servlet3.0规范，将原本cap-webapp工程中的web.xml中关于cap建模相关的配置抽取出来，放到web-fragment.xml中。
同时将cap建模用到的dwr相关配置抽取到cip_bm_dwr.xml中。


1.此工程依据servlet3.0规范整理，需要在tomcat7下运行。
2.依据目前测试结果，该工程需要达成jar包，放在lib中运行，才能生效。

目前还存在的问题：
web.xml中的配置优先级较高，无法将web-fragment.xml中的过滤器的放在web.xml中过滤器前面生效。
因此原来放在web.xml中的过滤器com.comtop.top.sys.login.action.SessionFilter 前面的cap过滤器com.comtop.cap.ptc.login.filter.CapSessionFilter。
现在是后生效的，导致无法直接通过/web/cap进入CAP建模登录。只能访问/web/CapInitLogin.ac进入cap建模登录入口。


cap-bm-fragment用法

1.导入cap-bm-fragment 工程
2.在cap-webapp工程，重新运行下copyDependenciesJarToWebApp命令，主要是为了将cap-bm-fragment工程打包
3.原来访问/web/cap进入CAP建模登录。现在需要访问/web/CapInitLogin.ac。

4.若还是使用tomcat6，在web-inf下上传了web - tomcat6.xml 文件，将他替换掉原来的web.xml就可以了。