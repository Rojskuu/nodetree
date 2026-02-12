package com.example.node.view;


import com.example.node.dto.NodeDto;
import com.example.node.entity.Node;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class NodeResponse {

    private Long id;

    private String name;

    private Boolean isActive;

    private Long parentId;

    private ListNodeResponse children;

    public static NodeResponse from(NodeDto node) {
        Long temp;
        if (node.getParent() == null) {
            temp = 0L;
        } else {
            temp = node.getParent().getId();
        }

        return NodeResponse.builder()
                .id(node.getId())
                .name(node.getName())
                .isActive(node.isActive())
                .parentId(Optional.ofNullable(temp).orElse(0L))
                .children(ListNodeResponse.create(node.getChildren()))
                .build();
    }

    public static NodeResponse from(Node node) {
        Long temp;
        if (node.getParent() == null) {
            temp = 0L;
        } else {
            temp = node.getParent().getId();
        }

        return NodeResponse.builder()
                .id(node.getId())
                .name(node.getName())
                .isActive(node.isActive())
                .parentId(Optional.ofNullable(temp).orElse(0L))
                .children(ListNodeResponse.create(node.getChildren()))
                .build();
    }

}
