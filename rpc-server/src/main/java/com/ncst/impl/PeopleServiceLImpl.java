package com.ncst.impl;

import com.ncst.annotation.Service;
import com.ncst.service.BaseServiceL;
import com.ncst.dto.People;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lsy
 * @date 2022/1/13
 */
@Service
public class PeopleServiceLImpl implements BaseServiceL {
    private static final Logger logger = LoggerFactory.getLogger(PeopleServiceLImpl.class);

    @Override
    public String sayHello(People people) {
        String str = String.format("我是小区%s，爱吃小熊饼干，每天%d点上班，喜欢业主%s",
                people.getOccupation(), people.getTime(), people.getLovePeople());
        logger.warn(str);
        return "小熊饼干很脆，吃完爱流眼泪。小熊饼干很甜，想你的泪很咸";
    }
}
