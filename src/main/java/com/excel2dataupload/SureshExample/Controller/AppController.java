package com.excel2dataupload.SureshExample.Controller;

import com.excel2dataupload.SureshExample.Service.AppService;
import com.excel2dataupload.SureshExample.Service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Controller("api/v1")
public class AppController {

    @Autowired
    AppService appService;

    @Autowired
    FileUploadService fileUploadService;

    @GetMapping("/")
    public String index() {
        return "uploadPage";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        fileUploadService.uploadFile(file);

        saveExcelData(file.getOriginalFilename());

        redirectAttributes.addFlashAttribute("message",
                "You have successfully uploaded '" + file.getOriginalFilename() + "' !");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/saveData")
    public String saveExcelData(String table) throws IOException {

        appService.dataExtract(table);

        return "Data Upload Successful";

    }
}

