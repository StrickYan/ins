/***************************************************************************
 *
 * Copyright (c) 2017 Baidu.com, Inc. All Rights Reserved
 *
 **************************************************************************/

/**
 * @file src/ucas/ir/action/SortByHeat.java
 * @author yanjing05(com@baidu.com)
 * @date 2017/04/18 21:22:39
 * @brief Sort the query result by heat.
 *
 **/

package ucas.ir.action;

import java.util.Comparator;
import ucas.ir.pojo.*;

public class SortByHeat implements Comparator<Object>{
	@Override
	public int compare(Object o1, Object o2) {
		News n1=(News) o1;
		News n2=(News) o2;
		
		if(Integer.parseInt(n2.getShow()) > Integer.parseInt(n1.getShow())){
			return 1;
		}
		else if(Integer.parseInt(n2.getShow()) < Integer.parseInt(n1.getShow())){
			return -1;
		}
		else {
			return 0;
		}
	}
}
