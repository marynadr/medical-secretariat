package by.bsu.secretariat.services.util;

import by.bsu.secretariat.dao.entity.Patient;
import org.springframework.util.StringUtils;

public class PatientNameUtil {
    public static String generateName(Patient patient){
        int spaces = 0;
        StringBuilder sb = new StringBuilder("");
        if(! StringUtils.isEmpty(patient.getFirstName())){
            sb.append(patient.getFirstName());
            spaces++;
        }
        if(! StringUtils.isEmpty(patient.getSecondName())){
            if(spaces > 0){
                spaces--;
                sb.append(" ");
            }
            sb.append(patient.getSecondName());
            spaces++;
        }
        if(! StringUtils.isEmpty(patient.getAdditionalName())){
            if(spaces > 0){
                sb.append(" ");
            }
            sb.append(patient.getAdditionalName());
        }
        return sb.toString();
    }
}
