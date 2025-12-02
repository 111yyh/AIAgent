package com.yyh.yaiagent.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileOperationToolTest {

    @Test
    void test_readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String res = fileOperationTool.readFile("1.txt");
        Assertions.assertNotNull(res);
    }

    @Test
    void test_writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String res = fileOperationTool.writeFile("1.txt", "fjadslkfj");
        Assertions.assertNotNull(res);
    }
}