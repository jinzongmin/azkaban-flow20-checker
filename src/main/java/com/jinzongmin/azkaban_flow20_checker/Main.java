package com.jinzongmin.azkaban_flow20_checker;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jinzongmin.azkaban_flow20_checker.entity.Flow;
import com.jinzongmin.azkaban_flow20_checker.entity.Node;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class Main {
    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void main(String[] args) {
        new Main().work(args[0]);
    }

    private void exit(String msg) {
        System.out.println(msg);
        System.exit(1);
    }

    private void work(String path) {
        Flow flow = null;
        try {
            flow = mapper.readValue(new File(path), Flow.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Node> nodes = flow.getNodes();

        if (null == nodes || nodes.isEmpty()) {
            return;
        }

        HashSet<String> nodeNames = new HashSet<>();
        int nodeSize = nodes.size();
        for (int nodeIndex = 0; nodeIndex < nodeSize; nodeIndex++) {
            Node node = nodes.get(nodeIndex);
            String nodeName = node.getName();
            if (null == nodeName || nodeName.isEmpty()) {
                exit(String.format("节点Name无效: [%d/%d]", nodeIndex + 1, nodeSize));
            } else if (nodeNames.contains(nodeName)) {
                exit(String.format("节点Name重复: %s", nodeName));
            } else {
                nodeNames.add(nodeName);
            }
        }

        for (int nodeIndex = 0; nodeIndex < nodeSize; nodeIndex++) {
            Node node = nodes.get(nodeIndex);
            String nodeName = node.getName();
            List<String> dependsOn = node.getDependsOn();

            if (null == dependsOn || dependsOn.isEmpty()) {
                continue;
            }

            HashSet<String> depends = new HashSet<>();
            int dependSize = dependsOn.size();
            for (int dependIndex = 0; dependIndex < dependSize; dependIndex++) {
                String depend = dependsOn.get(dependIndex);
                if (null == depend || depend.isEmpty()) {
                    exit(String.format("节点依赖为空[%d/%d - %d/%d] : %s", nodeIndex + 1, nodeSize, dependIndex + 1, dependSize, nodeName));
                } else if (!nodeNames.contains(depend)) {
                    exit(String.format("节点依赖不存在[%d/%d - %d/%d] : %s", nodeIndex + 1, nodeSize, dependIndex + 1, dependSize, depend));
                } else if (depends.contains(depend)) {
                    exit(String.format("节点依赖重复[%d/%d] : %s", nodeIndex + 1, nodeSize, depend));
                } else {
                    depends.add(depend);
                }
            }
        }
    }
}
