/**
 * 
 */
package com.kk.persist.impl;

import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kk.persist.config.PersistConfigManager;
import com.kk.persist.service.DBConnection;

/**
 * @author Kartikeya
 *
 *
 */
public final class DBConnectionImpl implements DBConnection {

	private static DBConnectionImpl instance = null;
	
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet resultSet = null;
	private Properties properties = null;
	private DBConnectionImpl(){
		PersistConfigManager pConfigMgr = new PersistConfigManager();
		properties = pConfigMgr.configurePersist();
	}
	
	public static DBConnection getInstance(){
		if(instance == null){
			instance = new DBConnectionImpl();
		}
		return instance;
	}
	
	@Override
	public <T> T retrieveData(String query, Map<Integer, Object> params,
			Class<? extends Object> cls) {
		// TODO Auto-generated method stub
		try {
			con = getConnection();
			pstmt = con.prepareStatement(query);
			if(params != null){
				for(Integer key : params.keySet()){
					pstmt.setObject(key, params.get(key));
				}
			}
			resultSet = pstmt.executeQuery();
			if(resultSet.next()){
				System.out.println("Data present" + (Integer)resultSet.getObject(1));
				
			}
			return (T)fillData(resultSet, cls);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public <T> T updateData(String query, Map<Integer, Object> params,
			Class<? extends Object> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T insertData(String query, Map<Integer, Object> params,
			Class<? extends Object> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T bulkInsert(String query, Map<Integer, Object> params,
			Class<? extends Object> cls) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Connection getConnection(){
		Connection con = null;
		try {
			Class.forName(properties.getProperty("persist.connection.driver"));
			con = DriverManager.getConnection(properties.getProperty("persist.connection.url"), 
					properties.getProperty("persist.connection.username"), 
					properties.getProperty("persist.connection.password"));
			return con;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(NullPointerException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SQLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return null;
	}
	
	private List fillData(ResultSet result, Class cls) 
			throws SQLException{
		List listVO = new ArrayList();
		//Class cls = obj.getClass();
		Object obj = null;
		try {
			obj = cls.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSetMetaData rsmdt = result.getMetaData();
		String fieldName = null;
		Map<Integer,String> fieldsMap = new HashMap<Integer,String>();
		int columnCount = rsmdt.getColumnCount();
		//Field[] fields = cls.getFields();
		Field[] fields = obj.getClass().getFields();
		for(int i = 1; i <= columnCount; i++){
			fieldName = rsmdt.getColumnName(i);
			for(Field fieldNm : fields){
				if(fieldName.equalsIgnoreCase(fieldNm.getName())){
					fieldsMap.put(i, createGetter(fieldNm.getName()));
					break;
				}
			}
		}
		
		Method method = null;
		while(result.next()){
			try {
				obj = cls.newInstance();
				for(int i = 0; i < columnCount; i++){

					method = cls.getMethod(fieldsMap.get(i), null);
					method.invoke(obj,result.getObject(i));
				} 
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			listVO.add(obj);
		}
		
		return listVO;
	}
	
	/**
	 * @param name
	 * @return
	 */
	private String createGetter(String name){
		char c = name.charAt(0);
		c = java.lang.Character.toUpperCase(c);
		return (c + name.substring(1,name.length()-1));
	}
}
