package com.soagrowers.productcommand;

import com.soagrowers.productcommand.commands.AddProductCommand;
import com.soagrowers.productcommand.utils.Asserts;
import io.swagger.annotations.ApiOperation;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.ConcurrencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * Created by ben on 19/01/16.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRestController.class);

    @Autowired
    CommandGateway commandGateway;

    @ApiOperation(value = "add", httpMethod = "POST", code = 200, notes = "Used to ADD new products")
    @RequestMapping(value = "/add/{id}", method = RequestMethod.POST)
    public void add(@PathVariable(value = "id") String id,
                    @RequestParam(value = "name", required = true) String name,
                    HttpServletResponse response) {

        LOG.info("ADD request received: ID: {}, NAME: {}", id, name);

        try {
            Asserts.areNotEmpty(Arrays.asList(id, name));
            AddProductCommand command = new AddProductCommand(id, name);
            commandGateway.sendAndWait(command);
            LOG.info("AddProductCommand sent to command gateway: Product [{}] '{}'", id, name);
            response.setStatus(HttpServletResponse.SC_CREATED);// Set up the 201 CREATED response
            return;
        } catch (AssertionError ae){
            LOG.warn("Request failed validation. ID: {}, NAME: {}", id, name);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw ae;
        }
        catch (CommandExecutionException cex) {
            LOG.warn("AddProductCommand FAILED. Unable to execute the command. Message: {}", cex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            if (null != cex.getCause()) {
                LOG.warn("CAUSED BY: {} {}", cex.getCause().getClass().getName(), cex.getCause().getMessage());
                if (cex.getCause() instanceof ConcurrencyException) {
                    LOG.warn("ISSUE: A Product with ID [{}] already exists.", id);
                }
            }

            throw cex;
        }
    }
}