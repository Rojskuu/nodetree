package com.example.node.service.serviceImpl;

import com.example.node.dto.NodeDto;
import com.example.node.entity.Node;
import com.example.node.repository.NodeRepository;
import com.example.node.service.NodeService;
import com.example.node.view.NodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    @Override
    public NodeResponse findById(long id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Node not found or inactive"));
        if (!node.isActive()) {
            throw new NoSuchElementException("Node not found or inactive");
        }
        NodeDto nodeDto = NodeDto.from(node);
        // Recursively remove inactive children
        nodeDto.setChildren(filterActiveChildren(node.getChildren()));
        return NodeResponse.from(nodeDto);
    }

    @Override
    public void deleteNode(long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new NoSuchElementException("Node not found"));
        cascadeSoftDelete(node);
        nodeRepository.save(node);
    }

    @Override
    public NodeResponse addChild(long parentId, String childName) {
        Node parent = nodeRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("Node not found or inactive"));
        if (!parent.isActive()) {
            throw new IllegalStateException("Cannot add child to a deleted node");
        }
        Node child = Node.builder()
                .name(childName)
                .parent(parent)
                .isActive(true)
                .build();
        nodeRepository.save(child);

        return NodeResponse.from(child);

    }

    @Override
    public NodeResponse moveNode(long nodeId, long newParentId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new NoSuchElementException("Node not found or inactive"));
        Node newParent = nodeRepository.findById(newParentId)
                .orElseThrow(() -> new NoSuchElementException("Node not found or inactive"));
        node.setParent(newParent);
        nodeRepository.save(node);

        return NodeResponse.from(node);
    }

    @Override
    public List<String> getDownline(long parentId) {
        Node parent = nodeRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("Node not found or inactive"));
        return getAllChildrenRecursive(parent).stream()
                .filter(Node::isActive)
                .map(Node::getName)
                .toList();
    }

    private List<Node> getAllChildrenRecursive(Node node) {
        List<Node> result = new ArrayList<>();
        for (Node child : node.getChildren()) {
            if (child.isActive()) {
                result.add(child);
                result.addAll(getAllChildrenRecursive(child));
            }
        }
        return result;
    }


    //recursive call for cascading delete
    private void cascadeSoftDelete(Node node) {
        node.setActive(false);
        for (Node child : node.getChildren()) {
            cascadeSoftDelete(child);
        }
    }

    private List<Node> filterActiveChildren(List<Node> children) {
        return children.stream()
                .filter(Node::isActive)
                .peek(child ->
                        child.setChildren(
                                filterActiveChildren(child.getChildren())
                        ))
                .collect(Collectors.toList());
    }
}

