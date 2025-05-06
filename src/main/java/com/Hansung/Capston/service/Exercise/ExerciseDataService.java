package com.Hansung.Capston.service.Exercise;


import com.Hansung.Capston.entity.Exercise.ExerciseData;
import com.Hansung.Capston.repository.Exercise.ExerciseDataRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ExerciseDataService {


    private final ExerciseDataRepository exerciseDataRepository;

    public ExerciseDataService(ExerciseDataRepository exerciseDataRepository) {
        this.exerciseDataRepository = exerciseDataRepository;
    }

    // 업로드된 CSV 파일을 처리하여 DB에 저장
    public void processCsvFile(MultipartFile file) throws IOException {
        System.out.println("파일 이름: " + file.getOriginalFilename());
        System.out.println("파일 크기: " + file.getSize());
        // MultipartFile을 읽기 위한 InputStreamReader로 변환
        InputStreamReader reader = new InputStreamReader(file.getInputStream());

        // OpenCSV 라이브러리로 CSV를 객체 리스트로 변환
        List<ExerciseData> exerciseDataList = new CsvToBeanBuilder<ExerciseData>(reader)
                .withType(ExerciseData.class)
                .build()
                .parse();

        // DB에 저장
        exerciseDataRepository.saveAll(exerciseDataList);
    }
}
