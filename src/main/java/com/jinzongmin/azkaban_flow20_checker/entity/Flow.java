package com.jinzongmin.azkaban_flow20_checker.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author jinzongmin
 */
public class Flow {
    @Getter
    @Setter
    private List<Node> nodes;
}
