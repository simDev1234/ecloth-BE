package com.ecloth.beta.domain.location.dto;

import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Locational {

    @NotNull
    private int x;

    @NotNull
    private int y;
}
