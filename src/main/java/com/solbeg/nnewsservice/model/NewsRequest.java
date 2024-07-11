package com.solbeg.nnewsservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {
    @NotNull
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private String title;
    @NotNull
    private String text;
}
