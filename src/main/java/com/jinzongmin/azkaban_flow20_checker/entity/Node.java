package com.jinzongmin.azkaban_flow20_checker.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author jinzongmin
 */
public class Node {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Type type;
    @Getter
    @Setter
    private List<String> dependsOn;
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Boolean isValid;
}
