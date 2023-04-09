package com.sber.java13.ecoteam;

import com.sber.java13.ecoteam.dto.WasteDTO;
import com.sber.java13.ecoteam.model.Waste;

import java.util.Arrays;
import java.util.List;

public interface WasteTestData {
    WasteDTO WASTE_DTO_1 = new WasteDTO("title1", "shortTitle1", "code1", "description1", null, null, false);
    WasteDTO WASTE_DTO_2 = new WasteDTO("title2", "shortTitle2", "code2", "description2", null, null, false);
    WasteDTO WASTE_DTO_3 = new WasteDTO("title3", "shortTitle3", "code3", "description3", null, null, false);
    List<WasteDTO> WASTE_DTO_LIST = Arrays.asList(WASTE_DTO_1, WASTE_DTO_2, WASTE_DTO_3);
    Waste WASTE_1 = new Waste("title1", "shortTitle1", "code1", "description1", null, null);
    Waste WASTE_2 = new Waste("title2", "shortTitle2", "code2", "description2", null, null);
    Waste WASTE_3 = new Waste("title3", "shortTitle3", "code3", "description3", null, null);
    List<Waste> WASTE_LIST = Arrays.asList(WASTE_1, WASTE_2, WASTE_3);
    
}
