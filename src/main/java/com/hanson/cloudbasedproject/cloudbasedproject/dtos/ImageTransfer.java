package com.hanson.cloudbasedproject.cloudbasedproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImageTransfer {
    private byte[] imageByte;
    private String fileName;
}
