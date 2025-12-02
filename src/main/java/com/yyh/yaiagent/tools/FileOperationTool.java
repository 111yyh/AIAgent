package com.yyh.yaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.yyh.yaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class FileOperationTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/files";

    @Tool(description = "Read content from file")
    public String readFile(@ToolParam(description = "Name of the file") String fileName) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    @Tool(description = "Write content in file")
    public String writeFile(
            @ToolParam(description = "Name of the file") String fileName,
            @ToolParam(description = "Content of the file") String content) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            FileUtil.mkdir(filePath);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to:" + filePath;
        } catch (Exception e) {
            return "Error writing file: " + e.getMessage();
        }
    }
}
