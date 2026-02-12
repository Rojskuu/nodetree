package com.example.node.dto;


import com.example.node.entity.Node;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class NodeDto {

    private Long id;

    private String name;

    private boolean isActive = true;

    private Node parent;

    private List<Node> children = new ArrayList<>();


    public static NodeDto from(Node node) {
        return NodeDto.builder()
                .id(node.getId())
                .name(node.getName())
                .isActive(node.isActive())
                .parent(node.getParent())
                .children(node.getChildren())
                .build();
    }
}
