package com.example.node.service.serviceImpl;

import com.example.node.entity.Node;
import com.example.node.repository.NodeRepository;
import com.example.node.view.NodeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NodeServiceImplTest {

    @Mock
    private NodeRepository nodeRepository;

    @InjectMocks
    private NodeServiceImpl nodeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_activeNode() {
        Node node = Node.builder()
                .id(1L)
                .name("Parent")
                .isActive(true)
                .children(List.of())
                .build();

        when(nodeRepository.findById(1L)).thenReturn(Optional.of(node));

        NodeResponse response = nodeService.findById(1L);

        assertNotNull(response);
        assertEquals("Parent", response.getName());
        verify(nodeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_inactiveNodeThrowsException() {
        Node node = Node.builder()
                .id(1L)
                .name("Inactive")
                .isActive(false)
                .children(List.of())
                .build();

        when(nodeRepository.findById(1L)).thenReturn(Optional.of(node));

        assertThrows(NoSuchElementException.class, () -> nodeService.findById(1L));
    }

    @Test
    void testAddChild_success() {
        Node parent = Node.builder()
                .id(1L)
                .name("Parent")
                .isActive(true)
                .children(List.of())
                .build();

        when(nodeRepository.findById(1L)).thenReturn(Optional.of(parent));

        NodeResponse response = nodeService.addChild(1L, "Child");

        assertNotNull(response);
        assertEquals("Child", response.getName());
        verify(nodeRepository, times(1)).save(any(Node.class));
    }

    @Test
    void testDeleteNode_cascadeSoftDelete() {
        Node child = Node.builder()
                .id(2L)
                .name("Child")
                .isActive(true)
                .children(List.of())
                .build();

        Node parent = Node.builder()
                .id(1L)
                .name("Parent")
                .isActive(true)
                .children(List.of(child))
                .build();

        when(nodeRepository.findById(1L)).thenReturn(Optional.of(parent));

        nodeService.deleteNode(1L);

        assertFalse(parent.isActive());
        assertFalse(child.isActive());
        verify(nodeRepository, times(1)).save(parent);
    }

    @Test
    void testMoveNode_success() {
        Node node = Node.builder()
                .id(1L)
                .name("Node")
                .isActive(true)
                .children(List.of())
                .build();

        Node newParent = Node.builder()
                .id(2L)
                .name("NewParent")
                .isActive(true)
                .children(List.of())
                .build();

        when(nodeRepository.findById(1L)).thenReturn(Optional.of(node));
        when(nodeRepository.findById(2L)).thenReturn(Optional.of(newParent));

        NodeResponse response = nodeService.moveNode(1L, 2L);

        assertEquals("Node", response.getName());
        assertEquals(newParent, node.getParent());
        verify(nodeRepository, times(1)).save(node);
    }
}
