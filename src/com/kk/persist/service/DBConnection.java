/**
 * 
 */
package com.kk.persist.service;

import java.util.Map;

/**
 * @author Kartikeya
 * @param <K>
 * @param <V>
 *
 */
public interface DBConnection {
	public <T> T retrieveData(String query, Map<Integer, Object> params, Class<? extends Object> cls);
	public <T> T updateData(String query, Map<Integer, Object> params, Class<? extends Object> cls); 
	public <T> T insertData(String query, Map<Integer, Object> params, Class<? extends Object> cls);
	public <T> T bulkInsert(String query, Map<Integer, Object> params, Class<? extends Object> cls);
}
