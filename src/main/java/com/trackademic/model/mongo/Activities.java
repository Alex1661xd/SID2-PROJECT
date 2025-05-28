package com.trackademic.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activities {
    private String id;
    private String name;
    private Double percentage;

    @Builder.Default
    private Double grade = 0.0;
}
