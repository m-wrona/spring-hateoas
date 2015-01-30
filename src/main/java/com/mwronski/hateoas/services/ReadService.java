package com.mwronski.hateoas.services;

import com.mwronski.hateoas.model.ResourceEntity;
import com.mwronski.hateoas.model.Resources;
import com.mwronski.hateoas.repositories.Repository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.Preconditions.checkArgument;
import static com.mwronski.hateoas.log.Tracer.tracer;
import static com.mwronski.hateoas.services.ServiceUtils.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Basic service that provided read access to elements of chosen type.
 * Service supports pageable access on elements.
 *
 * @param <T> type of element supported by service
 * @author Michal Wronski
 * @date 5/30/14.
 */
@RequestMapping(value = "/messages")
@RestController
public abstract class ReadService<T extends ResourceEntity> {

    protected static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * Find chosen element
     *
     * @param id of element to be found
     * @return found instance, null otherwise
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<T> find(@PathVariable String id) {
        tracer(this).info("Find element - id: %s", id);
        T element = getRepository().find(id);
        addSelfLink(element);
        return new ResponseEntity<T>(element, HttpStatus.OK);
    }

    /**
     * Get elements
     *
     * @param pageNumber number of page with elements to be displayed
     * @return non-nullable instance of resources with found elements
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    @ResponseBody
    public HttpEntity<Resources<T>> get(@PathVariable int pageNumber) {
        tracer(this).info("Getting elements - pageNumber: %d", pageNumber);
        //TODO return HTTP error instead of exception
        checkArgument(pageNumber > 0, "Page number must be a positive number");
        Resources<T> elements = new Resources<T>();
        int startIndex = pageToIndex(pageNumber, DEFAULT_PAGE_SIZE);
        elements.add(getRepository().get(startIndex, DEFAULT_PAGE_SIZE));
        addSelfLinks(elements);
        addPagingLinks(elements, pageNumber, DEFAULT_PAGE_SIZE, getRepository().size());
        return new ResponseEntity<Resources<T>>(elements, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String unknownException(HttpServletRequest req, Exception ex) {
        tracer(this).error("Error occurred while handling req: %s", ex, req.getRequestURI());
        return "Unknown exception occurred - please check API or contact support team";
    }


    /**
     * Add links needed for navigating through elements
     *
     * @param elements where links should be added
     * @param pageNumber current page
     * @param pageSize number of elements displayed on single page
     * @param maxCount total number of elements to be displayed
     */
    protected final void addPagingLinks(Resources<T> elements, int pageNumber, int pageSize, int maxCount) {
        if (hasPreviousPage(pageNumber)) {
            elements.add(linkTo(methodOn(ReadService.class).get(pageNumber - 1)).withRel("prev"));
        }
        elements.add(linkTo(methodOn(ReadService.class).get(pageNumber)).withSelfRel());
        if (hasNextPage(pageNumber, pageSize, maxCount)) {
            elements.add(linkTo(methodOn(ReadService.class).get(pageNumber + 1)).withRel("next"));
        }
    }

    /**
     * Add self links to all elements
     *
     * @param elements where links should be added
     */
    protected final void addSelfLinks(Resources<T> elements) {
        for (T element : elements.getResources()) {
            addSelfLink(element);
        }
    }

    /**
     * Add self link to given element
     *
     * @param element where link should be added
     */
    protected final void addSelfLink(T element) {
        element.add(linkTo(methodOn(ReadService.class).find(element.getEntityId())).withSelfRel());
    }

    /**
     * Get repository enabling access to elements
     *
     * @return non-nullable instance
     */
    protected abstract Repository<T> getRepository();
}
