package com.zdp.sharding.jdbc.demo;

import com.zdp.sharding.jdbc.demo.dao.TransDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author <a href="mailto:zhoudapeng8888@126.com">zhoudapeng</a>
 * Date 2018/6/4
 * Time 下午9:56
 */
public class Demo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-*.xml");
        TransDAO transDAO  = (TransDAO) context.getBean("transDAO");
//        Trans trans = new TransBuilder().setTransId("1").setTransName("1").build();
//        transDAO.insert(trans);
//        trans = new TransBuilder().setTransId("2").setTransName("2").build();
//        transDAO.insert(trans);
//        trans = new TransBuilder().setTransId("3").setTransName("3").build();
//        transDAO.insert(trans);
//        trans = new TransBuilder().setTransId("4").setTransName("4").build();
//        transDAO.insert(trans);
        System.out.println(transDAO.query(new TransBuilder().setTransId("1").build()));
        System.out.println(transDAO.query(new TransBuilder().setTransId("2").build()));
        System.out.println(transDAO.query(new TransBuilder().setTransId("3").build()));
        System.out.println(transDAO.query(new TransBuilder().setTransId("4").build()));
    }
}
