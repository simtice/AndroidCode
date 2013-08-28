import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {

	public static void main(String[] args) throws Exception {
		Student stu = new Student();
		stu.setId(10);
		stu.setAge(30);
		stu.setEmail("simtice");
		stu.setName("xia");
		stu.setSex("nan");

		Field fields[] = stu.getClass().getDeclaredFields();
		final Test test = new Test();
		for (Field field : fields) {
			System.out.println(field.getName());
			if (field.getGenericType().toString().equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
				// 拿到该属性的gettet方法
				/**
				 * 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的
				 * 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
				 * 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范
				 */
				Method m = (Method) stu.getClass().getMethod("get" + test.getMethodName(field.getName()));

				String val = (String) m.invoke(stu);// 调用getter方法获取属性值
				if (val != null) {
					System.out.println("String type:" + val);
				}
			}
			// Method[] methods = stu.getClass().getDeclaredMethods();
			// for (Method m : methods) {
			// System.out.println(m.getName());
			// Integer val = (Integer) m.invoke(stu);
			// System.out.println(val);
			// }
		}
	}
	
	private static String getMethodName(String fildeName) throws Exception{  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'a' + 'A');  
        return new String(items);  
    }  

}


//通过反射获取构造方法初始化子类对象

		CellAppEnter cellAppEnter = ndAppObjects.get(appCoordinate.getCellViewClassName());
                if (cellAppEnter == null) {
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(appCoordinate.getCellViewClassName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (clazz == null) {
                        clazz = RootNDView.class;
                    }
                    Constructor<?> constructor = clazz.getConstructor(new Class[] { Context.class, getClass() });
                    cellAppEnter = (CellAppEnter) constructor.newInstance(new Object[] { mLauncher, this });
                    ndAppObjects.put(appCoordinate.getCellViewClassName(), cellAppEnter);
                } else if (cellAppEnter instanceof WeatherView) {
                    WeatherView weatherView = (WeatherView) cellAppEnter;
                    weatherView.onDestroy();
                }
                
              RootNDView继承了CellAppEnter 
                
