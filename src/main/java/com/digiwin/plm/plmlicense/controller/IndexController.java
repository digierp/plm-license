package com.digiwin.plm.plmlicense.controller;

import com.digiwin.plm.plmlicense.component.BuildLicenseUtil;
import com.digiwin.plm.plmlicense.component.CompressDownloadUtil;
import com.digiwin.plm.plmlicense.component.ModuleVo;
import com.digiwin.plm.plmlicense.model.RegisterInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuhao
 * @date 2020/4/18
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject(new RegisterInfo());
        return modelAndView;
    }


    @PostMapping(value = "/generator")
    public ModelAndView generatorLicense(@Valid RegisterInfo registerInfo, BindingResult result, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject(registerInfo);
        if(result.hasErrors()){
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                modelAndView.addObject(error.getField()+"Message", error.getDefaultMessage());
            });
            return modelAndView;
        }

        ClassPathResource classPathResource = new ClassPathResource("License.properties");
        File properties = classPathResource.getFile();
        List<File> licenses = new ArrayList<>();
        licenses.add(properties);

        BuildLicenseUtil buildLicenseUtil = new BuildLicenseUtil();
        ModuleVo module = new ModuleVo();
        BeanUtils.copyProperties(registerInfo, module);
        String licFilePath = request.getSession().getServletContext().getRealPath("/") + "License.lic";
        File lic = buildLicenseUtil.genericLicenseFile(licFilePath, module);
        licenses.add(lic);
        CompressDownloadUtil.setDownloadResponse(response, "plm-license.zip");
        CompressDownloadUtil.compressZip(licenses, response.getOutputStream());
        return modelAndView;
    }

}
