package com.example.node.view;


import com.example.node.entity.Node;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NodeResponse {

    private Long id;

    private String name;

    private Boolean isActive;

    private Node parent;

    private List<Node> children;

    public static NodeResponse from(Node node) {
        return NodeResponse.builder()
                .id(node.getId())
                .name(node.getName())
                .isActive(node.isActive())
                .parent(node.getParent())
                .children(node.getChildren())
                .build();
    }

}
