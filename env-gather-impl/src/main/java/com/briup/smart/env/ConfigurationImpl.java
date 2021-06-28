package com.briup.smart.env;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.Log;
/**
 * 配置模块
 * @author Sking
 *
 */

public class ConfigurationImpl implements Configuration{
	
	

	private static final Configuration configuration =new ConfigurationImpl();

	//保存各个模块的实例对象
	private static Map<String,Object> map=new HashMap<String, Object>();
	
	//保存各个模块中的变量值
	private static Properties properties=new Properties();
	
	static {
		//创建解析器对象
		SAXReader sax=new SAXReader();
		
		//加载xml文件，获取文档树对象
		try {
			Document read = sax.read(new File("src/main/resources/conf.xml"));
			
			//获取根节点
			Element rootElement = read.getRootElement();
			
			//获取子节点
			List<Element> elements = rootElement.elements();
			
			//遍历子节点
			for (Element element : elements) {
				//得到节点的名字
				String name = element.getName();
				
				//获取对应节点的属性
				Attribute attribute = element.attribute("class");
				//获取属性值
				String attrValue = attribute.getValue();
				
				//通过全限定名获得Class对象
				Class<?> clazz = Class.forName(attrValue);
				
				//通过反射中的newInstance方法创建对应模块对象
				Object object = clazz.newInstance();
				
				//将各个模块的实例对象添加到集合中
				map.put(name, object);
				
				//获取当前节点的子节点
				List<Element> childelements = element.elements();
				for (Element childelement : childelements) {
					String childname = childelement.getName();
					String childtext = childelement.getText();
					properties.setProperty(childname, childtext);
				}
			}
			/*map.forEach((k,v)->{
				System.out.println(k+":"+v);
			});*/
			/*properties.forEach((k,v)->{
				System.out.println(k+":"+v);
			});*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 为其他模块初始化
	 */
	public static void initModel() {
		//得到各个模块的实例对象
		Collection<Object> objects = map.values();
		for (Object object : objects) {
//			object.init();
//			object.setConfiguration();
			//判断是否是Configuration
			if(object instanceof ConfigurationAware) {
				try {
					((ConfigurationAware) object).setConfiguration(configuration);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (object instanceof PropertiesAware) {
				try {
					((PropertiesAware) object).init(properties);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	public static Configuration getInstance() {
		return configuration;
	}
	

	@Override
	public Log getLogger() throws Exception {
		// TODO Auto-generated method stub
		return (Log) map.get("logger");
	}

	@Override
	public Server getServer() throws Exception {
		// TODO Auto-generated method stub
		return (Server) map.get("sever");
	}

	@Override
	public Client getClient() throws Exception {
		// TODO Auto-generated method stub
		return (Client) map.get("client");
	}

	@Override
	public DBStore getDbStore() throws Exception {
		// TODO Auto-generated method stub
		return (DBStore) map.get("dbStore");
	}

	@Override
	public Gather getGather() throws Exception {
		// TODO Auto-generated method stub
		return (Gather) map.get("gather");
	}

	@Override
	public Backup getBackup() throws Exception {
		// TODO Auto-generated method stub
		return (Backup) map.get("backup");
	}
	
	
	public static void main(String[] args) throws Exception {
		/*ConfigurationImpl configurationImpl = new ConfigurationImpl();
		Backup backup = configurationImpl.getBackup();
		Client client = configurationImpl.getClient();
		System.out.println(backup);
		System.out.println(client);*/
	}

}
