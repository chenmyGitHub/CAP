cap-bm-fragment �ǻ���servlet3.0�淶����ԭ��cap-webapp�����е�web.xml�й���cap��ģ��ص����ó�ȡ�������ŵ�web-fragment.xml�С�
ͬʱ��cap��ģ�õ���dwr������ó�ȡ��cip_bm_dwr.xml�С�


1.�˹�������servlet3.0�淶������Ҫ��tomcat7�����С�
2.����Ŀǰ���Խ�����ù�����Ҫ���jar��������lib�����У�������Ч��

Ŀǰ�����ڵ����⣺
web.xml�е��������ȼ��ϸߣ��޷���web-fragment.xml�еĹ������ķ���web.xml�й�����ǰ����Ч��
���ԭ������web.xml�еĹ�����com.comtop.top.sys.login.action.SessionFilter ǰ���cap������com.comtop.cap.ptc.login.filter.CapSessionFilter��
�����Ǻ���Ч�ģ������޷�ֱ��ͨ��/web/cap����CAP��ģ��¼��ֻ�ܷ���/web/CapInitLogin.ac����cap��ģ��¼��ڡ�


cap-bm-fragment�÷�

1.����cap-bm-fragment ����
2.��cap-webapp���̣�����������copyDependenciesJarToWebApp�����Ҫ��Ϊ�˽�cap-bm-fragment���̴��
3.ԭ������/web/cap����CAP��ģ��¼��������Ҫ����/web/CapInitLogin.ac��

4.������ʹ��tomcat6����web-inf���ϴ���web - tomcat6.xml �ļ��������滻��ԭ����web.xml�Ϳ����ˡ�