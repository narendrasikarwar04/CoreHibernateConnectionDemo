package org.hibernateconnection.test;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



public class ManageEmployee {
	private static SessionFactory factory;
	public static void main(String[] args){
		
		try
		{
			factory = new Configuration().configure().buildSessionFactory();
		}catch(Throwable ex){
			System.err.println("Falled to create SessionFactory object. "+ ex);
			throw new ExceptionInInitializerError(ex);
		}
		
		ManageEmployee me = new ManageEmployee();
		Integer empId1 = me.addEmployee("Zara","Ali",1000);
		Integer empId2 = me.addEmployee("Daisy","Das",1500);
		Integer empId3 = me.addEmployee("John","Paul",2000);
		
		me.listEmployee();
		
		me.updateEmployee(empId2, 1800);
		
		me.deleteEmployee(empId3);
	}
	public Integer addEmployee(String fname, String lname, int salary){
		Session session = factory.openSession();
		Transaction tx =null;
		Integer employeeID = null;
		try {
			tx = session.beginTransaction();
			Employee employee = new Employee(fname,lname,salary);
			employeeID = (Integer) session.save(employee);
			tx.commit();
		} catch(HibernateException e){
			if(tx != null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employeeID;
	}
	
	public void listEmployee(){
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx =session.beginTransaction();
			List employees = session.createQuery("From Employee").list();
			for(Iterator iterator = employees.iterator(); iterator.hasNext();){
				Employee employee = (Employee) iterator.next();
				System.out.println("First Name: " +employee.getFirstName());
				System.out.println("Last Name: " +employee.getLastName());
				System.out.println("Salary: " +employee.getSalary());
			}
			tx.commit();
		} catch(HibernateException e){
			if(tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.clear();
		}
	}
	
	public void updateEmployee(Integer employeeID, int salary){
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, employeeID);
			employee.setSalary(salary);
			session.update(employee);
			tx.commit();
		} catch(HibernateException e){
			if(tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	public void deleteEmployee(Integer employeeId){
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee)session.get(Employee.class,employeeId);
			session.delete(employee);
			tx.commit();
		}catch(HibernateException e){
			if(tx!=null) tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
}
