package com.mf.jira.server.util;

import com.mf.jira.server.base.ResponseEnum;
import com.mf.jira.server.exception.JiraException;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;


public class ObjectTransformer {

    public static <T> T transform (Object source, Class<T> clazz) {

        T target = null;
        try {
            target = clazz.newInstance();
            BeanUtils.copyProperties(source, target);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new JiraException(ResponseEnum.TRANSFORM_EXCEPTION);
        }
        return target;
    }

    public static <T>List<T> transform(List<?> list, Class<T> t) {
        if (null == list){
            list = new ArrayList<>();
        }

        List<T> resultList = new ArrayList<>();

        for (Object o : list) {
            resultList.add(transform(o, t));
        }
        return resultList;
    }
}
