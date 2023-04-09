package com.sber.java13.ecoteam;

import com.sber.java13.ecoteam.dto.PointDTO;
import com.sber.java13.ecoteam.model.Point;

import java.util.Arrays;
import java.util.List;

public interface PointTestData {
    PointDTO POINT_DTO_1 = new PointDTO("title1", "city1", "address1", "phone1", "workingTime1", null, 1L, null, false);
    PointDTO POINT_DTO_2 = new PointDTO("title2", "city2", "address2", "phone2", "workingTime3", null, 2L, null, false);
    PointDTO POINT_DTO_3 = new PointDTO("title3", "city3", "address3", "phone3", "workingTime3", null, 3L, null, true);
    List<PointDTO> POINT_DTO_LIST = Arrays.asList(POINT_DTO_1, POINT_DTO_2, POINT_DTO_3);
    Point POINT_1 = new Point("title1", "city1", "address1", "phone1", "workingTime1", null, null, null);
    Point POINT_2 = new Point("title2", "city2", "address2", "phone2", "workingTime2", null, null, null);
    Point POINT_3 = new Point("title3", "city3", "address3", "phone3", "workingTime3", null, null, null);
    
    List<Point> POINT_LIST = Arrays.asList(POINT_1, POINT_2, POINT_3);
}
