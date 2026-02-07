package com.example.node.service.serviceImpl;

import com.example.node.entity.Node;
import com.example.node.repository.NodeRepository;
import com.example.node.service.NodeService;
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
    public Node findById(long id) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Node not found or inactive"));
        if (!node.isActive()) {
            throw new NoSuchElementException("Node not found or inactive");
        }
        // Recursively remove inactive children
        node.setChildren(filterActiveChildren(node.getChildren()));
        return node;
    }

    @Override
    public List<Node> findNode(String name) {
        return nodeRepository.findByNameAndIsActiveTrue(name);
    }

    @Override
    public Node saveNode(Node node) {
        node.setActive(true);
        return nodeRepository.save(node);
    }

    @Override
    public Node updateNode(Node node) {
        if (node.getId() == null) throw new IllegalArgumentException("Node ID required");
        return nodeRepository.save(node);
    }

    @Override
    public void deleteNode(long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new NoSuchElementException("Node not found"));
        cascadeSoftDelete(node);
        nodeRepository.save(node);
    }

    @Override
    public Node addChild(long parentId, String childName) {
        Node parent = findById(parentId);
        if (!parent.isActive()) {
            throw new IllegalStateException("Cannot add child to a deleted node");
        }
        Node child = Node.builder()
                .name(childName)
                .parent(parent)
                .isActive(true)
                .build();
        return nodeRepository.save(child);
    }

    @Override
    public Node moveNode(long nodeId, long newParentId) {
        Node node = findById(nodeId);
        Node newParent = findById(newParentId);
        node.setParent(newParent);
        return nodeRepository.save(node);
    }

    @Override
    public List<String> getDownline(long parentId) {
        Node parent = findById(parentId);
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

    @Override
    public boolean isRoot(Node node) {
        return node.getParent() == null;
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

