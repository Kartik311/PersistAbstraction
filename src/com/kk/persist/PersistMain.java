/**
 * 
 */
package com.kk.persist;

import java.util.List;

import com.kk.persist.impl.DBConnectionImpl;
import com.kk.persist.service.DBConnection;

/**
 * @author Kartikeya
 *
 */
public class PersistMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnection dbImpl = DBConnectionImpl.getInstance();
		//PersistConfigManager xm= new PersistConfigManager();
		String query = "select * from employee";
		List<RulesVO> output = (List<RulesVO>)dbImpl.retrieveData(query, null, RulesVO.class);
		System.out.println(output.toString());
		//Properties prop = xm.configurePersist();
		//System.out.println(prop.toString());
	}

}
