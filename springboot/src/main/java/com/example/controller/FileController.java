package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * File upload and download controller
 * Handles user-uploaded files (images, documents, etc.)
 */
@RestController
@RequestMapping("/files")
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    // File storage path: project_root/files/
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${server.port:9090}")
    private String port;

    @Value("${ip:localhost}")
    private String ip;
    /**
     * Upload a file
     * @param file The file to upload
     * @return Result containing the file URL
     */

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String flag;
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        }
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // File storage format: timestamp-filename
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            log.info("File uploaded successfully: {}", fileName);

        } catch (Exception e) {
            log.error("File upload failed: {}", fileName, e);
        }
        String http = "http://" + ip + ":" + port + "/files/";
        return Result.success(http + flag + "-" + fileName);
    }

    /**
     * Upload file for rich text editor
     * Returns format compatible with wangEditor
     * @param file The file to upload
     * @return Dict containing file URL in editor format
     */
    @PostMapping("/editor/upload")
    public Dict editorUpload(MultipartFile file) {
        String flag;
        synchronized (FileController.class) {
            flag = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        }
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
            log.info("Editor file uploaded successfully: {}", fileName);

        } catch (Exception e) {
            log.error("Editor file upload failed: {}", fileName, e);
        }
        String http = "http://" + ip + ":" + port + "/files/";
        return Dict.create().set("errno", 0).set("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
    }


    /**
     * Download or retrieve a file
     * @param flag The file identifier (timestamp-filename)
     * @param response HTTP response to write file content
     */
    @GetMapping("/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(flag)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(flag, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + flag);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
                log.info("File downloaded: {}", flag);
            }
        } catch (Exception e) {
            log.error("File download failed: {}", flag, e);
        }
    }

    /**
     * Delete a file
     * @param flag The file identifier (timestamp-filename)
     */
    @DeleteMapping("/{flag}")
    public void delFile(@PathVariable String flag) {
        try {
            FileUtil.del(filePath + flag);
            log.info("File deleted successfully: {}", flag);
        } catch (Exception e) {
            log.error("File deletion failed: {}", flag, e);
        }
    }
}
