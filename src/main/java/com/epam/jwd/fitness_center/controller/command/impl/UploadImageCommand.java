package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.*;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.ProgramService;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;

public class UploadImageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(UploadImageCommand.class);
    private final RequestFactory requestFactory;
    private final UserService userService;

    UploadImageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<User> userOptional = retrieveUserFromSession(request);
        Optional<Part> filePartOptional = request.getPart("file");
        if (!userOptional.isPresent() || !filePartOptional.isPresent()) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        User user = userOptional.get();
        Optional<String> fileNameOptional = uploadImage(request, filePartOptional.get(), user);
        if(!fileNameOptional.isPresent()) {
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        String photoPath = "./images/" + fileNameOptional.get();
        user.setPhotoPath(photoPath);
        try {
            userService.update(user);
        } catch (ServiceException e) {
            LOG.error("Unable to update user. {}", e.getMessage());
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        return requestFactory.createRedirectResponse(PagePath.SHOW_CABINET_REDIRECT);
    }

    private Optional<String> uploadImage(CommandRequest request, Part filePart, User user) {
        String fileName = user.getId() + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        File uploadedFile;
        byte[] imageBytes;
        try (InputStream fileContent = filePart.getInputStream()){
            imageBytes = new byte[fileContent.available()];
            fileContent.read(imageBytes);
            uploadedFile = new File(request.getServletContext().getRealPath("/images"), fileName);
        } catch (IOException e) {
            LOG.error("Unable to read image. {}", e.getMessage());
           return Optional.empty();
        }
        try (FileOutputStream fos = new FileOutputStream(uploadedFile)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            LOG.error("Unable to write image. {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.of(fileName);
    }
}
