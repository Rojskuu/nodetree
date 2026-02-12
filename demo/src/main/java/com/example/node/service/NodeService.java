package com.example.node.service;

import com.example.node.view.NodeResponse;

import java.util.List;

public interface NodeService {

    NodeResponse findById(long id);

    void deleteNode(long nodeId); // soft delete

    NodeResponse addChild(long parentId, String childName);

    NodeResponse moveNode(long nodeId, long newParentId);

    List<String> getDownline(long parentId);

}


