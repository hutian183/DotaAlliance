package com.dataturn.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.ProcessingInstruction;
import org.dom4j.VisitorSupport;
import com.dataturn.bean.Message;



public class XmlUtil {

	public static void main(String[] args) {
		String xmlString = "<message><ip>123.122.121.12</ip></message>";
		Message message = XmlToBean(xmlString, Message.class);
		System.out.println(BeanToXml(message));
	}


	/**
	 * 设置属性
	 * 
	 * @param t
	 * @param propertyName
	 * @param propertyValue
	 * @throws Exception
	 */
	public static <T> void setProperty(T t, String propertyName,
			String propertyValue) {
		PropertyDescriptor propDesc;
		try {
			propDesc = new PropertyDescriptor(propertyName, t.getClass());
			Method methodSetUserName = propDesc.getWriteMethod();
			methodSetUserName.invoke(t, propertyValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static <T> String BeanToXml(T t) {
		String name = t.getClass().getSimpleName();
		StringBuilder propXml = new StringBuilder("<" + name + ">");

		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(t.getClass());
			PropertyDescriptor[] proDescrtptors = beanInfo
					.getPropertyDescriptors();
			if (proDescrtptors != null && proDescrtptors.length > 0) {
				for (PropertyDescriptor propDesc : proDescrtptors) {
					String nodeName = propDesc.getName();
					if("class".equals(nodeName))
						continue;
					Object nodeValue = propDesc.getReadMethod().invoke(t)
							;
					propXml.append("<" + nodeName + ">" + (nodeValue==null?"":nodeValue )+ "</"
							+ nodeName + ">");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		propXml.append("</" + name + ">");
		return propXml.toString();
	}

	public static <W> W XmlToBean(String xmlString, Class<W> tclass) {
		try {
			W w = tclass.newInstance();
			Document doc = DocumentHelper.parseText(xmlString);
			doc.accept(new VistorSet<W>(w));
			return w;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}

class VistorSet<T> extends VisitorSupport {
	private T t;

	public VistorSet(T t) {
		this.t = t;
	}

	public T getT() {
		return t;
	}

	public void visit(Attribute node) {
		//System.out.println("Attibute: " + node.getName() + "="
		//		+ node.getValue());
	}

	public void visit(Element node) {
		if (node.isTextOnly()) {
			//System.out.println("Element: " + node.getName() + "="
			//		+ node.getText());
			XmlUtil.setProperty(t, node.getName(), node.getText());
		} else {
			//System.out.println(node.getName());
		}
	}

	@Override
	public void visit(ProcessingInstruction node) {
		//System.out.println("PI:" + node.getTarget() + " " + node.getText());
	}
}
