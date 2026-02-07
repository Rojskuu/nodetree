package com.example.node.init;

import com.example.node.entity.Node;
import com.example.node.repository.NodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RootNodeInitializer implements CommandLineRunner {

    private final NodeRepository nodeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (nodeRepository.count() == 0) {
            Node root = Node.builder()
                    .name("ROOT")
                    .parent(null)
                    .isActive(true)
                    .build();

            nodeRepository.save(root);
            System.out.println("Root node initialized.");
        } else {
            System.out.println("Root node already exists.");
        }
    }
}
