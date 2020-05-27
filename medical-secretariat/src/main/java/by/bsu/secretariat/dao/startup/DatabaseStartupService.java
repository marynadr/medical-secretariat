package by.bsu.secretariat.dao.startup;

import by.bsu.secretariat.dao.entity.Patient;
import by.bsu.secretariat.dao.entity.User;
import by.bsu.secretariat.dao.util.NextSequenceUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DatabaseStartupService {

    @Autowired
    private MongoTemplate mongoTemplate;


    private final static Map<String, String> collections =
            ImmutableMap.of(
                    "users", "users",
                    "patients", "patients",
                    "visits", "visits",
                    "diseases", "diseases"
            );


    private static long SLEEP_TIME = 1000*1*60;

    public DatabaseStartupService(@Autowired MongoTemplate mongoTemplate) throws Exception{
        this.mongoTemplate = mongoTemplate;
        initCollections();
        initData();

        testData();
    }

    private void initCollections() throws Exception {
        for(String collectionName : collections.keySet()){
            if (!mongoTemplate.getCollectionNames().contains(collectionName)) {
                mongoTemplate.createCollection(collectionName);
            }
        }
    }

    private void initData(){
        mongoTemplate.dropCollection("users");
        mongoTemplate.createCollection("users");
        List<User> doctors = ImmutableList.of(
                User.builder()
                        .id(1000011L)
                        .role("DOCTOR")
                        .additonalInfo("Хирург")
                        .build(),
                User.builder()
                        .id(1000022L)
                        .role("DOCTOR")
                        .additonalInfo("Врач – рентгенолог")
                        .build(),
                User.builder()
                        .id(1000033L)
                        .role("DOCTOR")
                        .additonalInfo("Травматолог-ортопед")
                        .build(),
                User.builder()
                        .id(1000044L)
                        .role("DOCTOR")
                        .additonalInfo("Физиотерапевт")
                        .build(),
                User.builder()
                        .id(1000055L)
                        .role("DOCTOR")
                        .additonalInfo("Врач-невролог")
                        .build(),
                User.builder()
                        .id(1000066L)
                        .role("DOCTOR")
                        .additonalInfo("Врач-психоневролог")
                        .build()
        );

        List<User> receptionists = ImmutableList.of(
                User.builder()
                        .id(2000011L)
                        .role("RECEPTIONIST")
                        .additonalInfo("Заведующий отделом приема")
                        .build(),
                User.builder()
                        .id(2000022L)
                        .role("RECEPTIONIST")
                        .build()
        );

        User registrar = User.builder()
                .id(3000011L)
                .role("REGISTRAR")
                .build();

        User administrator = User.builder()
                .id(4000011L)
                .role("ADMINISTRATION")
                .additonalInfo("Главный врач городской клинической больницы")
                .build();

        List<User> users = new ArrayList<>(doctors);
        users.addAll(receptionists);
        users.add(registrar);
        users.add(administrator);

        mongoTemplate.insert(users, collections.get("users"));
    }

    private void testData(){

        NextSequenceUtil util = new NextSequenceUtil(mongoTemplate);

        Patient p_1 =Patient.builder()
                .id(util.getNextSequence())
                .birth(LocalDate.parse("1992-10-14"))
                .firstName("Павел")
                .secondName("Кочанов")
                .address("Минск, пр. Пушкина, 11/4")
                .status("REGISTERED")
                .build();
        Patient p_2 =Patient.builder()
                .id(util.getNextSequence())
                .birth(LocalDate.parse("1999-12-12"))
                .firstName("Евгения")
                .secondName("Паржанская")
                .address("Минск, ул. Уманская, 8")
                .status("REGISTERED")
                .build();


        mongoTemplate.dropCollection("patients");
        mongoTemplate.createCollection("patients");
        mongoTemplate.insert(p_1, collections.get("patients"));
        mongoTemplate.insert(p_2, collections.get("patients"));
    }
}
