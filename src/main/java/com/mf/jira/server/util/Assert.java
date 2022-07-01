package com.mf.jira.server.util;

import com.mf.jira.server.exception.JiraException;
import com.mf.jira.server.base.ResponseEnum;

public class Assert {

    public static boolean singleRowAffected(int result) {
        if (result == 0) {
            throw new JiraException(ResponseEnum.NO_ROWS_AFFECTED);
        }
        if (result > 1) {
            throw new JiraException(ResponseEnum.ERROR);
        }
        return true;
    }

    public static <T> boolean notNull (T data) {
        if (data == null) throw new JiraException(ResponseEnum.ENTITY_NOT_FOUND);
        return true;
    }

}
