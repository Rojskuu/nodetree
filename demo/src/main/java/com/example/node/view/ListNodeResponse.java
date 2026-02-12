package com.example.node.view;

import com.example.node.entity.Node;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListNodeResponse {

    List<NodeResponse> list;

    public static ListNodeResponse create(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) return null;
        return ListNodeResponse.builder()
                .list(nodes.stream()
                        .map(NodeResponse::from)
                        .toList()
                )
                .build();
    }
}
