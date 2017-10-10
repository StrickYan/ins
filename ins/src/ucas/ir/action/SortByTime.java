/***************************************************************************
 *
 * Copyright (c) 2017 Baidu.com, Inc. All Rights Reserved
 *
 **************************************************************************/

/**
 * @file src/ucas/ir/action/SortByTime.java
 * @author yanjing05(com@baidu.com)
 * @date 2017/04/18 21:22:39
 * @brief Sort the query result by Time.
 *
 **/

package ucas.ir.action;

import java.util.Comparator;
import ucas.ir.pojo.*;

class SortByTime implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		News n1 = (News) o1;
		News n2 = (News) o2;

		return n2.getTime().compareTo(n1.getTime());
	}

}
