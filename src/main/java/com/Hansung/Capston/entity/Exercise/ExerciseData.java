package com.Hansung.Capston.entity.Exercise;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Exercise_Data")
public class ExerciseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Integer exerciseId;

    @CsvBindByName(column = "exerciseName")
    private String exerciseName;


    @CsvBindByName(column = "exerciseDescription")
    private String exerciseDescription;

    @CsvBindByName(column = "energyConsumptionPerKg") // CSV 열 이름과 매핑
    private Double energyConsumptionPerKg;

    @CsvBindByName(column = "exerciseType")
    private String exerciseType;


}
