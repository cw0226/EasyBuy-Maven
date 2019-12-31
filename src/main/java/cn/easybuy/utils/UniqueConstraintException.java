package cn.easybuy.utils;

import java.io.Serializable;
import java.sql.SQLException;

public class UniqueConstraintException extends RuntimeException implements Serializable {

	//包装类异常
	public UniqueConstraintException(SQLException e) {
		super(e);
	}

}
