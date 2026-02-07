package com.example.node.service;

import com.example.node.entity.Node;

import java.util.List;

public interface NodeService {

    Node findById(long id);

    List<Node> findNode(String name);

    Node saveNode(Node node);

    Node updateNode(Node node);

    void deleteNode(long nodeId); // soft delete

    Node addChild(long parentId, String childName);

    Node moveNode(long nodeId, long newParentId);

    List<String> getDownline(long parentId);

    boolean isRoot(Node node);
}


