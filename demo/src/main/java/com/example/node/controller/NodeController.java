package com.example.node.controller;

import com.example.node.service.NodeService;
import com.example.node.view.NodeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nodes")
@Tag(name = "Node Management",
        description = "Manage hierarchical nodes with " +
                "add, " +
                "delete, " +
                "move, and " +
                "downline operations.")
public class NodeController {

    private final NodeService service;

    @Operation(summary = "Add a child node")
    @PostMapping("/{parentId}/children")
    public ResponseEntity<NodeResponse> addChild(@PathVariable Long parentId, @RequestParam String name) {
        return ResponseEntity.ok().body(service.addChild(parentId, name));
    }

    @Operation(summary = "Soft delete a node")
    @DeleteMapping("/{nodeId}")
    public ResponseEntity<String> deleteNode(@PathVariable Long nodeId) {
        service.deleteNode(nodeId);
        return ResponseEntity.accepted().body(
                String.format("Node %s Deleted Successfully.", nodeId));
    }

    @Operation(summary = "Move a node to a new parent")
    @PutMapping("/{nodeId}/move/{newParentId}")
    public ResponseEntity<NodeResponse> moveNode(@PathVariable Long nodeId, @PathVariable Long newParentId) {
        return ResponseEntity.ok(service.moveNode(nodeId, newParentId));
    }

    @Operation(summary = "Get downline of a node")
    @GetMapping("/{parentId}/downline")
    public ResponseEntity<List<String>> getDownline(@PathVariable Long parentId) {
        List<String> downline = service.getDownline(parentId);
        return ResponseEntity.ok(downline);
    }

    @Operation(summary = "Get Nopde by Id")
    @GetMapping("/{id}")
    public ResponseEntity<NodeResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
