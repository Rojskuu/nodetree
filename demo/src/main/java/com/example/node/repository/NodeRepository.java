package com.example.node.repository;

import com.example.node.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NodeRepository extends JpaRepository<Node, Long> {

    List<Node> findByNameAndIsActiveTrue(String name);


}
