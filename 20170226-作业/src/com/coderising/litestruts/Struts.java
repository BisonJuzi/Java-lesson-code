package com.coderising.litestruts;

import java.lang.reflect.Method;
import java.util.Map;

public class Struts {

	// Map<String,ActionConfig> configuration = new HashMap<>();
	private final static Configuration cfg = new Configuration("struts.xml");
	
	// actionName：action.name
	// Map<String,String>：key-value，比如<("name","test"),("password","1234")>
    public static View runAction(String actionName, Map<String,String> parameters) {

        /*
         
		0. 读取配置文件struts.xml（设计一个类把字段和属性读出来转化成一个对象）
 		
 		1. 根据actionName找到相对应的class，例如LoginAction,
 		通过反射实例化（创建对象），根据parameters中的数据，调用对象的setter方法，
 		例如parameters中的数据是("name"="test", "password"="1234"),
 		那就应该调用 setName和setPassword方法；
		
		2. 通过反射调用对象的execute方法，并获得返回值，例如"success"；
		
		3. 通过反射找到对象的所有getter方法（例如 getMessage）, 
		通过反射来调用， 把值和属性形成一个HashMap , 
		例如 {"message": "登录成功"} , 放到View对象的parameters。
		
		4. 根据struts.xml中的 <result> 配置,以及execute的返回值， 
		确定哪一个jsp页面 放到View对象的jsp字段中。
        
        */
    	
    	/**
    	 * ActionConfig（<action.name，action.class，action.result>）
    	 * action.result（<result.name，result.jsp>...）
    	 * 返回类的名称
    	 */
    	String clzName = cfg.getClassName(actionName);
    	
    	if(clzName == null){
    		return null;
    	}
    	
    	try {
    		// 通过反射实例化
    		Class<?> clz = Class.forName(clzName);
			Object action = clz.newInstance();
			
			// 根据parameters中的数据，调用对象的setter方法，
			// 例如parameters中的数据是("name"="test", "password"="1234"),
	 		// 那就应该调用 setName和setPassword方法；
			ReflectionUtil.setParameters(action, parameters);
			
			Method m = clz.getDeclaredMethod("execute");			
			String actionResultName = (String)m.invoke(action); // “success”、“fail”
			String actionResultJsp = cfg.getResultView(actionName, actionResultName);			
			
			Map<String,Object> params = ReflectionUtil.getParamterMap(action);	
			View view = new View();			
			view.setParameters(params);
			view.setJsp(actionResultJsp);
			return view;	
		} catch (Exception e) {		
			e.printStackTrace();
		}
    	return null;
    }    
}
